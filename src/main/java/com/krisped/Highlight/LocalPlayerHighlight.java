package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
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
        if (!config.enableLocalPlayer())
        {
            return;
        }

        var local = client.getLocalPlayer();
        if (local == null) return;

        renderPlayerHighlight(
                graphics,
                local,
                config.highlightTile(),
                config.highlightOutline(),
                config.highlightHull(),
                config.highlightMinimap(),
                config.localPlayerColor(),
                config.minimapAnimation(),
                config.playerNameLocationLocal()
        );
    }
}
