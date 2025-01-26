package com.krisped.Highlight;

import net.runelite.api.Client;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HighlightFunction
{
    @Inject
    private Client client;

    @Inject
    private HighlightConfig highlightConfig;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private HighlightOverlay highlightOverlay;

    private boolean isOverlayActive = false;

    /**
     * Starter overlay-funksjonaliteten.
     */
    public void start()
    {
        log.info("HighlightFunction started.");
        updateHighlights();
    }

    /**
     * Stopper overlay-funksjonaliteten.
     */
    public void stop()
    {
        log.info("HighlightFunction stopped.");
        disableHighlight();
    }

    /**
     * Oppdaterer highlights basert på konfigurasjonen.
     */
    public void updateHighlights()
    {
        if (highlightConfig.isAnyHighlightEnabled())
        {
            enableHighlight();
        }
        else
        {
            disableHighlight();
        }
    }

    private void enableHighlight()
    {
        if (!isOverlayActive)
        {
            configureOverlay();
            overlayManager.add(highlightOverlay);
            isOverlayActive = true;
            log.info("Overlay enabled.");
        }
        else
        {
            // Hvis overlay allerede er aktivt, bare oppdater konfigurasjonene
            configureOverlay();
        }
    }

    private void disableHighlight()
    {
        if (isOverlayActive)
        {
            overlayManager.remove(highlightOverlay);
            isOverlayActive = false;
            log.info("Overlay disabled.");
        }
    }

    /**
     * Les konfigurasjonen fra highlightConfig, og sett de ulike overlay-boolene.
     */
    private void configureOverlay()
    {
        log.info("Configuring overlays...");

        // 1) Local
        highlightOverlay.configureLocalOverlay(
                highlightConfig.shouldHighlightTile(),
                highlightConfig.shouldHighlightOutline(),
                highlightConfig.shouldHighlightHull(),
                highlightConfig.shouldHighlightMinimap(),
                highlightConfig.getLocalPlayerColor(),
                highlightConfig.getMinimapAnimation()
        );

        // 2) Attackable
        highlightOverlay.configureAttackableOverlay(
                highlightConfig.shouldHighlightAttackableTile(),
                highlightConfig.shouldHighlightAttackableOutline(),
                highlightConfig.shouldHighlightAttackableHull(),
                highlightConfig.shouldHighlightAttackableMinimap(),
                highlightConfig.getAttackablePlayerColor(),
                highlightConfig.getAttackableMinimapAnimation()
        );

        // 3) Friends
        highlightOverlay.configureFriendsOverlay(
                highlightConfig.shouldHighlightFriendsTile(),
                highlightConfig.shouldHighlightFriendsOutline(),
                highlightConfig.shouldHighlightFriendsHull(),
                highlightConfig.shouldHighlightFriendsMinimap(),
                highlightConfig.getFriendsHighlightColor(),
                highlightConfig.getFriendsMinimapAnimation()
        );

        // 4) Ignore
        highlightOverlay.configureIgnoreOverlay(
                highlightConfig.shouldHighlightIgnoreTile(),
                highlightConfig.shouldHighlightIgnoreOutline(),
                highlightConfig.shouldHighlightIgnoreHull(),
                highlightConfig.shouldHighlightIgnoreMinimap(),
                highlightConfig.getIgnoreHighlightColor(),
                highlightConfig.getIgnoreMinimapAnimation()
        );

        log.info("Overlay configured: {}", highlightConfig.debugConfig());
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (!"pvptools".equals(event.getGroup()))
        {
            return;
        }
        log.info("Configuration changed in HighlightFunction: {}", event.getKey());
        updateHighlights();
    }
}
