package com.krisped;

import com.google.inject.Provides;
import com.krisped.FightLogPanel.FightLog;
import com.krisped.Highlight.*;
import com.krisped.PlayerLookupPanel.PlayerLookup;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.hiscore.HiscoreClient;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PluginDescriptor(
        name = "[KP] PvP Tools",
        description = "A plugin to assist with PvP features",
        tags = {"pvp", "tools", "combat", "runelite"},
        enabledByDefault = false
)
public class PvPToolsPlugin extends Plugin
{
    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private OverlayManager overlayManager;

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

    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private PvPToolsConfig config;

    @Inject
    private ConfigManager configManager;  // For å oppdatere config i TagPlayerHighlight

    @Inject
    private EventBus eventBus; // For å la TagPlayerHighlight subscribe til menu events

    private NavigationButton navButton;
    private PvPToolsPanel panel;
    private FightLog fightLog;

    // Alle highlight-klasser
    private final List<BaseHighlight> highlightList = new ArrayList<>();

    // Overlay-objektet
    private net.runelite.client.ui.overlay.Overlay highlightOverlay;
    private boolean overlayAdded = false;

    @Provides
    PvPToolsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PvPToolsConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        log.info("[KP] PvP Tools started!");

        // 1) FightLog / PlayerLookup / Panel
        fightLog = new FightLog(client, () -> panel.switchToHome());
        PlayerLookup playerLookup = new PlayerLookup(client, clientThread, hiscoreClient, itemManager, spriteManager);
        playerLookup.setOnBackButtonPressed(() -> panel.switchToHome());
        panel = new PvPToolsPanel(playerLookup, fightLog);

        // 2) Lag sidebar-knapp
        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/skull_icon.png");
        navButton = NavigationButton.builder()
                .tooltip("[KP] PvP Tools")
                .icon(icon)
                .panel(panel)
                .build();
        clientToolbar.addNavigation(navButton);

        // 3) Opprett highlight-objekter
        SettingsHighlight settings = new SettingsHighlight(config);

        highlightList.add(new LocalPlayerHighlight(client, config, modelOutlineRenderer, settings));
        highlightList.add(new AttackablePlayerHighlight(client, config, modelOutlineRenderer, settings));
        highlightList.add(new FriendsHighlight(client, config, modelOutlineRenderer, settings));
        highlightList.add(new IgnoreHighlight(client, config, modelOutlineRenderer, settings));
        highlightList.add(new ChatChannelHighlight(client, config, modelOutlineRenderer, settings));

        // Legg til "TagPlayerHighlight" med eventBus/configManager
        highlightList.add(new TagPlayerHighlight(
                client,
                config,
                modelOutlineRenderer,
                settings,
                configManager,
                eventBus
        ));

        // 4) Opprett et anonymt Overlay som kaller each highlight's render()
        highlightOverlay = new net.runelite.client.ui.overlay.Overlay()
        {
            @Override
            public Dimension render(Graphics2D graphics)
            {
                if (!isAnyHighlightEnabled())
                {
                    return null;
                }
                for (BaseHighlight highlight : highlightList)
                {
                    highlight.render(graphics);
                }
                return null;
            }
        };

        highlightOverlay.setPosition(OverlayPosition.DYNAMIC);
        highlightOverlay.setLayer(OverlayLayer.ABOVE_WIDGETS);
        highlightOverlay.setPriority(OverlayPriority.HIGH);

        updateOverlayState();
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("[KP] PvP Tools stopped!");
        clientToolbar.removeNavigation(navButton);

        // Fjern overlay hvis det er lagt til
        if (overlayAdded)
        {
            overlayManager.remove(highlightOverlay);
            overlayAdded = false;
        }
        highlightList.clear();
    }

    // -------------------------------------------------------------
    //  Håndterer chat-meldinger (kill/death) + FightLog
    // -------------------------------------------------------------
    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        String message = event.getMessage();
        if (message.contains("You have defeated"))
        {
            String enemyName = message.replace("You have defeated ", "").trim();
            handleKillEvent(enemyName);
        }
        else if (message.contains("You were defeated by"))
        {
            String enemyName = message.replace("You were defeated by ", "").trim();
            handleDeathEvent(enemyName);
        }
    }

    private void handleKillEvent(String enemyName)
    {
        log.info("Logging kill against: " + enemyName);
        fightLog.logFight(enemyName, true);
        sendChatMessage("Kill logged to Fight Log");
    }

    private void handleDeathEvent(String enemyName)
    {
        log.info("Logging death against: " + enemyName);
        fightLog.logFight(enemyName, false);
        sendChatMessage("Death logged to Fight Log");
    }

    private void sendChatMessage(String msg)
    {
        if (client != null)
        {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[KP] PvP Tools: " + msg, null);
        }
    }

    // -------------------------------------------------------------
    //  Oppdatering når config endres
    // -------------------------------------------------------------
    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (!"pvptools".equals(event.getGroup()))
        {
            return;
        }
        log.info("Configuration changed: {}", event.getKey());

        clientThread.invokeLater(this::updateOverlayState);
    }

    // -------------------------------------------------------------
    //  Hjelpemetoder
    // -------------------------------------------------------------
    private void updateOverlayState()
    {
        if (isAnyHighlightEnabled())
        {
            if (!overlayAdded)
            {
                overlayManager.add(highlightOverlay);
                overlayAdded = true;
            }
        }
        else
        {
            if (overlayAdded)
            {
                overlayManager.remove(highlightOverlay);
                overlayAdded = false;
            }
        }
    }

    private boolean isAnyHighlightEnabled()
    {
        return config.enableLocalPlayer()
                || config.enableAttackablePlayers()
                || config.enableFriendsHighlight()
                || config.enableIgnoreHighlight()
                || config.enableChatChannelHighlight()
                || config.enableTagPlayerHighlight();
    }
}
