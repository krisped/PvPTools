package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import javax.inject.Inject;
import java.awt.*;

public class HighlightConfig {

    @Inject
    private PvPToolsConfig config;

    // Local Player Highlight Configurations

    /**
     * Sjekker om hele "Highlight Local Player"-seksjonen er aktivert.
     */
    public boolean isHighlightLocalPlayerEnabled() {
        return config.enableLocalPlayer();
    }

    /**
     * Alias for isHighlightLocalPlayerEnabled().
     */
    public boolean shouldHighlightLocalPlayer() {
        return isHighlightLocalPlayerEnabled();
    }

    /**
     * Sjekker om highlighting av fliser for den lokale spilleren er aktivert.
     */
    public boolean shouldHighlightTile() {
        return config.highlightTile();
    }

    /**
     * Sjekker om outline highlighting for den lokale spilleren er aktivert.
     */
    public boolean shouldHighlightOutline() {
        return config.highlightOutline();
    }

    /**
     * Sjekker om hull highlighting for den lokale spilleren er aktivert.
     */
    public boolean shouldHighlightHull() {
        return config.highlightHull();
    }

    /**
     * Sjekker om minimap highlighting for den lokale spilleren er aktivert.
     */
    public boolean shouldHighlightMinimap() {
        return config.highlightMinimap();
    }

    /**
     * Henter den valgte minimap-animasjonen for den lokale spilleren.
     */
    public PvPToolsConfig.MinimapAnimation getMinimapAnimation() {
        return config.minimapAnimation();
    }

    /**
     * Henter fargen for den lokale spilleren.
     */
    public Color getLocalPlayerColor() {
        return config.localPlayerColor();
    }

    /**
     * Henter plasseringen av navn og nivå for den lokale spilleren.
     */
    public PvPToolsConfig.PlayerNameLocation getPlayerNameLocationLocal() {
        return config.playerNameLocationLocal();
    }

    // Attackable Players Highlight Configurations

    /**
     * Sjekker om hele "Highlight Attackable Players"-seksjonen er aktivert.
     */
    public boolean isHighlightAttackableEnabled() {
        return config.enableAttackablePlayers();
    }

    /**
     * Sjekker om highlighting av fliser for angrepsbare spillere er aktivert.
     */
    public boolean shouldHighlightAttackableTile() {
        return isHighlightAttackableEnabled() && config.highlightTileAttackable();
    }

    /**
     * Sjekker om outline highlighting for angrepsbare spillere er aktivert.
     */
    public boolean shouldHighlightAttackableOutline() {
        return isHighlightAttackableEnabled() && config.highlightOutlineAttackable();
    }

    /**
     * Sjekker om hull highlighting for angrepsbare spillere er aktivert.
     */
    public boolean shouldHighlightAttackableHull() {
        return isHighlightAttackableEnabled() && config.highlightHullAttackable();
    }

    /**
     * Sjekker om minimap highlighting for angrepsbare spillere er aktivert.
     */
    public boolean shouldHighlightAttackableMinimap() {
        return isHighlightAttackableEnabled() && config.highlightMinimapAttackable();
    }

    /**
     * Henter den valgte minimap-animasjonen for angrepsbare spillere.
     */
    public PvPToolsConfig.MinimapAnimation getAttackableMinimapAnimation() {
        return config.minimapAnimationAttackable();
    }

    /**
     * Henter fargen for angrepsbare spillere.
     */
    public Color getAttackablePlayerColor() {
        return config.highlightColorAttackable();
    }

    /**
     * Henter plasseringen av navn og nivå for angrepsbare spillere.
     */
    public PvPToolsConfig.PlayerNameLocation getPlayerNameLocationAttackable() {
        return config.playerNameLocationAttackable();
    }

    // Friends Highlight Configurations

    /**
     * Sjekker om hele "Highlight Friends"-seksjonen er aktivert.
     */
    public boolean isHighlightFriendsEnabled() {
        return config.enableFriendsHighlight();
    }

    /**
     * Sjekker om highlighting av fliser for venner er aktivert.
     */
    public boolean shouldHighlightFriendsTile() {
        return isHighlightFriendsEnabled() && config.highlightFriendsTile();
    }

    /**
     * Sjekker om outline highlighting for venner er aktivert.
     */
    public boolean shouldHighlightFriendsOutline() {
        return isHighlightFriendsEnabled() && config.highlightFriendsOutline();
    }

    /**
     * Sjekker om hull highlighting for venner er aktivert.
     */
    public boolean shouldHighlightFriendsHull() {
        return isHighlightFriendsEnabled() && config.highlightFriendsHull();
    }

    /**
     * Sjekker om minimap highlighting for venner er aktivert.
     */
    public boolean shouldHighlightFriendsMinimap() {
        return isHighlightFriendsEnabled() && config.highlightFriendsMinimap();
    }

    /**
     * Henter minimap-animasjonen for venner.
     */
    public PvPToolsConfig.MinimapAnimation getFriendsMinimapAnimation() {
        return config.friendsMinimapAnimation();
    }

    /**
     * Henter fargen for venner.
     */
    public Color getFriendsHighlightColor() {
        Color defaultColor = Color.GREEN; // Standard farge hvis ingen er satt
        return config.friendsHighlightColor() != null ? config.friendsHighlightColor() : defaultColor;
    }

    /**
     * Henter plasseringen av navn og nivå for venner.
     */
    public PvPToolsConfig.PlayerNameLocation getFriendsNameLocation() {
        return config.friendsNameLocation();
    }

    // Ignore List Highlight Configurations

    /**
     * Sjekker om hele "Highlight Ignore"-seksjonen er aktivert.
     */
    public boolean isHighlightIgnoreEnabled() {
        return config.enableIgnoreHighlight();
    }

    /**
     * Sjekker om highlighting av fliser for Ignore-listen er aktivert.
     */
    public boolean shouldHighlightIgnoreTile() {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreTile();
    }

    /**
     * Sjekker om outline highlighting for Ignore-listen er aktivert.
     */
    public boolean shouldHighlightIgnoreOutline() {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreOutline();
    }

    /**
     * Sjekker om hull highlighting for Ignore-listen er aktivert.
     */
    public boolean shouldHighlightIgnoreHull() {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreHull();
    }

    /**
     * Sjekker om minimap highlighting for Ignore-listen er aktivert.
     */
    public boolean shouldHighlightIgnoreMinimap() {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreMinimap();
    }

    /**
     * Henter minimap-animasjonen for Ignore-listen.
     */
    public PvPToolsConfig.MinimapAnimation getIgnoreMinimapAnimation() {
        return config.ignoreMinimapAnimation();
    }

    /**
     * Henter fargen for Ignore-listen.
     */
    public Color getIgnoreHighlightColor() {
        return config.ignoreHighlightColor();
    }

    // Debugging Utility

    /**
     * Gir en samlet oversikt over alle konfigurasjonsverdier for debugging.
     */
    public String debugConfig() {
        return String.format(
                "HighlightConfig Debug - Local Player: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Animation: %s, NameLocation: %s] " +
                        "Attackable Players: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Animation: %s, NameLocation: %s] " +
                        "Friends: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Animation: %s, NameLocation: %s] " +
                        "Ignore: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Animation: %s]",
                isHighlightLocalPlayerEnabled(),
                shouldHighlightTile(),
                shouldHighlightOutline(),
                shouldHighlightHull(),
                shouldHighlightMinimap(),
                getLocalPlayerColor(),
                getMinimapAnimation(),
                getPlayerNameLocationLocal(),
                isHighlightAttackableEnabled(),
                shouldHighlightAttackableTile(),
                shouldHighlightAttackableOutline(),
                shouldHighlightAttackableHull(),
                shouldHighlightAttackableMinimap(),
                getAttackablePlayerColor(),
                getAttackableMinimapAnimation(),
                getPlayerNameLocationAttackable(),
                isHighlightFriendsEnabled(),
                shouldHighlightFriendsTile(),
                shouldHighlightFriendsOutline(),
                shouldHighlightFriendsHull(),
                shouldHighlightFriendsMinimap(),
                getFriendsHighlightColor(),
                getFriendsMinimapAnimation(),
                getFriendsNameLocation(),
                isHighlightIgnoreEnabled(),
                shouldHighlightIgnoreTile(),
                shouldHighlightIgnoreOutline(),
                shouldHighlightIgnoreHull(),
                shouldHighlightIgnoreMinimap(),
                getIgnoreHighlightColor(),
                getIgnoreMinimapAnimation()
        );
    }
}
