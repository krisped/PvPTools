package com.krisped;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config
{
    // ------------------------------------------------------------------------
    // A) Global Settings Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Settings Highlight",
            description = "Global highlight settings (thickness, minimap size/speed, fonts, styles, label color mode).",
            position = 0,
            closedByDefault = true
    )
    String highlightSettingsSection = "highlightSettingsSection";

    // -- Sliders for thickness + minimap
    @Range(min = 1, max = 255)
    @ConfigItem(
            keyName = "tileThickness",
            name = "Tile Thickness",
            description = "Line thickness (1..255) for tile highlights.",
            section = highlightSettingsSection
    )
    default int tileThickness()
    {
        return 2;
    }

    @Range(min = 1, max = 255)
    @ConfigItem(
            keyName = "outlineThickness",
            name = "Outline Thickness",
            description = "Outline thickness (1..255) for model highlight.",
            section = highlightSettingsSection
    )
    default int outlineThickness()
    {
        return 2;
    }

    @Range(min = 1, max = 255)
    @ConfigItem(
            keyName = "hullThickness",
            name = "Hull Thickness",
            description = "Stroke thickness (1..255) around hull highlight. (No fill)",
            section = highlightSettingsSection
    )
    default int hullThickness()
    {
        return 2;
    }

    @Range(min = 1, max = 255)
    @ConfigItem(
            keyName = "minimapCircleSize",
            name = "Minimap Circle Size",
            description = "Base size (1..255) of the minimap highlight circle.",
            section = highlightSettingsSection
    )
    default int minimapCircleSize()
    {
        return 6;
    }

    @Range(min = 1, max = 15)
    @ConfigItem(
            keyName = "minimapAnimSpeed",
            name = "Minimap Anim Speed",
            description = "Animation speed factor (1=fast, 15=slow).",
            section = highlightSettingsSection
    )
    default int minimapAnimSpeed()
    {
        return 5;
    }

    // -- Name font & style
    @ConfigItem(
            keyName = "nameFont",
            name = "Name Font",
            description = "Font used for player names (10 choices).",
            section = highlightSettingsSection
    )
    default NameFont nameFont()
    {
        return NameFont.ARIAL;
    }

    @ConfigItem(
            keyName = "nameBold",
            name = "Name Bold",
            description = "Use bold style for names?",
            section = highlightSettingsSection
    )
    default boolean nameBold()
    {
        return false;
    }

    @ConfigItem(
            keyName = "nameItalic",
            name = "Name Italic",
            description = "Use italic style for names?",
            section = highlightSettingsSection
    )
    default boolean nameItalic()
    {
        return false;
    }

    @ConfigItem(
            keyName = "nameUnderline",
            name = "Name Underline",
            description = "Underline the player names?",
            section = highlightSettingsSection
    )
    default boolean nameUnderline()
    {
        return false;
    }

    // -- Label font & style
    @ConfigItem(
            keyName = "labelFont",
            name = "Label Font",
            description = "Font used for category labels (Local Player, Friend, etc.).",
            section = highlightSettingsSection
    )
    default NameFont labelFont()
    {
        return NameFont.ARIAL;
    }

    @ConfigItem(
            keyName = "labelBold",
            name = "Label Bold",
            description = "Use bold style for labels?",
            section = highlightSettingsSection
    )
    default boolean labelBold()
    {
        return false;
    }

    @ConfigItem(
            keyName = "labelItalic",
            name = "Label Italic",
            description = "Use italic style for labels?",
            section = highlightSettingsSection
    )
    default boolean labelItalic()
    {
        return false;
    }

    @ConfigItem(
            keyName = "labelUnderline",
            name = "Label Underline",
            description = "Underline the category labels?",
            section = highlightSettingsSection
    )
    default boolean labelUnderline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "labelColorMode",
            name = "Label Color Mode",
            description = "Use highlight color or White for category label?",
            section = highlightSettingsSection
    )
    default LabelColorMode labelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    // ------------------------------------------------------------------------
    // B) Local Player Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Local Player Highlight",
            description = "Settings for local player highlight.",
            position = 1,
            closedByDefault = true
    )
    String localPlayerHighlightSection = "localPlayerHighlightSection";

    @ConfigItem(
            keyName = "enableLocalPlayerHighlight",
            name = "Enable Local Player Highlight",
            description = "Highlight the local player?",
            position = 1,
            section = localPlayerHighlightSection
    )
    default boolean enableLocalPlayerHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "localPlayerNameLocation",
            name = "Name Location",
            description = "Where to display player's own name.",
            position = 2,
            section = localPlayerHighlightSection
    )
    default PlayerNameLocation localPlayerNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "localPlayerOutline",
            name = "Outline",
            description = "Highlight local player's outline?",
            position = 3,
            section = localPlayerHighlightSection
    )
    default boolean localPlayerOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "localPlayerHull",
            name = "Hull",
            description = "Highlight local player's hull (ring)?",
            position = 4,
            section = localPlayerHighlightSection
    )
    default boolean localPlayerHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "localPlayerTile",
            name = "Tile",
            description = "Highlight local player's tile?",
            position = 5,
            section = localPlayerHighlightSection
    )
    default boolean localPlayerTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "localPlayerMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for local player's minimap highlight. (None=off)",
            position = 6,
            section = localPlayerHighlightSection
    )
    default MinimapAnimation localPlayerMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName = "localPlayerColor",
            name = "Color",
            description = "Color for local player highlight.",
            position = 7,
            section = localPlayerHighlightSection
    )
    default Color localPlayerColor()
    {
        return Color.CYAN;
    }

    @ConfigItem(
            keyName = "localPlayerLabelLocation",
            name = "Category Label",
            description = "Where to display 'Local Player' label (if any).",
            position = 8,
            section = localPlayerHighlightSection
    )
    default PlayerNameLocation localPlayerLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    // C) Attackable Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Attackable Highlight",
            description = "Settings for players you can attack.",
            position = 2,
            closedByDefault = true
    )
    String attackableHighlightSection = "attackableHighlightSection";

    @ConfigItem(
            keyName = "enableAttackablePlayersHighlight",
            name = "Enable Attackable Highlight",
            description = "Highlight players you can attack?",
            position = 1,
            section = attackableHighlightSection
    )
    default boolean enableAttackablePlayersHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "attackableNameLocation",
            name = "Name Location",
            description = "Where to display name/level for attackable players.",
            position = 2,
            section = attackableHighlightSection
    )
    default PlayerNameLocation attackableNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "attackableOutline",
            name = "Outline",
            description = "Highlight outline for attackable players?",
            position = 3,
            section = attackableHighlightSection
    )
    default boolean attackableOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "attackableHull",
            name = "Hull",
            description = "Highlight hull (ring) for attackable players?",
            position = 4,
            section = attackableHighlightSection
    )
    default boolean attackableHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "attackableTile",
            name = "Tile",
            description = "Highlight tile for attackable players?",
            position = 5,
            section = attackableHighlightSection
    )
    default boolean attackableTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "attackableMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation on minimap. (None=off)",
            position = 6,
            section = attackableHighlightSection
    )
    default MinimapAnimation attackableMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName = "attackableColor",
            name = "Color",
            description = "Color for attackable players highlight",
            position = 7,
            section = attackableHighlightSection
    )
    default Color attackableColor()
    {
        return Color.BLUE;
    }

    @ConfigItem(
            keyName = "attackableLabelLocation",
            name = "Category Label",
            description = "Where to display 'Attackable' label.",
            position = 8,
            section = attackableHighlightSection
    )
    default PlayerNameLocation attackableLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    // D) Friends Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Friends Highlight",
            description = "Highlight your friends",
            position = 3,
            closedByDefault = true
    )
    String friendsHighlightSection = "friendsHighlightSection";

    @ConfigItem(
            keyName = "enableFriendsHighlight",
            name = "Enable Friends Highlight",
            description = "Highlight your friends?",
            position = 1,
            section = friendsHighlightSection
    )
    default boolean enableFriendsHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "friendsNameLocation",
            name = "Name Location",
            description = "Where to show name/level for friends",
            position = 2,
            section = friendsHighlightSection
    )
    default PlayerNameLocation friendsNameLocation()
    {
        return PlayerNameLocation.ABOVE_HEAD;
    }

    @ConfigItem(
            keyName = "friendsOutline",
            name = "Outline",
            description = "Highlight outline for friends?",
            position = 3,
            section = friendsHighlightSection
    )
    default boolean friendsOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "friendsHull",
            name = "Hull",
            description = "Highlight hull (ring) for friends?",
            position = 4,
            section = friendsHighlightSection
    )
    default boolean friendsHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "friendsTile",
            name = "Tile",
            description = "Highlight tile for friends?",
            position = 5,
            section = friendsHighlightSection
    )
    default boolean friendsTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "friendsMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for friends' minimap highlight. (None=off)",
            position = 6,
            section = friendsHighlightSection
    )
    default MinimapAnimation friendsMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName = "friendsHighlightColor",
            name = "Color",
            description = "Color for highlighting friends",
            position = 7,
            section = friendsHighlightSection
    )
    default Color friendsHighlightColor()
    {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "friendsLabelLocation",
            name = "Category Label",
            description = "Where to display 'Friend' label.",
            position = 8,
            section = friendsHighlightSection
    )
    default PlayerNameLocation friendsLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    // E) Ignore Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Ignore Highlight",
            description = "Highlight players on your ignore list",
            position = 4,
            closedByDefault = true
    )
    String ignoreHighlightSection = "ignoreHighlightSection";

    @ConfigItem(
            keyName = "enableIgnoreHighlight",
            name = "Enable Ignore Highlight",
            description = "Highlight players on your ignore list?",
            position = 1,
            section = ignoreHighlightSection
    )
    default boolean enableIgnoreHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreNameLocation",
            name = "Name Location",
            description = "Where to show name/level for ignored players",
            position = 2,
            section = ignoreHighlightSection
    )
    default PlayerNameLocation ignoreNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "ignoreOutline",
            name = "Outline",
            description = "Highlight outline for ignored players?",
            position = 3,
            section = ignoreHighlightSection
    )
    default boolean ignoreOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreHull",
            name = "Hull",
            description = "Highlight hull (ring) for ignored players?",
            position = 4,
            section = ignoreHighlightSection
    )
    default boolean ignoreHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreTile",
            name = "Tile",
            description = "Highlight tile for ignored players?",
            position = 5,
            section = ignoreHighlightSection
    )
    default boolean ignoreTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "ignoreMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for ignore minimap highlight. (None=off)",
            position = 6,
            section = ignoreHighlightSection
    )
    default MinimapAnimation ignoreMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName = "ignoreHighlightColor",
            name = "Color",
            description = "Color used for ignore highlight",
            position = 7,
            section = ignoreHighlightSection
    )
    default Color ignoreHighlightColor()
    {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "ignoreLabelLocation",
            name = "Category Label",
            description = "Where to display 'Ignored' label.",
            position = 8,
            section = ignoreHighlightSection
    )
    default PlayerNameLocation ignoreLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    // F) Chat Channel Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Chat Channel Highlight",
            description = "Highlight players in your clan/friends chat channel",
            position = 5,
            closedByDefault = true
    )
    String chatChannelHighlightSection = "chatChannelHighlightSection";

    @ConfigItem(
            keyName = "enableChatChannelHighlight",
            name = "Enable ChatChannel Highlight",
            description = "Highlight players in your clan/chat channel?",
            position = 1,
            section = chatChannelHighlightSection
    )
    default boolean enableChatChannelHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chatChannelNameLocation",
            name = "Name Location",
            description = "Where to draw name/level for chat channel players",
            position = 2,
            section = chatChannelHighlightSection
    )
    default PlayerNameLocation chatChannelNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "chatChannelOutline",
            name = "Outline",
            description = "Outline highlight for chat channel players?",
            position = 3,
            section = chatChannelHighlightSection
    )
    default boolean chatChannelOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chatChannelHull",
            name = "Hull",
            description = "Hull (ring) highlight for chat channel players?",
            position = 4,
            section = chatChannelHighlightSection
    )
    default boolean chatChannelHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chatChannelTile",
            name = "Tile",
            description = "Tile highlight for chat channel players?",
            position = 5,
            section = chatChannelHighlightSection
    )
    default boolean chatChannelTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chatChannelMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for chat channel minimap highlight (None=off).",
            position = 6,
            section = chatChannelHighlightSection
    )
    default MinimapAnimation chatChannelMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName = "chatChannelColor",
            name = "Color",
            description = "Color for chat channel highlight",
            position = 7,
            section = chatChannelHighlightSection
    )
    default Color chatChannelColor()
    {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "chatChannelLabelLocation",
            name = "Category Label",
            description = "Where to display 'Chat Channel' label.",
            position = 8,
            section = chatChannelHighlightSection
    )
    default PlayerNameLocation chatChannelLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    // G) Tag Highlight
    // ------------------------------------------------------------------------
    @ConfigSection(
            name = "Tag Players Highlight",
            description = "Settings for tagging specific players by name.",
            position = 6,
            closedByDefault = true
    )
    String highlightTagPlayersSection = "highlightTagPlayers";

    @ConfigItem(
            keyName = "enableTagPlayerHighlight",
            name = "Enable Tag Highlight",
            description = "Enable highlight for players manually tagged in config.",
            position = 1,
            section = highlightTagPlayersSection
    )
    default boolean enableTagPlayerHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName = "taggedPlayersList",
            name = "Tagged Player List",
            description = "Multi-line list of players (one per line).",
            position = 2,
            section = highlightTagPlayersSection
    )
    default String taggedPlayersList()
    {
        return "";
    }

    @ConfigItem(
            keyName = "tagMenuOption",
            name = "Tag Menu Option",
            description = "Off = no 'Tag' in menu, Right Click = always show, Shift+Right Click = only show if SHIFT is held.",
            position = 3,
            section = highlightTagPlayersSection
    )
    default TagMenuOption tagMenuOption()
    {
        return TagMenuOption.OFF;
    }

    @ConfigItem(
            keyName = "tagNameLocation",
            name = "Name Location",
            description = "Where to display name/level for tagged players.",
            position = 4,
            section = highlightTagPlayersSection
    )
    default PlayerNameLocation tagNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName = "tagOutline",
            name = "Outline",
            description = "Highlight the outline of tagged players?",
            position = 5,
            section = highlightTagPlayersSection
    )
    default boolean tagOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName = "tagHull",
            name = "Hull",
            description = "Highlight the hull (ring) of tagged players?",
            position = 6,
            section = highlightTagPlayersSection
    )
    default boolean tagHull()
    {
        return false;
    }

    @ConfigItem(
            keyName = "tagTile",
            name = "Tile",
            description = "Highlight the tile of tagged players?",
            position = 7,
            section = highlightTagPlayersSection
    )
    default boolean tagTile()
    {
        return false;
    }

    @ConfigItem(
            keyName = "tagMinimapAnimation",
            name = "Minimap Animation",
            description = "Animation for tagged players' minimap highlight. (None=off)",
            position = 8,
            section = highlightTagPlayersSection
    )
    default MinimapAnimation tagMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName = "tagHighlightColor",
            name = "Color",
            description = "Color used for tagged players highlight.",
            position = 9,
            section = highlightTagPlayersSection
    )
    default Color tagHighlightColor()
    {
        return Color.ORANGE;
    }

    @ConfigItem(
            keyName = "tagLabelLocation",
            name = "Category Label",
            description = "Where to display 'Tagged' label.",
            position = 10,
            section = highlightTagPlayersSection
    )
    default PlayerNameLocation tagLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    // ------------------------------------------------------------------------
    // ENUMS
    // ------------------------------------------------------------------------
    enum MinimapAnimation
    {
        NONE,
        STATIC,
        PULSE,
        BLINK,
        SONAR,
        WAVE
    }

    enum PlayerNameLocation
    {
        DISABLED,
        ABOVE_HEAD,
        CENTER_OF_MODEL,
        UNDER_MODEL
    }

    /**
     * Ti forskjellige fonter som du ønsket
     */
    enum NameFont
    {
        ARIAL,
        CALIBRI,
        VERDANA,
        TAHOMA,
        CONSOLAS,
        COMIC_SANS,
        TIMES_NEW_ROMAN,
        DIALOG,
        MONOSPACED,
        SERIF
    }

    /**
     * LabelColorMode: enten hvit eller samme som highlight
     */
    enum LabelColorMode
    {
        WHITE,
        CATEGORY_COLOR
    }

    enum TagMenuOption
    {
        OFF,
        RIGHT_CLICK,
        SHIFT_RIGHT_CLICK
    }
}
