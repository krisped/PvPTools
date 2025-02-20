package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import com.krisped.PvPToolsConfig.TagMenuOption;
import net.runelite.api.*;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.Text;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Lar deg "tagge" spillere ved et menyvalg i Right-click
 * (enten alltid eller kun med SHIFT, avhengig av TagMenuOption).
 */
public class TagPlayerHighlight extends BaseHighlight
{
    // Basisstreng for menyvalget vårt (uten fargekoder)
    private static final String BASE_TAG_TEXT = "Tag";

    private final ConfigManager configManager;
    private EventBus eventBus;
    private boolean eventsRegistered=false;

    public TagPlayerHighlight(
            Client client,
            PvPToolsConfig config,
            ModelOutlineRenderer modelOutlineRenderer,
            SettingsHighlight settingsHighlight,
            ConfigManager configManager
    )
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
        this.configManager= configManager;
    }

    public void registerEvents(EventBus eventBus)
    {
        if(!eventsRegistered)
        {
            this.eventBus= eventBus;
            eventBus.register(this);
            eventsRegistered=true;
        }
    }

    public void unregisterEvents()
    {
        if(eventsRegistered && eventBus!=null)
        {
            eventBus.unregister(this);
            eventBus=null;
            eventsRegistered=false;
        }
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableTagPlayerHighlight()) return;

        // Finn alle taggede spillernavn
        Set<String> tset = parseTaggedPlayersList();
        if(tset.isEmpty()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            String nm = cleanNameForList(p.getName());
            if (tset.contains(nm))
            {
                // Denne spilleren er i tag-listen
                Color c = config.tagHighlightColor();

                // Outline, Hull, Tile
                if (config.tagOutline()) drawOutline(p, c);
                if (config.tagHull())    drawHull(g, p, c);
                if (config.tagTile())    drawTile(g, p, c);

                // Navn + Label
                String nameTxt = p.getName() + " (" + p.getCombatLevel() + ")";
                PvPToolsConfig.PlayerNameLocation nLoc = config.tagNameLocation();

                String lbTxt = "Tagged";
                PvPToolsConfig.PlayerNameLocation lbLoc = config.tagLabelLocation();

                LabelColorMode mode = config.tagLabelColorMode();
                Color lbColor = (mode == LabelColorMode.WHITE) ? Color.WHITE : c;

                drawNameAndLabel(g, p,
                        nameTxt, nLoc, c,
                        lbTxt, lbLoc, lbColor
                );
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableTagPlayerHighlight()) return;

        Set<String> tset = parseTaggedPlayersList();
        if(tset.isEmpty()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            String nm = cleanNameForList(p.getName());
            if (tset.contains(nm))
            {
                drawMinimapDot(g, p, config.tagHighlightColor(), config.tagMinimapAnimation());
            }
        }
    }

    /**
     * Legger til et "Tag"-menyvalg ved høyreklikk på spiller, avhengig av TagMenuOption.
     */
    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded e)
    {
        if(!config.enableTagPlayerHighlight()) return;

        TagMenuOption opt = config.tagMenuOption();
        if(opt == TagMenuOption.OFF)
        {
            return; // ingen meny
        }

        // Vi har to varianter å støtte: RIGHT_CLICK og SHIFT_RIGHT_CLICK
        if(opt == TagMenuOption.SHIFT_RIGHT_CLICK && !client.isKeyPressed(KeyCode.KC_SHIFT))
        {
            // SHIFT_RIGHT_CLICK men SHIFT er ikke nede => ingenting
            return;
        }

        // RIGHT_CLICK => alltid
        // SHIFT_RIGHT_CLICK => SHIFT holdes nede
        // => i begge tilfeller vil vi opprette menypunktet

        MenuEntry me = e.getMenuEntry();
        if(me == null || me.getOption() == null) return;

        // Du har i koden en sjekk for "Trade with" (kan endres etter ønske)
        if(!me.getOption().equalsIgnoreCase("Trade with"))
        {
            return;
        }

        // Bygg en farget streng for "Tag"
        Color color = config.tagHighlightColor();
        String colorHex = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        String coloredOption = "<col=" + colorHex + ">" + BASE_TAG_TEXT + "</col>";

        // Opprett menypunktet helt nederst i menyen
        client.createMenuEntry(client.getMenuEntries().length)
                .setOption(coloredOption)
                .setTarget(me.getTarget())
                .setType(MenuAction.RUNELITE);
    }

    /**
     * Når spilleren klikker "Tag", legger vi vedkommende i config.taggedPlayersList().
     */
    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked e)
    {
        if(!config.enableTagPlayerHighlight()) return;

        TagMenuOption opt = config.tagMenuOption();
        if(opt == TagMenuOption.OFF) return;

        // Sjekk om vi klikket på "Tag"
        String rawOp = e.getMenuOption();
        if(rawOp == null) return;

        // Fjern fargekoder
        String noTags = Text.removeTags(rawOp).trim();
        if(!noTags.equalsIgnoreCase(BASE_TAG_TEXT))
        {
            return;
        }

        // Legg til i tag-lista
        String rawTarget = e.getMenuTarget();
        if(rawTarget == null) rawTarget = "";

        String cleaned = cleanNameForList(rawTarget);
        addPlayerToTaggedList(cleaned);
    }

    private Set<String> parseTaggedPlayersList()
    {
        Set<String> set = new HashSet<>();
        String raw = config.taggedPlayersList();
        if(raw == null || raw.trim().isEmpty()) return set;

        String[] lines = raw.split("\\r?\\n");
        for(String ln: lines)
        {
            String nm = cleanNameForList(ln);
            if(!nm.isEmpty()) set.add(nm);
        }
        return set;
    }

    private void addPlayerToTaggedList(String name)
    {
        if(name.isEmpty()) return;

        Set<String> all = parseTaggedPlayersList();
        if(all.contains(name)) return;

        String exist = config.taggedPlayersList();
        if(exist == null) exist = "";
        String newVal = exist.isEmpty() ? name : (exist + "\n" + name);

        configManager.setConfiguration("pvptools", "taggedPlayersList", newVal);
    }

    private String cleanNameForList(String raw)
    {
        if(raw == null) return "";
        String noTags = raw.replaceAll("<.*?>","");
        String noLevel= noTags.replaceAll("\\(level-\\d+\\)","");
        String rep    = noLevel.replace('\u00A0',' ');
        return rep.trim().toLowerCase();
    }
}
