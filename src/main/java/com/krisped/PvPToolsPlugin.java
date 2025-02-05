package com.krisped;

import com.google.inject.Provides;
import com.krisped.Highlight.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
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
    private OverlayManager overlayManager;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private EventBus eventBus;
    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;
    @Inject
    private ConfigManager configManager;
    @Inject
    private PvPToolsConfig config;

    // To overlays – én under widgets (normal), én over (minimap)
    private Overlay highlightOverlayUnder;
    private Overlay highlightOverlayAbove;
    private boolean overlaysAdded = false;

    // Highlight-liste
    private final List<BaseHighlight> highlightList = new ArrayList<>();

    // Underklasser
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

        // Opprett highlight-objekter
        localHighlight = new LocalPlayerHighlight(client, config, modelOutlineRenderer, settings);
        attackableHighlight = new AttackablePlayerHighlight(client, config, modelOutlineRenderer, settings);
        friendsHighlight = new FriendsHighlight(client, config, modelOutlineRenderer, settings);
        ignoreHighlight = new IgnoreHighlight(client, config, modelOutlineRenderer, settings);
        chatHighlight = new ChatChannelHighlight(client, config, modelOutlineRenderer, settings);

        tagHighlight = new TagPlayerHighlight(client, config, modelOutlineRenderer, settings, configManager);
        tagHighlight.registerEvents(eventBus);

        // Legg i listen
        highlightList.add(localHighlight);
        highlightList.add(attackableHighlight);
        highlightList.add(friendsHighlight);
        highlightList.add(ignoreHighlight);
        highlightList.add(chatHighlight);
        highlightList.add(tagHighlight);

        // Opprett overlay for "normal" highlights (UNDER widgets)
        highlightOverlayUnder = new Overlay()
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
                    h.renderNormal(graphics);
                }
                return null;
            }
        };
        highlightOverlayUnder.setPosition(OverlayPosition.DYNAMIC);
        highlightOverlayUnder.setLayer(OverlayLayer.UNDER_WIDGETS);
        highlightOverlayUnder.setPriority(OverlayPriority.LOW);

        // Opprett overlay for minimap highlights (OVER widgets)
        highlightOverlayAbove = new Overlay()
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
                    h.renderMinimap(graphics);
                }
                return null;
            }
        };
        highlightOverlayAbove.setPosition(OverlayPosition.DYNAMIC);
        highlightOverlayAbove.setLayer(OverlayLayer.ABOVE_WIDGETS);
        highlightOverlayAbove.setPriority(OverlayPriority.HIGHEST);

        updateOverlayState();
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("PvPToolsPlugin stopped!");

        // Avregistrer Tag
        if (tagHighlight != null)
        {
            tagHighlight.unregisterEvents();
        }

        // Fjern overlays
        if (overlaysAdded)
        {
            overlayManager.remove(highlightOverlayUnder);
            overlayManager.remove(highlightOverlayAbove);
            overlaysAdded = false;
        }

        highlightList.clear();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (!"pvptools".equals(event.getGroup()))
        {
            return;
        }
        clientThread.invokeLater(this::updateOverlayState);
    }

    private void updateOverlayState()
    {
        boolean any = isAnyHighlightEnabled();
        if (any && !overlaysAdded)
        {
            overlayManager.add(highlightOverlayUnder);
            overlayManager.add(highlightOverlayAbove);
            overlaysAdded = true;
        }
        else if (!any && overlaysAdded)
        {
            overlayManager.remove(highlightOverlayUnder);
            overlayManager.remove(highlightOverlayAbove);
            overlaysAdded = false;
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
