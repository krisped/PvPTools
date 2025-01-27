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
import net.runelite.client.ui.*;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PluginDescriptor(
        name="[KP] PvP Tools",
        description="Plugin with multiple highlight categories + Tag Player",
        tags={"pvp","combat","tools"},
        enabledByDefault=false
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
    private EventBus eventBus;
    @Inject
    private HiscoreClient hiscoreClient;
    @Inject
    private ItemManager itemManager;
    @Inject
    private SpriteManager spriteManager;
    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;
    @Inject
    private ConfigManager configManager;
    @Inject
    private PvPToolsConfig config;

    private Overlay highlightOverlay;
    private boolean overlayAdded = false;

    // Panel
    private PvPToolsPanel panel;
    private NavigationButton navButton;
    // Eksempel FightLog
    private FightLog fightLog;

    // Highlight-liste
    private final List<BaseHighlight> highlightList = new ArrayList<>();

    // Disse vil vi opprette:
    private LocalPlayerHighlight localHighlight;
    private AttackablePlayerHighlight attackableHighlight;
    private FriendsHighlight friendsHighlight;
    private IgnoreHighlight ignoreHighlight;
    private ChatChannelHighlight chatHighlight;
    private TagPlayerHighlight tagHighlight;

    @Provides
    PvPToolsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(PvPToolsConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        log.info("PvPToolsPlugin started!");
        SettingsHighlight settings = new SettingsHighlight(config);

        // Opprett alle highlight-kategorier:
        localHighlight = new LocalPlayerHighlight(client, config, modelOutlineRenderer, settings);
        attackableHighlight = new AttackablePlayerHighlight(client, config, modelOutlineRenderer, settings);
        friendsHighlight = new FriendsHighlight(client, config, modelOutlineRenderer, settings);
        ignoreHighlight = new IgnoreHighlight(client, config, modelOutlineRenderer, settings);
        chatHighlight = new ChatChannelHighlight(client, config, modelOutlineRenderer, settings);

        // Tag
        tagHighlight = new TagPlayerHighlight(client, config, modelOutlineRenderer, settings, configManager);
        // Registrer Tag i eventbus (unngår menydubel)
        tagHighlight.registerEvents(eventBus);

        // Legg dem i highlightList
        highlightList.add(localHighlight);
        highlightList.add(attackableHighlight);
        highlightList.add(friendsHighlight);
        highlightList.add(ignoreHighlight);
        highlightList.add(chatHighlight);
        highlightList.add(tagHighlight);

        // Opprett overlay
        highlightOverlay = new Overlay()
        {
            @Override
            public java.awt.Dimension render(java.awt.Graphics2D graphics)
            {
                if (!isAnyHighlightEnabled())
                {
                    return null;
                }
                for (BaseHighlight h : highlightList)
                {
                    h.render(graphics);
                }
                return null;
            }
        };
        highlightOverlay.setPosition(OverlayPosition.DYNAMIC);
        highlightOverlay.setLayer(OverlayLayer.ABOVE_WIDGETS);
        highlightOverlay.setPriority(OverlayPriority.HIGH);

        updateOverlayState();

        // Opprett sidepanel hvis enableSidepanel() er true
        if (config.enableSidepanel())
        {
            fightLog = new FightLog(client, ()-> panel.switchToHome());
            PlayerLookup lookup = new PlayerLookup(client, clientThread, hiscoreClient, itemManager, spriteManager);
            lookup.setOnBackButtonPressed(() -> panel.switchToHome());

            panel = new PvPToolsPanel(lookup, fightLog);

            BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/skull_icon.png");
            navButton = NavigationButton.builder()
                    .tooltip("[KP] PvP Tools")
                    .icon(icon)
                    .panel(panel)
                    .build();
            clientToolbar.addNavigation(navButton);
        }
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("PvPToolsPlugin stopped!");

        // Fjern sidepanel
        if (navButton != null)
        {
            clientToolbar.removeNavigation(navButton);
            navButton=null;
        }

        // Avregistrer Tag
        if (tagHighlight!=null)
        {
            tagHighlight.unregisterEvents();
        }

        // Fjern overlay
        if (overlayAdded)
        {
            overlayManager.remove(highlightOverlay);
            overlayAdded=false;
        }
        highlightList.clear();
    }

    // --------------------------------------------------
    // Eksempel: ChatMessage => FightLog
    // --------------------------------------------------
    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        String msg = event.getMessage();
        if (msg.contains("You have defeated"))
        {
            String enemy = msg.replace("You have defeated", "").trim();
            handleKillEvent(enemy);
        }
        else if (msg.contains("You were defeated by"))
        {
            String enemy = msg.replace("You were defeated by", "").trim();
            handleDeathEvent(enemy);
        }
    }

    private void handleKillEvent(String enemyName)
    {
        if (fightLog != null)
        {
            fightLog.logFight(enemyName, true);
        }
        sendChatMessage("Kill logged vs "+enemyName);
    }

    private void handleDeathEvent(String enemyName)
    {
        if (fightLog != null)
        {
            fightLog.logFight(enemyName, false);
        }
        sendChatMessage("Death logged vs "+enemyName);
    }

    private void sendChatMessage(String text)
    {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[KP] PvP Tools: "+text, null);
    }

    // --------------------------------------------------
    // Oppdater overlay
    // --------------------------------------------------
    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (!"pvptools".equals(event.getGroup()))
        {
            return;
        }
        // Oppdater overlay
        clientThread.invokeLater(this::updateOverlayState);

        // Sjekk sidepanel
        if ("enableSidepanel".equals(event.getKey()))
        {
            clientThread.invokeLater(() -> {
                if (!config.enableSidepanel() && navButton!=null)
                {
                    clientToolbar.removeNavigation(navButton);
                    navButton=null;
                }
                else if (config.enableSidepanel() && navButton==null)
                {
                    fightLog = new FightLog(client, ()-> panel.switchToHome());
                    PlayerLookup lookup = new PlayerLookup(client, clientThread, hiscoreClient, itemManager, spriteManager);
                    lookup.setOnBackButtonPressed(()->panel.switchToHome());

                    panel = new PvPToolsPanel(lookup, fightLog);
                    BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/skull_icon.png");
                    navButton = NavigationButton.builder()
                            .tooltip("[KP] PvP Tools")
                            .icon(icon)
                            .panel(panel)
                            .build();
                    clientToolbar.addNavigation(navButton);
                }
            });
        }
    }

    private void updateOverlayState()
    {
        if (isAnyHighlightEnabled())
        {
            if (!overlayAdded)
            {
                overlayManager.add(highlightOverlay);
                overlayAdded=true;
            }
        }
        else
        {
            if (overlayAdded)
            {
                overlayManager.remove(highlightOverlay);
                overlayAdded=false;
            }
        }
    }

    private boolean isAnyHighlightEnabled()
    {
        // Sjekk ALLE:
        return config.enableLocalPlayer()
                || config.enableAttackablePlayers()
                || config.enableFriendsHighlight()
                || config.enableIgnoreHighlight()
                || config.enableChatChannelHighlight()
                || config.enableTagPlayerHighlight();
    }
}
