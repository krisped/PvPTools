package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import javax.inject.Inject;
import java.awt.*;

public class HighlightConfig
{
    @Inject
    private PvPToolsConfig config;

    // -------- Local Player

    public boolean isHighlightLocalPlayerEnabled()
    {
        return config.enableLocalPlayer();
    }

    public boolean shouldHighlightLocalPlayer()
    {
        return isHighlightLocalPlayerEnabled();
    }

    public boolean shouldHighlightTile()
    {
        return config.highlightTile();
    }

    public boolean shouldHighlightOutline()
    {
        return config.highlightOutline();
    }

    public boolean shouldHighlightHull()
    {
        return config.highlightHull();
    }

    public boolean shouldHighlightMinimap()
    {
        return config.highlightMinimap();
    }

    public PvPToolsConfig.MinimapAnimation getMinimapAnimation()
    {
        return config.minimapAnimation();
    }

    public Color getLocalPlayerColor()
    {
        return config.localPlayerColor();
    }

    public PvPToolsConfig.PlayerNameLocation getPlayerNameLocationLocal()
    {
        return config.playerNameLocationLocal();
    }

    // -------- Attackable Players

    public boolean isHighlightAttackableEnabled()
    {
        return config.enableAttackablePlayers();
    }

    public boolean shouldHighlightAttackableTile()
    {
        return isHighlightAttackableEnabled() && config.highlightTileAttackable();
    }

    public boolean shouldHighlightAttackableOutline()
    {
        return isHighlightAttackableEnabled() && config.highlightOutlineAttackable();
    }

    public boolean shouldHighlightAttackableHull()
    {
        return isHighlightAttackableEnabled() && config.highlightHullAttackable();
    }

    public boolean shouldHighlightAttackableMinimap()
    {
        return isHighlightAttackableEnabled() && config.highlightMinimapAttackable();
    }

    public PvPToolsConfig.MinimapAnimation getAttackableMinimapAnimation()
    {
        return config.minimapAnimationAttackable();
    }

    public Color getAttackablePlayerColor()
    {
        return config.highlightColorAttackable();
    }

    public PvPToolsConfig.PlayerNameLocation getPlayerNameLocationAttackable()
    {
        return config.playerNameLocationAttackable();
    }

    // -------- Friends

    public boolean isHighlightFriendsEnabled()
    {
        return config.enableFriendsHighlight();
    }

    public boolean shouldHighlightFriendsTile()
    {
        return isHighlightFriendsEnabled() && config.highlightFriendsTile();
    }

    public boolean shouldHighlightFriendsOutline()
    {
        return isHighlightFriendsEnabled() && config.highlightFriendsOutline();
    }

    public boolean shouldHighlightFriendsHull()
    {
        return isHighlightFriendsEnabled() && config.highlightFriendsHull();
    }

    public boolean shouldHighlightFriendsMinimap()
    {
        return isHighlightFriendsEnabled() && config.highlightFriendsMinimap();
    }

    public PvPToolsConfig.MinimapAnimation getFriendsMinimapAnimation()
    {
        return config.friendsMinimapAnimation();
    }

    public Color getFriendsHighlightColor()
    {
        Color c = config.friendsHighlightColor();
        return (c != null) ? c : Color.GREEN;
    }

    public PvPToolsConfig.PlayerNameLocation getFriendsNameLocation()
    {
        return config.friendsNameLocation();
    }

    // -------- Ignore

    public boolean isHighlightIgnoreEnabled()
    {
        return config.enableIgnoreHighlight();
    }

    public boolean shouldHighlightIgnoreTile()
    {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreTile();
    }

    public boolean shouldHighlightIgnoreOutline()
    {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreOutline();
    }

    public boolean shouldHighlightIgnoreHull()
    {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreHull();
    }

    public boolean shouldHighlightIgnoreMinimap()
    {
        return isHighlightIgnoreEnabled() && config.highlightIgnoreMinimap();
    }

    public PvPToolsConfig.MinimapAnimation getIgnoreMinimapAnimation()
    {
        return config.ignoreMinimapAnimation();
    }

    public Color getIgnoreHighlightColor()
    {
        return config.ignoreHighlightColor();
    }

    // -------- Felles-hjelp

    /**
     * Brukes til å sjekke om minst én highlight er aktivert (Local, Attackable, Friends eller Ignore).
     */
    public boolean isAnyHighlightEnabled()
    {
        return isHighlightLocalPlayerEnabled()
                || isHighlightAttackableEnabled()
                || isHighlightFriendsEnabled()
                || isHighlightIgnoreEnabled();
    }

    /**
     * For debugging: Gi en samlet oversikt over konfigurasjoner.
     */
    public String debugConfig()
    {
        return String.format(
                "Local: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Anim: %s, NameLoc: %s] | " +
                        "Attackable: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Anim: %s, NameLoc: %s] | " +
                        "Friends: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Anim: %s, NameLoc: %s] | " +
                        "Ignore: [Enabled: %s, Tile: %s, Outline: %s, Hull: %s, Minimap: %s, Color: %s, Anim: %s]",
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
