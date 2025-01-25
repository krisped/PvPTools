package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import javax.inject.Inject;
import java.awt.*;

public class HighlightConfig {

    @Inject
    private PvPToolsConfig config;

    /**
     * Sjekker om highlighting for den lokale spilleren er aktivert.
     */
    public boolean shouldHighlightLocalPlayer() {
        return config.enableLocalPlayer();
    }

    /**
     * Sjekker om highlighting av fliser er aktivert.
     */
    public boolean shouldHighlightTile() {
        return config.highlightTile();
    }

    /**
     * Sjekker om outline highlighting er aktivert.
     */
    public boolean shouldHighlightOutline() {
        return config.highlightOutline();
    }

    /**
     * Sjekker om hull highlighting er aktivert.
     */
    public boolean shouldHighlightHull() {
        return config.highlightHull(); // Henter verdien fra PvPToolsConfig
    }

    /**
     * Sjekker om minimap highlighting er aktivert.
     */
    public boolean shouldHighlightMinimap() {
        return config.highlightMinimap(); // Henter verdien fra PvPToolsConfig
    }

    /**
     * Henter den valgte minimap-animasjonen.
     */
    public PvPToolsConfig.MinimapAnimation getMinimapAnimation() {
        return config.minimapAnimation(); // Henter verdien fra PvPToolsConfig
    }

    /**
     * Henter fargen for local player highlights.
     */
    public Color getLocalPlayerColor() {
        return config.localPlayerColor(); // Henter fargen fra PvPToolsConfig
    }

    /**
     * Sjekker om hele "Highlight Local Player"-seksjonen er aktivert.
     */
    public boolean isHighlightLocalPlayerEnabled() {
        return config.enableLocalPlayer();
    }

    /**
     * Henter en samlet oversikt over alle konfigurasjonsverdier.
     */
    public String debugConfig() {
        return String.format(
                "HighlightConfig Debug - Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Animation: %s",
                shouldHighlightTile(),
                shouldHighlightOutline(),
                shouldHighlightHull(),
                shouldHighlightMinimap(),
                getLocalPlayerColor(),
                getMinimapAnimation()
        );
    }
}
