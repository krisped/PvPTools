package com.krisped;

import com.google.inject.Provides;  // Legg til denne importen
import com.krisped.FightLogPanel.FightLog;
import com.krisped.PlayerLookupPanel.PlayerLookup;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
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
import net.runelite.client.eventbus.Subscribe;

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

    private FightLog fightLog;

    @Override
    protected void startUp() throws Exception {
        log.info("[KP] PvP Tools started!");

        // Initialize FightLog and PlayerLookup
        fightLog = new FightLog(client, () -> panel.switchToHome());
        PlayerLookup playerLookup = new PlayerLookup(client, clientThread, hiscoreClient, itemManager, spriteManager);

        // Set the action for the back button
        playerLookup.setOnBackButtonPressed(() -> panel.switchToHome()); // Ensures the back button works properly

        // Setup main panel
        panel = new PvPToolsPanel(playerLookup, fightLog);

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

    // Handle chat messages and detect kills and deaths
    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String message = event.getMessage();

        // Detect kill event (e.g., "You have defeated")
        if (message.contains("You have defeated")) {
            String enemyName = message.replace("You have defeated ", "").trim();
            handleKillEvent(enemyName);
        }
        // Detect death event (e.g., "You were defeated by")
        else if (message.contains("You were defeated by")) {
            String enemyName = message.replace("You were defeated by ", "").trim();
            handleDeathEvent(enemyName);
        }
    }

    // Handle kill event
    private void handleKillEvent(String enemyName) {
        log.info("Logging kill against: " + enemyName);
        fightLog.logFight(enemyName, true);
        sendChatMessage("Kill logged to Fight Log");
    }

    // Handle death event
    private void handleDeathEvent(String enemyName) {
        log.info("Logging death against: " + enemyName);
        fightLog.logFight(enemyName, false);
        sendChatMessage("Death logged to Fight Log");
    }

    // Send a message to the chat
    private void sendChatMessage(String message) {
        if (client != null) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[KP] PvP Tools: " + message, null);
        }
    }
}
