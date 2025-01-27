package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Ignore;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class IgnoreHighlight extends BaseHighlight
{
    public IgnoreHighlight(Client client,
                           PvPToolsConfig config,
                           ModelOutlineRenderer modelOutlineRenderer,
                           SettingsHighlight settingsHighlight)
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
    }

    @Override
    public void render(Graphics2D graphics)
    {
        if (!config.enableIgnoreHighlight()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            if (isIgnored(p.getName()))
            {
                // f.eks. ALWAYS show name above head
                renderPlayerHighlight(
                        graphics,
                        p,
                        config.highlightIgnoreTile(),
                        config.highlightIgnoreOutline(),
                        config.highlightIgnoreHull(),
                        config.highlightIgnoreMinimap(),
                        config.ignoreHighlightColor(),
                        config.ignoreMinimapAnimation(),
                        PvPToolsConfig.PlayerNameLocation.ABOVE_HEAD
                );
            }
        }
    }

    private boolean isIgnored(String name)
    {
        if (name == null || client.getIgnoreContainer() == null) return false;
        Ignore i = client.getIgnoreContainer().findByName(name);
        return (i != null);
    }
}
