package com.krisped;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config
{
    // ----------------------------------------------------
    // ENUMS
    // ----------------------------------------------------
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

    enum LabelColorMode
    {
        WHITE,
        CATEGORY_COLOR
    }

    // *** HER ER ENDRINGEN: Kun OFF, RIGHT_CLICK, SHIFT_RIGHT_CLICK
    enum TagMenuOption
    {
        OFF,
        RIGHT_CLICK,
        SHIFT_RIGHT_CLICK
    }

    // ----------------------------------------------------
    // 1) Global: Thickness & Minimap
    // ----------------------------------------------------
    @ConfigSection(
            name="Thickness & Minimap",
            description="Global thickness + minimap settings",
            position=0,
            closedByDefault=true
    )
    String secThicknessMinimap = "secThicknessMinimap";

    @Range(min=1, max=255)
    @ConfigItem(
            keyName="tileThickness",
            name="Tile Thickness",
            description="Line thickness (1..255) for tile highlights",
            section=secThicknessMinimap
    )
    default int tileThickness()
    {
        return 2;
    }

    @Range(min=1, max=255)
    @ConfigItem(
            keyName="outlineThickness",
            name="Outline Thickness",
            description="Outline thickness (1..255).",
            section=secThicknessMinimap
    )
    default int outlineThickness()
    {
        return 2;
    }

    @Range(min=1, max=255)
    @ConfigItem(
            keyName="hullThickness",
            name="Hull Thickness",
            description="Stroke thickness for hull (no fill).",
            section=secThicknessMinimap
    )
    default int hullThickness()
    {
        return 2;
    }

    @Range(min=1, max=255)
    @ConfigItem(
            keyName="minimapCircleSize",
            name="Minimap Circle Size",
            description="Base size (1..255) of minimap highlight circle.",
            section=secThicknessMinimap
    )
    default int minimapCircleSize()
    {
        return 6;
    }

    @Range(min=1, max=15)
    @ConfigItem(
            keyName="minimapAnimSpeed",
            name="Minimap Anim Speed",
            description="Animation speed factor (1=fast, 15=slow).",
            section=secThicknessMinimap
    )
    default int minimapAnimSpeed()
    {
        return 5;
    }

    // ----------------------------------------------------
    // 2) Name Font & Style + Name Offset
    // ----------------------------------------------------
    @ConfigSection(
            name="Name Font & Style",
            description="Global name font, style, offset",
            position=1,
            closedByDefault=true
    )
    String secNameFont = "secNameFont";

    @ConfigItem(
            keyName="nameFont",
            name="Name Font",
            description="Which font to use for names",
            section=secNameFont
    )
    default NameFont nameFont()
    {
        return NameFont.ARIAL;
    }

    @ConfigItem(
            keyName="nameBold",
            name="Name Bold",
            description="Use bold style for names?",
            section=secNameFont
    )
    default boolean nameBold()
    {
        return false;
    }

    @ConfigItem(
            keyName="nameUnderline",
            name="Name Underline",
            description="Underline the names?",
            section=secNameFont
    )
    default boolean nameUnderline()
    {
        return false;
    }

    @ConfigItem(
            keyName="nameItalic",
            name="Name Italic",
            description="Use italic style for names?",
            section=secNameFont
    )
    default boolean nameItalic()
    {
        return false;
    }

    @Range(min=-100, max=100)
    @ConfigItem(
            keyName="nameOffsetX",
            name="Name Offset X",
            description="Horizontal offset for all player names (-100..100)",
            section=secNameFont
    )
    default int nameOffsetX()
    {
        return 0;
    }

    @Range(min=-100, max=100)
    @ConfigItem(
            keyName="nameOffsetY",
            name="Name Offset Y",
            description="Vertical offset for player names",
            section=secNameFont
    )
    default int nameOffsetY()
    {
        return 0;
    }

    // ----------------------------------------------------
    // 3) Label Font & Style + Label Offset
    // ----------------------------------------------------
    @ConfigSection(
            name="Label Font & Style",
            description="Global label font, style, offset",
            position=2,
            closedByDefault=true
    )
    String secLabelFont = "secLabelFont";

    @ConfigItem(
            keyName="labelFont",
            name="Label Font",
            description="Which font to use for category labels",
            section=secLabelFont
    )
    default NameFont labelFont()
    {
        return NameFont.ARIAL;
    }

    @ConfigItem(
            keyName="labelBold",
            name="Label Bold",
            description="Use bold style for labels?",
            section=secLabelFont
    )
    default boolean labelBold()
    {
        return false;
    }

    @ConfigItem(
            keyName="labelUnderline",
            name="Label Underline",
            description="Underline category labels?",
            section=secLabelFont
    )
    default boolean labelUnderline()
    {
        return false;
    }

    @ConfigItem(
            keyName="labelItalic",
            name="Label Italic",
            description="Use italic style for labels?",
            section=secLabelFont
    )
    default boolean labelItalic()
    {
        return false;
    }

    @Range(min=-100, max=100)
    @ConfigItem(
            keyName="labelOffsetX",
            name="Label Offset X",
            description="Horizontal offset for all category labels",
            section=secLabelFont
    )
    default int labelOffsetX()
    {
        return 0;
    }

    @Range(min=-100, max=100)
    @ConfigItem(
            keyName="labelOffsetY",
            name="Label Offset Y",
            description="Vertical offset for category labels",
            section=secLabelFont
    )
    default int labelOffsetY()
    {
        return 0;
    }

    // ----------------------------------------------------
    // 4) Local Player
    // ----------------------------------------------------
    @ConfigSection(
            name="Local Player Highlight",
            description="Local player settings",
            position=10,
            closedByDefault=true
    )
    String secLocal = "secLocal";

    @ConfigItem(
            keyName="enableLocalPlayerHighlight",
            name="Enable Local",
            description="Highlight the local player?",
            section=secLocal
    )
    default boolean enableLocalPlayerHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName="localPlayerColor",
            name="Color",
            description="Color for local player highlight",
            section=secLocal
    )
    default Color localPlayerColor()
    {
        return Color.CYAN;
    }

    @ConfigItem(
            keyName="localPlayerOutline",
            name="Outline",
            description="Highlight local player's outline?",
            section=secLocal
    )
    default boolean localPlayerOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName="localPlayerHull",
            name="Hull",
            description="Highlight local player's hull ring?",
            section=secLocal
    )
    default boolean localPlayerHull()
    {
        return false;
    }

    @ConfigItem(
            keyName="localPlayerTile",
            name="Tile",
            description="Highlight local player's tile?",
            section=secLocal
    )
    default boolean localPlayerTile()
    {
        return false;
    }

    @ConfigItem(
            keyName="localPlayerMinimapAnimation",
            name="Minimap",
            description="Minimap highlight (None=off)",
            section=secLocal
    )
    default MinimapAnimation localPlayerMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName="localPlayerNameLocation",
            name="Name Location",
            description="Where to display local player's name?",
            section=secLocal
    )
    default PlayerNameLocation localPlayerNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="localPlayerLabelLocation",
            name="Label Location",
            description="Where to display 'Local Player' label?",
            section=secLocal
    )
    default PlayerNameLocation localPlayerLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="localLabelColorMode",
            name="Label Color Mode",
            description="White or highlight color?",
            section=secLocal
    )
    default LabelColorMode localLabelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    // ----------------------------------------------------
    // 5) Attackable
    // ----------------------------------------------------
    @ConfigSection(
            name="Attackable Highlight",
            description="Players you can attack",
            position=11,
            closedByDefault=true
    )
    String secAttackable = "secAttackable";

    @ConfigItem(
            keyName="enableAttackablePlayersHighlight",
            name="Enable Attackable",
            description="Highlight players you can attack?",
            section=secAttackable
    )
    default boolean enableAttackablePlayersHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName="attackableColor",
            name="Color",
            description="Color for attackable highlight",
            section=secAttackable
    )
    default Color attackableColor()
    {
        return Color.BLUE;
    }

    @ConfigItem(
            keyName="attackableOutline",
            name="Outline",
            description="Outline highlight for attackable?",
            section=secAttackable
    )
    default boolean attackableOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName="attackableHull",
            name="Hull",
            description="Highlight hull ring for attackable?",
            section=secAttackable
    )
    default boolean attackableHull()
    {
        return false;
    }

    @ConfigItem(
            keyName="attackableTile",
            name="Tile",
            description="Highlight tile for attackable?",
            section=secAttackable
    )
    default boolean attackableTile()
    {
        return false;
    }

    @ConfigItem(
            keyName="attackableMinimapAnimation",
            name="Minimap",
            description="Minimap highlight for attackable",
            section=secAttackable
    )
    default MinimapAnimation attackableMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName="attackableNameLocation",
            name="Name Location",
            description="Where to display attackable player's name?",
            section=secAttackable
    )
    default PlayerNameLocation attackableNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="attackableLabelLocation",
            name="Label Location",
            description="Where to display 'Attackable' label?",
            section=secAttackable
    )
    default PlayerNameLocation attackableLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="attackableLabelColorMode",
            name="Label Color Mode",
            description="White or highlight color for 'Attackable' label?",
            section=secAttackable
    )
    default LabelColorMode attackableLabelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    // ----------------------------------------------------
    // 6) Friends
    // ----------------------------------------------------
    @ConfigSection(
            name="Friends Highlight",
            description="Highlight your friends",
            position=12,
            closedByDefault=true
    )
    String secFriends = "secFriends";

    @ConfigItem(
            keyName="enableFriendsHighlight",
            name="Enable Friends",
            description="Highlight your friends?",
            section=secFriends
    )
    default boolean enableFriendsHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName="friendsHighlightColor",
            name="Color",
            description="Friends highlight color",
            section=secFriends
    )
    default Color friendsHighlightColor()
    {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName="friendsOutline",
            name="Outline",
            description="Highlight outline for friends?",
            section=secFriends
    )
    default boolean friendsOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName="friendsHull",
            name="Hull",
            description="Highlight hull ring for friends?",
            section=secFriends
    )
    default boolean friendsHull()
    {
        return false;
    }

    @ConfigItem(
            keyName="friendsTile",
            name="Tile",
            description="Highlight tile for friends?",
            section=secFriends
    )
    default boolean friendsTile()
    {
        return false;
    }

    @ConfigItem(
            keyName="friendsMinimapAnimation",
            name="Minimap",
            description="Minimap highlight for friends",
            section=secFriends
    )
    default MinimapAnimation friendsMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName="friendsNameLocation",
            name="Name Location",
            description="Where to display friend name?",
            section=secFriends
    )
    default PlayerNameLocation friendsNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="friendsLabelLocation",
            name="Label Location",
            description="Where to display 'Friend' label?",
            section=secFriends
    )
    default PlayerNameLocation friendsLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="friendsLabelColorMode",
            name="Label Color Mode",
            description="White or highlight color for 'Friend' label?",
            section=secFriends
    )
    default LabelColorMode friendsLabelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    // ----------------------------------------------------
    // 7) Ignore
    // ----------------------------------------------------
    @ConfigSection(
            name="Ignore Highlight",
            description="Highlight players on your ignore list",
            position=13,
            closedByDefault=true
    )
    String secIgnore = "secIgnore";

    @ConfigItem(
            keyName="enableIgnoreHighlight",
            name="Enable Ignore",
            description="Highlight players on ignore list?",
            section=secIgnore
    )
    default boolean enableIgnoreHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName="ignoreHighlightColor",
            name="Color",
            description="Ignore highlight color",
            section=secIgnore
    )
    default Color ignoreHighlightColor()
    {
        return Color.RED;
    }

    @ConfigItem(
            keyName="ignoreOutline",
            name="Outline",
            description="Highlight outline for ignored players?",
            section=secIgnore
    )
    default boolean ignoreOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName="ignoreHull",
            name="Hull",
            description="Highlight hull ring for ignored players?",
            section=secIgnore
    )
    default boolean ignoreHull()
    {
        return false;
    }

    @ConfigItem(
            keyName="ignoreTile",
            name="Tile",
            description="Highlight tile for ignored players?",
            section=secIgnore
    )
    default boolean ignoreTile()
    {
        return false;
    }

    @ConfigItem(
            keyName="ignoreMinimapAnimation",
            name="Minimap",
            description="Minimap highlight for ignored players",
            section=secIgnore
    )
    default MinimapAnimation ignoreMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName="ignoreNameLocation",
            name="Name Location",
            description="Where to display ignored player's name?",
            section=secIgnore
    )
    default PlayerNameLocation ignoreNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="ignoreLabelLocation",
            name="Label Location",
            description="Where to display 'Ignored' label?",
            section=secIgnore
    )
    default PlayerNameLocation ignoreLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="ignoreLabelColorMode",
            name="Label Color Mode",
            description="White or highlight color for 'Ignored' label?",
            section=secIgnore
    )
    default LabelColorMode ignoreLabelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    // ----------------------------------------------------
    // 8) Chat Channel
    // ----------------------------------------------------
    @ConfigSection(
            name="Chat Channel Highlight",
            description="Highlight players in your clan/friends chat channel",
            position=14,
            closedByDefault=true
    )
    String secChat = "secChat";

    @ConfigItem(
            keyName="enableChatChannelHighlight",
            name="Enable ChatChannel",
            description="Highlight players in your chat channel?",
            section=secChat
    )
    default boolean enableChatChannelHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName="chatChannelColor",
            name="Color",
            description="Chat channel highlight color",
            section=secChat
    )
    default Color chatChannelColor()
    {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName="chatChannelOutline",
            name="Outline",
            description="Outline highlight for chat channel players?",
            section=secChat
    )
    default boolean chatChannelOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName="chatChannelHull",
            name="Hull",
            description="Hull ring for chat channel players?",
            section=secChat
    )
    default boolean chatChannelHull()
    {
        return false;
    }

    @ConfigItem(
            keyName="chatChannelTile",
            name="Tile",
            description="Tile highlight for chat channel players?",
            section=secChat
    )
    default boolean chatChannelTile()
    {
        return false;
    }

    @ConfigItem(
            keyName="chatChannelMinimapAnimation",
            name="Minimap",
            description="Minimap highlight for chat channel players?",
            section=secChat
    )
    default MinimapAnimation chatChannelMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName="chatChannelNameLocation",
            name="Name Location",
            description="Where to display name for chat channel players?",
            section=secChat
    )
    default PlayerNameLocation chatChannelNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="chatChannelLabelLocation",
            name="Label Location",
            description="Where to display 'Chat Channel' label?",
            section=secChat
    )
    default PlayerNameLocation chatChannelLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="chatChannelLabelColorMode",
            name="Label Color Mode",
            description="White or highlight color for label?",
            section=secChat
    )
    default LabelColorMode chatChannelLabelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    // ----------------------------------------------------
    // 9) Tag Players
    // ----------------------------------------------------
    @ConfigSection(
            name="Tag Players Highlight",
            description="Highlight players by name",
            position=15,
            closedByDefault=true
    )
    String secTag = "secTag";

    @ConfigItem(
            keyName="enableTagPlayerHighlight",
            name="Enable Tag",
            description="Enable highlight for tagged players?",
            section=secTag
    )
    default boolean enableTagPlayerHighlight()
    {
        return false;
    }

    @ConfigItem(
            keyName="tagHighlightColor",
            name="Color",
            description="Color for tagged players highlight",
            section=secTag
    )
    default Color tagHighlightColor()
    {
        return Color.ORANGE;
    }

    @ConfigItem(
            keyName="tagOutline",
            name="Outline",
            description="Highlight outline for tagged players?",
            section=secTag
    )
    default boolean tagOutline()
    {
        return false;
    }

    @ConfigItem(
            keyName="tagHull",
            name="Hull",
            description="Highlight hull ring for tagged players?",
            section=secTag
    )
    default boolean tagHull()
    {
        return false;
    }

    @ConfigItem(
            keyName="tagTile",
            name="Tile",
            description="Highlight tile for tagged players?",
            section=secTag
    )
    default boolean tagTile()
    {
        return false;
    }

    @ConfigItem(
            keyName="tagMinimapAnimation",
            name="Minimap",
            description="Minimap highlight for tagged players? (None=off)",
            section=secTag
    )
    default MinimapAnimation tagMinimapAnimation()
    {
        return MinimapAnimation.NONE;
    }

    @ConfigItem(
            keyName="tagNameLocation",
            name="Name Location",
            description="Where to display name for tagged players?",
            section=secTag
    )
    default PlayerNameLocation tagNameLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="tagLabelLocation",
            name="Label Location",
            description="Where to display 'Tagged' label?",
            section=secTag
    )
    default PlayerNameLocation tagLabelLocation()
    {
        return PlayerNameLocation.DISABLED;
    }

    @ConfigItem(
            keyName="tagLabelColorMode",
            name="Label Color Mode",
            description="White or highlight color for 'Tagged' label?",
            section=secTag
    )
    default LabelColorMode tagLabelColorMode()
    {
        return LabelColorMode.CATEGORY_COLOR;
    }

    @ConfigItem(
            keyName="tagMenuOption",
            name="Tag Menu Option",
            description="OFF=disabled, RIGHT_CLICK=always show, SHIFT_RIGHT_CLICK=only when SHIFT held",
            section=secTag
    )
    default TagMenuOption tagMenuOption()
    {
        return TagMenuOption.OFF;
    }

    // (10) Tagged Player List
    @ConfigItem(
            keyName="taggedPlayersList",
            name="Tagged Player List",
            description="Multi-line list of players (one per line).",
            section=secTag
    )
    default String taggedPlayersList()
    {
        return "";
    }
}
