package com.krisped;

import net.runelite.client.config.*;
import java.awt.*;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config
{
    // ------------------------------------------------------------------------
    // 1) Enable Sidepanel
    // ------------------------------------------------------------------------
    @ConfigItem(
            keyName = "enableSidepanel",
            name = "Enable Sidepanel",
            description = "Show the PvP Tools panel in the sidebar?"
    )
    default boolean enableSidepanel()
    {
        return true;
    }

    // ------------------------------------------------------------------------
    // 2) Settings Highlight (global thickness, minimap circle, anim speed 1..10)
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Settings Highlight",
            description = "Global highlight settings (tile/outline/hull thickness, minimap circle, anim speed factor).",
            position = 0,
            closedByDefault = true
    )
    String highlightSettingsSection = "highlightSettingsSection";

    @Range(
            min = 1,
            max = 10
    )
    @ConfigItem(
            keyName = "minimapAnimSpeed",
            name = "Minimap Anim Speed",
            description = "Animation speed factor (1=fastest, 10=slowest).",
            section = highlightSettingsSection
    )
    default int minimapAnimSpeed()
    {
        return 5; // mid-range
    }

    @ConfigItem(
            keyName = "tileThickness",
            name = "Tile Thickness",
            description = "Line thickness for tile highlights.",
            section = highlightSettingsSection
    )
    default int tileThickness()
    {
        return 1;
    }

    @ConfigItem(
            keyName = "outlineThickness",
            name = "Outline Thickness",
            description = "Outline thickness for model highlight.",
            section = highlightSettingsSection
    )
    default int outlineThickness()
    {
        return 2;
    }

    @ConfigItem(
            keyName = "hullThickness",
            name = "Hull Thickness",
            description = "Stroke thickness around hull highlight.",
            section = highlightSettingsSection
    )
    default int hullThickness()
    {
        return 1;
    }

    @ConfigItem(
            keyName = "minimapCircleSize",
            name = "Minimap Circle Size",
            description = "Base size of the minimap highlight circle.",
            section = highlightSettingsSection
    )
    default int minimapCircleSize()
    {
        return 6;
    }

    // ------------------------------------------------------------------------
    // 3) Local Player
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Highlight Local Player",
            description = "Settings for local player highlight.",
            position = 1,
            closedByDefault = true
    )
    String localPlayerSection = "localPlayerSection";

    @ConfigItem(
            keyName = "enableLocalPlayer",
            name = "Enable Local Player",
            description = "Enable highlight on local player?",
            section = localPlayerSection
    )
    default boolean enableLocalPlayer()
    {
        return false;
    }

    @ConfigItem(
            keyName = "localPlayerColor",
            name = "Local Player Color",
            description = "Color for local player's highlight",
            section = localPlayerSection
    )
    default Color localPlayerColor()
    {
        return Color.CYAN;
    }

    @ConfigItem(
            keyName = "playerNameLocationLocal",
            name = "Name & Level",
            description = "Show local player's name/level location",
            section = localPlayerSection
    )
    default PlayerNameLocation playerNameLocationLocal()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightTileLocal",
            name = "Highlight Tile",
            description = "Highlight local player's tile?",
            section = localPlayerSection
    )
    default boolean highlightTileLocal()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutlineLocal",
            name = "Highlight Outline",
            description = "Highlight local player's outline?",
            section = localPlayerSection
    )
    default boolean highlightOutlineLocal()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHullLocal",
            name = "Highlight Hull",
            description = "Highlight local player's hull?",
            section = localPlayerSection
    )
    default boolean highlightHullLocal()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimapLocal",
            name = "Highlight Minimap",
            description = "Highlight local player's minimap dot?",
            section = localPlayerSection
    )
    default boolean highlightMinimapLocal()
    {
        return false;
    }

    @ConfigItem(
            keyName = "minimapAnimationLocal",
            name = "Minimap Animation",
            description = "Animation for local player's minimap highlight",
            section = localPlayerSection
    )
    default MinimapAnimation minimapAnimationLocal()
    {
        return MinimapAnimation.Static;
    }

    // ------------------------------------------------------------------------
    // 4) Attackable Players
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Highlight Attackable Players",
            description = "Settings for players you can attack.",
            position = 2,
            closedByDefault = true
    )
    String attackableSection = "attackableSection";

    @ConfigItem(
            keyName = "enableAttackablePlayers",
            name = "Enable Attackable Players",
            description = "Highlight players you can attack?",
            section = attackableSection
    )
    default boolean enableAttackablePlayers()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightColorAttackable",
            name = "Attackable Color",
            description = "Color for attackable players highlight",
            section = attackableSection
    )
    default Color highlightColorAttackable()
    {
        return Color.BLUE;
    }

    @ConfigItem(
            keyName = "playerNameLocationAttackable",
            name = "Name & Level",
            description = "Show name/level for attackable players",
            section = attackableSection
    )
    default PlayerNameLocation playerNameLocationAttackable()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightTileAttackable",
            name = "Highlight Tile",
            description = "Highlight tile for attackable players?",
            section = attackableSection
    )
    default boolean highlightTileAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutlineAttackable",
            name = "Outline",
            description = "Highlight outline for attackable players?",
            section = attackableSection
    )
    default boolean highlightOutlineAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHullAttackable",
            name = "Hull",
            description = "Highlight hull for attackable players?",
            section = attackableSection
    )
    default boolean highlightHullAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimapAttackable",
            name = "Minimap",
            description = "Highlight minimap for attackable players?",
            section = attackableSection
    )
    default boolean highlightMinimapAttackable()
    {
        return false;
    }

    @ConfigItem(
            keyName = "minimapAnimationAttackable",
            name = "Minimap Animation",
            description = "Animation for attackable players' minimap highlight",
            section = attackableSection
    )
    default MinimapAnimation minimapAnimationAttackable()
    {
        return MinimapAnimation.Static;
    }

    // ------------------------------------------------------------------------
    // 5) Friends
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Highlight Friends",
            description = "Settings for friend highlight",
            position = 3,
            closedByDefault = true
    )
    String friendsSection = "friendsSection";

    @ConfigItem(
            keyName = "enableFriendsHighlight",
            name = "Enable Friends Highlight",
            description = "Highlight your friends?",
            section = friendsSection
    )
    default boolean enableFriendsHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "friendsHighlightColor",
            name = "Friends Color",
            description = "Color for highlighting friends",
            section = friendsSection
    )
    default Color friendsHighlightColor()
    {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "friendsNameLocation",
            name = "Name & Level",
            description = "Where to show name/level for friends",
            section = friendsSection
    )
    default PlayerNameLocation friendsNameLocation()
    {
        return PlayerNameLocation.ABOVE_HEAD;
    }

    @ConfigItem(
            keyName = "highlightFriendsTile",
            name = "Tile",
            description = "Highlight tile for friends?",
            section = friendsSection
    )
    default boolean highlightFriendsTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightFriendsOutline",
            name = "Outline",
            description = "Highlight outline for friends?",
            section = friendsSection
    )
    default boolean highlightFriendsOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightFriendsHull",
            name = "Hull",
            description = "Highlight hull for friends?",
            section = friendsSection
    )
    default boolean highlightFriendsHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightFriendsMinimap",
            name = "Minimap",
            description = "Highlight minimap dot for friends?",
            section = friendsSection
    )
    default boolean highlightFriendsMinimap()
    {
        return false;
    }

    @ConfigItem(
            keyName = "friendsMinimapAnimation",
            name = "Minimap Animation",
            description = "Friends minimap highlight animation",
            section = friendsSection
    )
    default MinimapAnimation friendsMinimapAnimation()
    {
        return MinimapAnimation.Static;
    }

    // ------------------------------------------------------------------------
    // 6) Ignore
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Highlight Ignore",
            description = "Ignore list highlight",
            position = 4,
            closedByDefault = true
    )
    String ignoreSection = "ignoreSection";

    @ConfigItem(
            keyName = "enableIgnoreHighlight",
            name = "Enable Ignore Highlight",
            description = "Highlight players on your ignore list?",
            section = ignoreSection
    )
    default boolean enableIgnoreHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreHighlightColor",
            name = "Ignore Color",
            description = "Color used for ignore highlight",
            section = ignoreSection
    )
    default Color ignoreHighlightColor()
    {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "highlightIgnoreTile",
            name = "Tile",
            description = "Highlight tile for ignored players?",
            section = ignoreSection
    )
    default boolean highlightIgnoreTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightIgnoreOutline",
            name = "Outline",
            description = "Highlight outline for ignored players?",
            section = ignoreSection
    )
    default boolean highlightIgnoreOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightIgnoreHull",
            name = "Hull",
            description = "Highlight hull for ignored players?",
            section = ignoreSection
    )
    default boolean highlightIgnoreHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightIgnoreMinimap",
            name = "Minimap",
            description = "Highlight minimap dot for ignored players?",
            section = ignoreSection
    )
    default boolean highlightIgnoreMinimap()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for ignore minimap highlight",
            section = ignoreSection
    )
    default MinimapAnimation ignoreMinimapAnimation()
    {
        return MinimapAnimation.Blink;
    }

    // ------------------------------------------------------------------------
    // 7) Chat Channel
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Highlight Chat Channel",
            description = "Highlight clan/chat channel members",
            position = 5,
            closedByDefault = true
    )
    String chatChannelSection = "chatChannelSection";

    @ConfigItem(
            keyName = "enableChatChannelHighlight",
            name = "Enable Chat Channel Highlight",
            description = "Highlight players in your clan/chat channel?",
            section = chatChannelSection
    )
    default boolean enableChatChannelHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chatChannelColor",
            name = "Chat Channel Color",
            description = "Color for chat channel highlight",
            section = chatChannelSection
    )
    default Color chatChannelColor()
    {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "chatChannelNameLocation",
            name = "Name & Level",
            description = "Where to draw name/level for chat channel players",
            section = chatChannelSection
    )
    default PlayerNameLocation chatChannelNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "highlightChatChannelTile",
            name = "Tile",
            description = "Tile highlight for chat channel players?",
            section = chatChannelSection
    )
    default boolean highlightChatChannelTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelOutline",
            name = "Outline",
            description = "Outline highlight for chat channel players?",
            section = chatChannelSection
    )
    default boolean highlightChatChannelOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelHull",
            name = "Hull",
            description = "Hull highlight for chat channel players?",
            section = chatChannelSection
    )
    default boolean highlightChatChannelHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightChatChannelMinimap",
            name = "Minimap",
            description = "Minimap highlight for chat channel players?",
            section = chatChannelSection
    )
    default boolean highlightChatChannelMinimap()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chatChannelMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for chat channel minimap highlight",
            section = chatChannelSection
    )
    default MinimapAnimation chatChannelMinimapAnimation()
    {
        return MinimapAnimation.Static;
    }

    // ------------------------------------------------------------------------
    // 8) Tag Players
    // ------------------------------------------------------------------------
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
            description = "Enable highlight for players manually tagged in config.",
            section = highlightTagPlayersSection
    )
    default boolean enableTagPlayerHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "enableRightClickTagPlayer",
            name = "Right Click Tag Player",
            description = "Adds a 'Tag' option for players in the menu.",
            section = highlightTagPlayersSection
    )
    default boolean enableRightClickTagPlayer()
    {
        return false;
    }

    @ConfigItem(
            keyName = "taggedPlayersList",
            name = "Tagged Player List",
            description = "Multi-line list of players (one per line) for tag highlight.",
            section = highlightTagPlayersSection
    )
    default String taggedPlayersList()
    {
        return "";
    }

    @ConfigItem(
            keyName = "highlightTileTag",
            name = "Highlight Tile",
            description = "Highlight the tile of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightTileTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightOutlineTag",
            name = "Highlight Outline",
            description = "Highlight the outline of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightOutlineTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightHullTag",
            name = "Highlight Hull",
            description = "Highlight the hull of tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightHullTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "highlightMinimapTag",
            name = "Highlight Minimap",
            description = "Highlight minimap dot for tagged players.",
            section = highlightTagPlayersSection
    )
    default boolean highlightMinimapTag()
    {
        return false;
    }

    @ConfigItem(
            keyName = "tagHighlightColor",
            name = "Tag Highlight Color",
            description = "Color used for tagged players highlight.",
            section = highlightTagPlayersSection
    )
    default Color tagHighlightColor()
    {
        return Color.ORANGE;
    }

    @ConfigItem(
            keyName = "tagMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for tagged players' minimap highlight",
            section = highlightTagPlayersSection
    )
    default MinimapAnimation tagMinimapAnimation()
    {
        return MinimapAnimation.Static;
    }

    @ConfigItem(
            keyName = "playerNameLocationTag",
            name = "Name and Level",
            description = "Where to display name/level for tagged players.",
            section = highlightTagPlayersSection
    )
    default PlayerNameLocation playerNameLocationTag()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    //  ENUMS
    // ------------------------------------------------------------------------
    enum MinimapAnimation
    {
        Static,
        Pulse,
        Blink,
        Sonar,
        Wave
    }

    enum PlayerNameLocation
    {
        DISABLED,
        ABOVE_HEAD,
        CENTER_OF_MODEL,
        RIGHT_OF_MODEL
    }
}
