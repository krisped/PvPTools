package com.krisped;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("pvptools")
public interface PvPToolsConfig extends Config {

    @ConfigItem(
            keyName = "panelPriority",
            name = "Side Panel Priority",
            description = "Set the priority for the side panel icon (higher is displayed earlier)."
    )
    default int panelPriority() {
        return 5; // Default priority
    }
}