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
                Color c = config.ignoreHighlightColor();
                // for enkelhet tegner vi ALLTID navnet over hodet, men du kan endre.
                if (config.highlightIgnoreTile())
                {
                    drawTile(g, p, c);
                }
                if (config.highlightIgnoreOutline())
                {
                    drawOutline(p, c);
                }
                if (config.highlightIgnoreHull())
                {
                    drawHull(g, p, c);
                }
                // Du kan tilpasse hvis du vil vise nameLocation
                String txt = p.getName() + " (" + p.getCombatLevel() + ")";
                drawName(g, p, txt, c, PvPToolsConfig.PlayerNameLocation.ABOVE_HEAD);
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
                if (config.highlightIgnoreMinimap())
                {
                    drawMinimapDot(g, p, config.ignoreHighlightColor(), config.ignoreMinimapAnimation());
                }
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
