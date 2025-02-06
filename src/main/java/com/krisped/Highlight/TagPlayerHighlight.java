package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.TagMenuOption;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class TagPlayerHighlight extends BaseHighlight
{
    private final ConfigManager configManager;
    private EventBus eventBus;
    private boolean eventsRegistered = false;

    public TagPlayerHighlight(
            Client client,
            PvPToolsConfig config,
            ModelOutlineRenderer modelOutlineRenderer,
            SettingsHighlight settingsHighlight,
            ConfigManager configManager
    )
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
        this.configManager = configManager;
    }

    public void registerEvents(EventBus eventBus)
    {
        if (!eventsRegistered)
        {
            this.eventBus = eventBus;
            eventBus.register(this);
            eventsRegistered = true;
        }
    }

    public void unregisterEvents()
    {
        if (eventsRegistered && eventBus != null)
        {
            eventBus.unregister(this);
            eventBus = null;
            eventsRegistered = false;
        }
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if (!config.enableTagPlayerHighlight()) return;

        Set<String> tagged = parseTaggedPlayersList();

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            String nameLc = cleanNameForList(p.getName());
            if (tagged.contains(nameLc))
            {
                // Name
                String nameTxt = p.getName() + " (" + p.getCombatLevel() + ")";
                PvPToolsConfig.PlayerNameLocation nameLoc = config.tagNameLocation();

                // Label
                String labelTxt = "Tagged";
                PvPToolsConfig.PlayerNameLocation labelLoc = config.tagLabelLocation();

                drawNameAndLabel(g, p, nameTxt, nameLoc, config.tagHighlightColor(), labelTxt, labelLoc);

                // Outline
                if (config.tagOutline())
                {
                    drawOutline(p, config.tagHighlightColor());
                }

                // Hull
                if (config.tagHull())
                {
                    drawHull(g, p, config.tagHighlightColor());
                }

                // Tile
                if (config.tagTile())
                {
                    drawTile(g, p, config.tagHighlightColor());
                }
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableTagPlayerHighlight()) return;

        Set<String> tagged = parseTaggedPlayersList();
        for (Player p : client.getPlayers())
        {
            if (p == null) continue;

            String nameLc = cleanNameForList(p.getName());
            if (tagged.contains(nameLc))
            {
                drawMinimapDot(g, p, config.tagHighlightColor(), config.tagMinimapAnimation());
            }
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        if (!config.enableTagPlayerHighlight()) return;

        TagMenuOption option = config.tagMenuOption();
        if (option == TagMenuOption.OFF)
        {
            return;
        }
        else if (option == TagMenuOption.SHIFT_RIGHT_CLICK)
        {
            // Vises kun om SHIFT holdes nede FØR man rightklikker
            if (!client.isKeyPressed(KeyEvent.VK_SHIFT))
            {
                return;
            }
        }
        // => RIGHT_CLICK viser alltid

        MenuEntry entry = event.getMenuEntry();
        if (entry == null || entry.getOption() == null) return;

        if (entry.getOption().equalsIgnoreCase("Trade with"))
        {
            Color c = config.tagHighlightColor();
            String colorTag = String.format("<col=%02X%02X%02X>", c.getRed(), c.getGreen(), c.getBlue());
            client.createMenuEntry(client.getMenuEntries().length)
                    .setOption(colorTag + "Tag</col>")
                    .setTarget(entry.getTarget())
                    .setType(MenuAction.RUNELITE);
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event)
    {
        if (!config.enableTagPlayerHighlight()) return;

        TagMenuOption option = config.tagMenuOption();
        if (option == TagMenuOption.OFF)
        {
            return;
        }
        else if (option == TagMenuOption.SHIFT_RIGHT_CLICK)
        {
            if (!client.isKeyPressed(KeyEvent.VK_SHIFT))
            {
                return;
            }
        }

        String rawOption = event.getMenuOption();
        if (rawOption == null) return;

        // Fjern fargetag
        String optionNoColor = rawOption.replaceAll("<[^>]*>", "").trim();
        if (!optionNoColor.equalsIgnoreCase("Tag"))
        {
            return;
        }

        // Legg til i config
        String rawTarget = event.getMenuTarget();
        if (rawTarget == null) rawTarget = "";

        String targetName = cleanNameForList(rawTarget);
        addPlayerToTaggedList(targetName);
    }

    // ---------------------------------
    //  Hjelp: parse liste
    // ---------------------------------
    private Set<String> parseTaggedPlayersList()
    {
        Set<String> result = new HashSet<>();
        String raw = config.taggedPlayersList();
        if (raw == null || raw.trim().isEmpty())
        {
            return result;
        }

        String[] lines = raw.split("\\r?\\n");
        for (String line : lines)
        {
            String nm = cleanNameForList(line);
            if (!nm.isEmpty())
            {
                result.add(nm);
            }
        }
        return result;
    }

    private void addPlayerToTaggedList(String playerName)
    {
        if (playerName.isEmpty()) return;

        Set<String> all = parseTaggedPlayersList();
        if (all.contains(playerName)) return;

        String existing = config.taggedPlayersList();
        if (existing == null) existing = "";

        String newVal = existing.isEmpty()
                ? playerName
                : existing + "\n" + playerName;

        configManager.setConfiguration("pvptools", "taggedPlayersList", newVal);
    }

    private String cleanNameForList(String raw)
    {
        if (raw == null) return "";
        String noCol = raw.replaceAll("<.*?>", "");
        String noLevel = noCol.replaceAll("\\(level-\\d+\\)", "");
        String replaced = noLevel.replace('\u00A0', ' ');
        return replaced.trim().toLowerCase();
    }
}
