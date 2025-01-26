package com.krisped;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config {

    // Section for Attackable Players
    @ConfigSection(
            name = "Highlight Attackable Players",
            description = "Settings related to highlighting attackable players.",
            position = 0,
            closedByDefault = true
    )
    String highlightAttackablePlayersSection = "highlightAttackablePlayers";

    @ConfigItem(
            keyName = "enableAttackablePlayers",
            name = "Enable Attackable Players",
            description = "Enable or disable all highlighting for attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean enableAttackablePlayers() {
        return false;
    }

    @ConfigItem(
            keyName = "playerNameLocationAttackable",
            name = "Name and Level",
            description = "Display name and combat level of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default PlayerNameLocation playerNameLocationAttackable() {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightTileAttackable",
            name = "Highlight Tile",
            description = "Highlight the tile of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightTileAttackable() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutlineAttackable",
            name = "Highlight Outline",
            description = "Highlight the outline of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightOutlineAttackable() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHullAttackable",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightHullAttackable() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimapAttackable",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightMinimapAttackable() {
        return false;
    }

    @ConfigItem(
            keyName = "minimapAnimationAttackable",
            name = "Minimap Animation",
            description = "Select the animation type for attackable players' minimap highlights.",
            section = highlightAttackablePlayersSection
    )
    default MinimapAnimation minimapAnimationAttackable() {
        return MinimapAnimation.Static;
    }

    @ConfigItem(
            keyName = "highlightColorAttackable",
            name = "Attackable Players Color",
            description = "Select the highlight color for attackable players.",
            section = highlightAttackablePlayersSection
    )
    default Color highlightColorAttackable() {
        return Color.RED;
    }

    // Section for Local Player
    @ConfigSection(
            name = "Highlight Local Player",
            description = "Settings related to highlighting the local player.",
            position = 1,
            closedByDefault = true
    )
    String highlightLocalPlayerSection = "highlightLocalPlayer";

    @ConfigItem(
            keyName = "enableLocalPlayer",
            name = "Enable Local Player",
            description = "Enable highlighting of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean enableLocalPlayer() {
        return false;
    }

    @ConfigItem(
            keyName = "playerNameLocationLocal",
            name = "Name and Level",
            description = "Display name and combat level of the local player.",
            section = highlightLocalPlayerSection
    )
    default PlayerNameLocation playerNameLocationLocal() {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightTile",
            name = "Highlight Tile",
            description = "Highlight the tile the local player is standing on.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightTile() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightOutline() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightHull() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimap",
            name = "Highlight Minimap",
            description = "Highlight the local player's dot on the minimap.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightMinimap() {
        return false;
    }

    @ConfigItem(
            keyName = "minimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for the minimap highlight.",
            section = highlightLocalPlayerSection
    )
    default MinimapAnimation minimapAnimation() {
        return MinimapAnimation.Static;
    }

    @ConfigItem(
            keyName = "localPlayerColor",
            name = "Local Player Color",
            description = "Select the color for all local player highlights.",
            section = highlightLocalPlayerSection
    )
    default Color localPlayerColor() {
        return Color.GREEN;
    }

    // Enum for minimap animation types
    enum MinimapAnimation {
        Static,
        Pulse,
        Blink,
        Sonar
    }

    // Enum for player name and level location
    enum PlayerNameLocation {
        DISABLED,
        ABOVE_HEAD,
        CENTER_OF_MODEL,
        RIGHT_OF_MODEL
    }
}
