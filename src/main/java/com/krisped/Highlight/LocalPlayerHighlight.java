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
    public void render(Graphics2D graphics)
    {
        // Sjekk config
        if (!config.enableLocalPlayer()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        // Kall BaseHighlight
        renderPlayerHighlight(
                graphics,
                local,
                config.highlightTileLocal(),
                config.highlightOutlineLocal(),
                config.highlightHullLocal(),
                config.highlightMinimapLocal(),
                config.localPlayerColor(),
                config.minimapAnimationLocal(),
                config.playerNameLocationLocal()
        );
    }
}
