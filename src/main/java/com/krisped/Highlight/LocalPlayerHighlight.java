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
        if (!config.enableLocalPlayerHighlight())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        // For navnet
        String nameTxt = local.getName() + " (" + local.getCombatLevel() + ")";
        PvPToolsConfig.PlayerNameLocation nameLoc = config.localPlayerNameLocation();

        // For label
        String labelTxt = "Local Player";
        PvPToolsConfig.PlayerNameLocation labelLoc = config.localPlayerLabelLocation();

        // Tegn label + name (uten overlapp)
        drawNameAndLabel(g, local, nameTxt, nameLoc, config.localPlayerColor(), labelTxt, labelLoc);

        // Outline
        if (config.localPlayerOutline())
        {
            drawOutline(local, config.localPlayerColor());
        }

        // Hull
        if (config.localPlayerHull())
        {
            drawHull(g, local, config.localPlayerColor());
        }

        // Tile
        if (config.localPlayerTile())
        {
            drawTile(g, local, config.localPlayerColor());
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableLocalPlayerHighlight())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        drawMinimapDot(g, local, config.localPlayerColor(), config.localPlayerMinimapAnimation());
    }
}
