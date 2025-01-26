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

    // Section for Friends
    @ConfigSection(
            name = "Highlight Friends",
            description = "Settings related to highlighting friends.",
            position = 2,
            closedByDefault = true
    )
    String highlightFriendsSection = "highlightFriends";

    @ConfigItem(
            keyName = "enableFriendsHighlight",
            name = "Enable Friends Highlight",
            description = "Enable or disable all highlighting for friends.",
            section = highlightFriendsSection
    )
    default boolean enableFriendsHighlight() {
        return false;
    }

    @ConfigItem(
            keyName = "friendsHighlightColor",
            name = "Friends Highlight Color",
            description = "Select the highlight color for friends.",
            section = highlightFriendsSection
    )
    default Color friendsHighlightColor() {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "friendsNameLocation",
            name = "Friends Name and Level",
            description = "Display name and combat level of friends.",
            section = highlightFriendsSection
    )
    default PlayerNameLocation friendsNameLocation() {
        return PlayerNameLocation.ABOVE_HEAD;
    }

    @ConfigItem(
            keyName = "highlightFriendsTile",
            name = "Highlight Tile",
            description = "Highlight the tile of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsTile() {
        return true;
    }

    @ConfigItem(
            keyName = "highlightFriendsOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsOutline() {
        return true;
    }

    @ConfigItem(
            keyName = "highlightFriendsHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsHull() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightFriendsMinimap",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsMinimap() {
        return true;
    }

    @ConfigItem(
            keyName = "friendsMinimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for friends' minimap highlights.",
            section = highlightFriendsSection
    )
    default MinimapAnimation friendsMinimapAnimation() {
        return MinimapAnimation.Pulse;
    }

    // Section for Ignore List
    @ConfigSection(
            name = "Highlight Ignore",
            description = "Settings related to highlighting players on the Ignore list.",
            position = 3,
            closedByDefault = true
    )
    String highlightIgnoreSection = "highlightIgnore";

    @ConfigItem(
            keyName = "enableIgnoreHighlight",
            name = "Enable Ignore Highlight",
            description = "Enable or disable all highlighting for players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean enableIgnoreHighlight() {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreHighlightColor",
            name = "Ignore Highlight Color",
            description = "Select the highlight color for players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default Color ignoreHighlightColor() {
        return Color.MAGENTA;
    }

    @ConfigItem(
            keyName = "highlightIgnoreTile",
            name = "Highlight Tile",
            description = "Highlight the tile of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreTile() {
        return true;
    }

    @ConfigItem(
            keyName = "highlightIgnoreOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreOutline() {
        return true;
    }

    @ConfigItem(
            keyName = "highlightIgnoreHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreHull() {
        return false;
    }

    @ConfigItem(
            keyName = "highlightIgnoreMinimap",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreMinimap() {
        return true;
    }

    @ConfigItem(
            keyName = "ignoreMinimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for Ignore list players' minimap highlights.",
            section = highlightIgnoreSection
    )
    default MinimapAnimation ignoreMinimapAnimation() {
        return MinimapAnimation.Blink;
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
