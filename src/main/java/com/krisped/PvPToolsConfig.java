package com.krisped;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config {

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
            description = "Enable highlighting of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean enableAttackablePlayers() {
        return false; // Default: Off
    }

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
        return false; // Default: Off
    }

    @ConfigItem(
            keyName = "highlightTile",
            name = "Highlight Tile",
            description = "Highlight the tile the local player is standing on.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightTile() {
        return false; // Default: Off
    }

    @ConfigItem(
            keyName = "highlightOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightOutline() {
        return false; // Default: Off
    }

    @ConfigItem(
            keyName = "highlightHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightHull() {
        return false; // Default: Off
    }

    @ConfigItem(
            keyName = "highlightMinimap",
            name = "Highlight Minimap",
            description = "Highlight the local player's dot on the minimap.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightMinimap() {
        return false; // Default: Off
    }

    @ConfigItem(
            keyName = "minimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for the minimap highlight.",
            section = highlightLocalPlayerSection
    )
    default MinimapAnimation minimapAnimation() {
        return MinimapAnimation.Static; // Default: Static
    }

    @ConfigItem(
            keyName = "localPlayerColor",
            name = "Local Player Color",
            description = "Select the color for all local player highlights.",
            section = highlightLocalPlayerSection
    )
    default Color localPlayerColor() {
        return Color.GREEN; // Default color
    }

    /**
     * Enum for minimap animation types.
     */
    enum MinimapAnimation {
        Static,
        Pulse,
        Blink,
        Sonar,
        Spin,
        Heartbeat,
        ExpandingWaves,
        PulsatingGlow,
        DirectionalIndicator,
        PulsingHexagon,
        DepthSpin
    }
}
