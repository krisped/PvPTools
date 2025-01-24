package com.krisped;

import com.google.inject.Provides;
import com.krisped.PlayerLookupPanel.PlayerLookup;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.hiscore.HiscoreClient;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
        name = "[KP] PvP Tools",
        description = "A plugin to assist with PvP features",
        tags = {"pvp", "tools", "combat", "runelite"},
        enabledByDefault = false
)
public class PvPToolsPlugin extends Plugin {

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private HiscoreClient hiscoreClient;

    @Inject
    private ItemManager itemManager;

    @Inject
    private SpriteManager spriteManager;

    private NavigationButton navButton;
    private PvPToolsPanel panel;

    @Override
    protected void startUp() throws Exception {
        log.info("[KP] PvP Tools started!");

        PlayerLookup playerLookup = new PlayerLookup(client, clientThread, hiscoreClient, itemManager, spriteManager);
        panel = new PvPToolsPanel(playerLookup);
        playerLookup.setOnBackButtonPressed(panel::switchToHome);

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/skull_icon.png");

        navButton = NavigationButton.builder()
                .tooltip("[KP] PvP Tools")
                .icon(icon)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("[KP] PvP Tools stopped!");
        clientToolbar.removeNavigation(navButton);
    }

    @Provides
    PvPToolsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PvPToolsConfig.class);
    }
}
