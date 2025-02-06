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
    public void renderNormal(Graphics2D g)
    {
        if (!config.enableIgnoreHighlight()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            if (isIgnored(p.getName()))
            {
                // Name
                String nameTxt = p.getName() + " (" + p.getCombatLevel() + ")";
                PvPToolsConfig.PlayerNameLocation nameLoc = config.ignoreNameLocation();

                // Label
                String labelTxt = "Ignored";
                PvPToolsConfig.PlayerNameLocation labelLoc = config.ignoreLabelLocation();

                drawNameAndLabel(g, p, nameTxt, nameLoc, config.ignoreHighlightColor(), labelTxt, labelLoc);

                // Outline
                if (config.ignoreOutline())
                {
                    drawOutline(p, config.ignoreHighlightColor());
                }

                // Hull
                if (config.ignoreHull())
                {
                    drawHull(g, p, config.ignoreHighlightColor());
                }

                // Tile
                if (config.ignoreTile())
                {
                    drawTile(g, p, config.ignoreHighlightColor());
                }
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableIgnoreHighlight()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null) continue;
            if (isIgnored(p.getName()))
            {
                drawMinimapDot(g, p, config.ignoreHighlightColor(), config.ignoreMinimapAnimation());
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
