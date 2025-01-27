package com.krisped.Highlight;

import com.google.inject.Inject;
import com.krisped.PvPToolsConfig;
import net.runelite.api.*;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Lar deg tagge spillere manuelt (via config-liste eller høyreklikk).
 * Viser kun "Tag" under "Trade with <player>" for å unngå at det
 * dukker opp under Attack, Follow, osv.
 */
public class TagPlayerHighlight extends BaseHighlight
{
    private final ConfigManager configManager;
    private final EventBus eventBus;

    @Inject
    public TagPlayerHighlight(Client client,
                              PvPToolsConfig config,
                              ModelOutlineRenderer modelOutlineRenderer,
                              SettingsHighlight settingsHighlight,
                              ConfigManager configManager,
                              EventBus eventBus)
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
        this.configManager = configManager;
        this.eventBus = eventBus;

        // Lytt til menyhendelser
        eventBus.register(this);
    }

    @Override
    public void render(Graphics2D graphics)
    {
        // Tegn bare hvis Tag Player Highlight er på
        if (!config.enableTagPlayerHighlight())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null) return;

        // Hent alle "taggede" spillere (lowercase)
        Set<String> taggedPlayers = parseTaggedPlayersList();

        // Loop over players i verden
        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            String nameLc = cleanName(p.getName());
            if (taggedPlayers.contains(nameLc))
            {
                renderPlayerHighlight(
                        graphics,
                        p,
                        config.highlightTileTag(),
                        config.highlightOutlineTag(),
                        config.highlightHullTag(),
                        config.highlightMinimapTag(),
                        config.tagHighlightColor(),
                        config.tagMinimapAnimation(),
                        config.playerNameLocationTag()
                );
            }
        }
    }

    /**
     * Overvåker menyhendinger. Hvis menypunktet er "Trade with", legger vi til "Tag".
     */
    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        // Bare hvis Tag Player er på og Right Click Tag er aktivert.
        if (!config.enableTagPlayerHighlight() || !config.enableRightClickTagPlayer())
        {
            return;
        }

        MenuEntry menuEntry = event.getMenuEntry();
        // Hent "option" - f.eks. "Trade with"
        String baseOpt = menuEntry.getOption();
        if (baseOpt == null)
        {
            return;
        }

        // Sjekk om baseOpt er "Trade with" (der du vil legge inn Tag)
        if (baseOpt.equalsIgnoreCase("Trade with"))
        {
            // Velg farge fra config
            Color color = config.tagHighlightColor();
            String colorTag = String.format("<col=%02X%02X%02X>", color.getRed(), color.getGreen(), color.getBlue());

            // Legg til en RUNELITE-meny for "Tag"
            client.createMenuEntry(-1)
                    .setOption(colorTag + "Tag</col>") // Eks. <col=FFA500>Tag</col>
                    .setTarget(menuEntry.getTarget())  // Samme target, f.eks. <col=ffffff>PlayerName
                    .setType(MenuAction.RUNELITE)
                    .onClick(e ->
                    {
                        // Her lagrer vi navnet rett i config-liste
                        String playerName = cleanName(menuEntry.getTarget());
                        addPlayerToTaggedList(playerName);
                    });
        }
    }

    // ------------------------------------------------
    //  Metoder for parsing/lagring av config-liste
    // ------------------------------------------------
    private Set<String> parseTaggedPlayersList()
    {
        Set<String> result = new HashSet<>();
        String raw = config.taggedPlayersList();
        if (raw == null || raw.trim().isEmpty())
        {
            return result;
        }

        String[] lines = raw.split("\r?\n");
        for (String line : lines)
        {
            String name = cleanName(line);
            if (!name.isEmpty())
            {
                result.add(name);
            }
        }
        return result;
    }

    private void addPlayerToTaggedList(String playerName)
    {
        if (playerName.isEmpty())
        {
            return;
        }

        // Sjekk om allerede tagget
        Set<String> all = parseTaggedPlayersList();
        if (all.contains(playerName))
        {
            return; // finnes fra før
        }

        String existing = config.taggedPlayersList();
        if (existing == null)
        {
            existing = "";
        }

        // Legg til på ny linje
        String newValue = existing.isEmpty()
                ? playerName
                : existing + "\n" + playerName;

        // Oppdater config
        configManager.setConfiguration("pvptools", "taggedPlayersList", newValue);
    }

    private String cleanName(String n)
    {
        if (n == null) return "";
        // Fjerner fargekoder, NBSP, og gjør lowercase
        return n.replaceAll("<.*?>", "")
                .replace('\u00A0', ' ')
                .toLowerCase()
                .trim();
    }
}
