package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
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

        Player local = client.getLocalPlayer();
        if (local == null) return;

        Set<String> tagged = parseTaggedPlayersList();

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            String nameLc = cleanNameForList(p.getName());
            if (tagged.contains(nameLc))
            {
                Color c = config.tagHighlightColor();
                boolean showName = (config.playerNameLocationTag() != PvPToolsConfig.PlayerNameLocation.DISABLED);

                if (config.highlightTileTag())
                {
                    drawTile(g, p, c);
                }
                if (config.highlightOutlineTag())
                {
                    drawOutline(p, c);
                }
                if (config.highlightHullTag())
                {
                    drawHull(g, p, c);
                }
                if (showName)
                {
                    String txt = p.getName() + " (" + p.getCombatLevel() + ")";
                    drawName(g, p, txt, c, config.playerNameLocationTag());
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
                if (config.highlightMinimapTag())
                {
                    drawMinimapDot(g, p, config.tagHighlightColor(), config.tagMinimapAnimation());
                }
            }
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        if (!config.enableTagPlayerHighlight() || !config.enableRightClickTagPlayer()) return;

        MenuEntry entry = event.getMenuEntry();
        if (entry == null) return;

        String opt = entry.getOption();
        if (opt == null) return;

        if (opt.equalsIgnoreCase("Trade with"))
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
        if (!config.enableTagPlayerHighlight() || !config.enableRightClickTagPlayer()) return;

        String rawOption = event.getMenuOption();
        if (rawOption == null) return;

        String optionNoColor = rawOption.replaceAll("<[^>]*>", "").trim();
        if (!optionNoColor.equalsIgnoreCase("Tag"))
        {
            return;
        }

        String rawTarget = event.getMenuTarget();
        if (rawTarget == null) rawTarget = "";

        String targetName = cleanNameForList(rawTarget);
        addPlayerToTaggedList(targetName);
    }

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

        String newVal = existing.isEmpty() ? playerName : existing + "\n" + playerName;
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
