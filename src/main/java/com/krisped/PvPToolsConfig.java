package com.krisped;

import net.runelite.client.config.*;
import java.awt.*;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config
{
    // ------------------------------------------------------
    // (1) Settings Highlight (outlineThickness osv.)
    // ------------------------------------------------------
    @ConfigSection(
            name = "Settings Highlight",
            description = "Global highlight settings (e.g. outline thickness).",
            position = 0,
            closedByDefault = true
    )
    String highlightSettingsSection = "highlightSettingsSection";

    @ConfigItem(
            keyName = "outlineThickness",
            name = "Outline Thickness",
            description = "Thickness of the highlight outline.",
            section = highlightSettingsSection
    )
    default int outlineThickness()
    {
        return 2;
    }

    // ------------------------------------------------------
    // (2) Attackable Players
    // ------------------------------------------------------
    @ConfigSection(
            name = "Highlight Attackable Players",
            description = "Settings related to highlighting attackable players.",
            position = 1,
            closedByDefault = true
    )
    String highlightAttackablePlayersSection = "highlightAttackablePlayers";

    @ConfigItem(
            keyName = "enableAttackablePlayers",
            name = "Enable Attackable Players",
            description = "Enable or disable all highlighting for attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean enableAttackablePlayers()
    {
        return false;
    }

    @ConfigItem(
            keyName = "playerNameLocationAttackable",
            name = "Name and Level",
            description = "Display name and combat level of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default PlayerNameLocation playerNameLocationAttackable()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightTileAttackable",
            name = "Highlight Tile",
            description = "Highlight the tile of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightTileAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutlineAttackable",
            name = "Highlight Outline",
            description = "Highlight the outline of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightOutlineAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHullAttackable",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightHullAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimapAttackable",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of attackable players.",
            section = highlightAttackablePlayersSection
    )
    default boolean highlightMinimapAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "minimapAnimationAttackable",
            name = "Minimap Animation",
            description = "Select the animation type for attackable players' minimap highlights.",
            section = highlightAttackablePlayersSection
    )
    default MinimapAnimation minimapAnimationAttackable()
    {
        return MinimapAnimation.Static;
    }

    // Default color = Blue
    @ConfigItem(
            keyName = "highlightColorAttackable",
            name = "Attackable Players Color",
            description = "Select the highlight color for attackable players.",
            section = highlightAttackablePlayersSection
    )
    default Color highlightColorAttackable()
    {
        return Color.BLUE;
    }


    // ------------------------------------------------------
    // (3) Local Player
    // ------------------------------------------------------
    @ConfigSection(
            name = "Highlight Local Player",
            description = "Settings related to highlighting the local player.",
            position = 2,
            closedByDefault = true
    )
    String highlightLocalPlayerSection = "highlightLocalPlayer";

    @ConfigItem(
            keyName = "enableLocalPlayer",
            name = "Enable Local Player",
            description = "Enable highlighting of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean enableLocalPlayer()
    {
        return false;
    }

    @ConfigItem(
            keyName = "playerNameLocationLocal",
            name = "Name and Level",
            description = "Display name and combat level of the local player.",
            section = highlightLocalPlayerSection
    )
    default PlayerNameLocation playerNameLocationLocal()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightTile",
            name = "Highlight Tile",
            description = "Highlight the tile the local player is standing on.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of the local player.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimap",
            name = "Highlight Minimap",
            description = "Highlight the local player's dot on the minimap.",
            section = highlightLocalPlayerSection
    )
    default boolean highlightMinimap()
    {
        return false;
    }

    @ConfigItem(
            keyName = "minimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for the minimap highlight.",
            section = highlightLocalPlayerSection
    )
    default MinimapAnimation minimapAnimation()
    {
        return MinimapAnimation.Static;
    }

    // Default color = Cyan
    @ConfigItem(
            keyName = "localPlayerColor",
            name = "Local Player Color",
            description = "Select the color for all local player highlights.",
            section = highlightLocalPlayerSection
    )
    default Color localPlayerColor()
    {
        return Color.CYAN;
    }


    // ------------------------------------------------------
    // (4) Friends
    // ------------------------------------------------------
    @ConfigSection(
            name = "Highlight Friends",
            description = "Settings related to highlighting friends.",
            position = 3,
            closedByDefault = true
    )
    String highlightFriendsSection = "highlightFriends";

    @ConfigItem(
            keyName = "enableFriendsHighlight",
            name = "Enable Friends Highlight",
            description = "Enable or disable all highlighting for friends.",
            section = highlightFriendsSection
    )
    default boolean enableFriendsHighlight()
    {
        return false;
    }

    // Default color = Green
    @ConfigItem(
            keyName = "friendsHighlightColor",
            name = "Friends Highlight Color",
            description = "Select the highlight color for friends.",
            section = highlightFriendsSection
    )
    default Color friendsHighlightColor()
    {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "friendsNameLocation",
            name = "Friends Name and Level",
            description = "Display name and combat level of friends.",
            section = highlightFriendsSection
    )
    default PlayerNameLocation friendsNameLocation()
    {
        return PlayerNameLocation.ABOVE_HEAD;
    }

    @ConfigItem(
            keyName = "highlightFriendsTile",
            name = "Highlight Tile",
            description = "Highlight the tile of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsTile()
    {
        return true;
    }

    @ConfigItem(
            keyName = "highlightFriendsOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsOutline()
    {
        return true;
    }

    @ConfigItem(
            keyName = "highlightFriendsHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightFriendsMinimap",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of friends.",
            section = highlightFriendsSection
    )
    default boolean highlightFriendsMinimap()
    {
        return true;
    }

    @ConfigItem(
            keyName = "friendsMinimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for friends' minimap highlights.",
            section = highlightFriendsSection
    )
    default MinimapAnimation friendsMinimapAnimation()
    {
        return MinimapAnimation.Pulse;
    }


    // ------------------------------------------------------
    // (5) Ignore
    // ------------------------------------------------------
    @ConfigSection(
            name = "Highlight Ignore",
            description = "Settings related to highlighting players on the Ignore list.",
            position = 4,
            closedByDefault = true
    )
    String highlightIgnoreSection = "highlightIgnore";

    @ConfigItem(
            keyName = "enableIgnoreHighlight",
            name = "Enable Ignore Highlight",
            description = "Enable or disable all highlighting for players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean enableIgnoreHighlight()
    {
        return false;
    }

    // Default color = Red
    @ConfigItem(
            keyName = "ignoreHighlightColor",
            name = "Ignore Highlight Color",
            description = "Select the highlight color for players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default Color ignoreHighlightColor()
    {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "highlightIgnoreTile",
            name = "Highlight Tile",
            description = "Highlight the tile of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreTile()
    {
        return true;
    }

    @ConfigItem(
            keyName = "highlightIgnoreOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreOutline()
    {
        return true;
    }

    @ConfigItem(
            keyName = "highlightIgnoreHull",
            name = "Highlight Hull",
            description = "Highlight the hull (convex hull) of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightIgnoreMinimap",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of players on the Ignore list.",
            section = highlightIgnoreSection
    )
    default boolean highlightIgnoreMinimap()
    {
        return true;
    }

    @ConfigItem(
            keyName = "ignoreMinimapAnimation",
            name = "Minimap Animation",
            description = "Select the animation type for Ignore list players' minimap highlights.",
            section = highlightIgnoreSection
    )
    default MinimapAnimation ignoreMinimapAnimation()
    {
        return MinimapAnimation.Blink;
    }


    // ------------------------------------------------------
    // (6) Chat Channel
    // ------------------------------------------------------
    @ConfigSection(
            name = "Highlight Chat Channel",
            description = "Settings for highlighting clan/chat-channel members.",
            position = 5,
            closedByDefault = true
    )
    String highlightChatChannelSection = "highlightChatChannel";

    @ConfigItem(
            keyName = "enableChatChannelHighlight",
            name = "Enable Chat Channel Highlight",
            description = "Highlight players in the same clan/chat channel.",
            section = highlightChatChannelSection
    )
    default boolean enableChatChannelHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelTile",
            name = "Highlight Tile",
            description = "Highlight the tile of chat channel members.",
            section = highlightChatChannelSection
    )
    default boolean highlightChatChannelTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelOutline",
            name = "Highlight Outline",
            description = "Highlight the outline of chat channel members.",
            section = highlightChatChannelSection
    )
    default boolean highlightChatChannelOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelHull",
            name = "Highlight Hull",
            description = "Highlight the hull of chat channel members.",
            section = highlightChatChannelSection
    )
    default boolean highlightChatChannelHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelMinimap",
            name = "Highlight Minimap",
            description = "Highlight the minimap dot of chat channel members.",
            section = highlightChatChannelSection
    )
    default boolean highlightChatChannelMinimap()
    {
        return false;
    }

    // Default color = Yellow
    @ConfigItem(
            keyName = "chatChannelColor",
            name = "Chat Channel Color",
            description = "Select the highlight color for chat channel members.",
            section = highlightChatChannelSection
    )
    default Color chatChannelColor()
    {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "chatChannelMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for chat channel minimap highlight.",
            section = highlightChatChannelSection
    )
    default MinimapAnimation chatChannelMinimapAnimation()
    {
        return MinimapAnimation.Static;
    }

    @ConfigItem(
            keyName = "chatChannelNameLocation",
            name = "Name and Level",
            description = "Display name and combat level location for chat channel members.",
            section = highlightChatChannelSection
    )
    default PlayerNameLocation chatChannelNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }


    // ------------------------------------------------------
    // (7) Tag Player
    // ------------------------------------------------------
    @ConfigSection(
            name = "Highlight Tag Players",
            description = "Settings for tagging specific players by name.",
            position = 6,
            closedByDefault = true
    )
    String highlightTagPlayersSection = "highlightTagPlayers";

    @ConfigItem(
            keyName = "enableTagPlayerHighlight",
            name = "Enable Tag Player Highlight",
            description = "Enable highlight for players manually tagged.",
            section = highlightTagPlayersSection
    )
    default boolean enableTagPlayerHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "enableRightClickTagPlayer",
            name = "Right Click Tag Player",
            description = "Adds a 'Tag' option when right-clicking a player.",
            section = highlightTagPlayersSection
    )
    default boolean enableRightClickTagPlayer()
    {
        return false;
    }

    @ConfigItem(
            keyName = "taggedPlayersList",
            name = "Tagged Player List",
            description = "Multi-line list of players to highlight (one per line).",
            section = highlightTagPlayersSection
    )
    default String taggedPlayersList()
    {
        // For testing, can default to empty or some sample
        return "";
    }

    @ConfigItem(
            keyName = "highlightTileTag",
            name = "Highlight Tile (Tag)",
            description = "Highlight the tile of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightTileTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutlineTag",
            name = "Highlight Outline (Tag)",
            description = "Highlight the outline of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightOutlineTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHullTag",
            name = "Highlight Hull (Tag)",
            description = "Highlight the hull of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightHullTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimapTag",
            name = "Minimap (Tag)",
            description = "Highlight the minimap dot of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightMinimapTag()
    {
        return false;
    }

    // Farge på tag-player highlight
    @ConfigItem(
            keyName = "tagHighlightColor",
            name = "Tag Highlight Color",
            description = "Color for tagged players highlight.",
            section = highlightTagPlayersSection
    )
    default Color tagHighlightColor()
    {
        return Color.ORANGE; // <-- ORANGE
    }

    @ConfigItem(
            keyName = "tagMinimapAnimation",
            name = "Minimap Animation (Tag)",
            description = "Animation type for tagged players' minimap highlight.",
            section = highlightTagPlayersSection
    )
    default MinimapAnimation tagMinimapAnimation()
    {
        return MinimapAnimation.Static;
    }

    @ConfigItem(
            keyName = "playerNameLocationTag",
            name = "Name and Level (Tag)",
            description = "Display name and combat level for tagged players.",
            section = highlightTagPlayersSection
    )
    default PlayerNameLocation playerNameLocationTag()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------
    // Enums
    // ------------------------------------------------------
    enum MinimapAnimation
    {
        Static,
        Pulse,
        Blink,
        Sonar
    }

    enum PlayerNameLocation
    {
        DISABLED,
        ABOVE_HEAD,
        CENTER_OF_MODEL,
        RIGHT_OF_MODEL
    }
}
