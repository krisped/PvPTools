package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class LocalPlayerHighlight extends BaseHighlight
{
    public LocalPlayerHighlight(Client client,
                                PvPToolsConfig config,
                                ModelOutlineRenderer modelOutlineRenderer,
                                SettingsHighlight settingsHighlight)
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if (!config.enableLocalPlayer()) return;
        Player local = client.getLocalPlayer();
        if (local == null) return;

        Color c = config.localPlayerColor();
        boolean showName = (config.playerNameLocationLocal() != PvPToolsConfig.PlayerNameLocation.DISABLED);

        if (config.highlightTileLocal())
        {
            drawTile(g, local, c);
        }
        if (config.highlightOutlineLocal())
        {
            drawOutline(local, c);
        }
        if (config.highlightHullLocal())
        {
            drawHull(g, local, c);
        }
        if (showName)
        {
            String txt = local.getName() + " (" + local.getCombatLevel() + ")";
            drawName(g, local, txt, c, config.playerNameLocationLocal());
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableLocalPlayer()) return;
        Player local = client.getLocalPlayer();
        if (local == null) return;

        if (config.highlightMinimapLocal())
        {
            drawMinimapDot(g, local, config.localPlayerColor(), config.minimapAnimationLocal());
        }
    }
}
