package com.krisped.Highlight;

import net.runelite.api.Client;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HighlightFunction {

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
    public void start() {
        log.info("HighlightFunction started.");
        updateHighlights();
    }

    /**
     * Stopper overlay-funksjonaliteten.
     */
    public void stop() {
        log.info("HighlightFunction stopped.");
        disableHighlight();
    }

    /**
     * Oppdaterer highlights basert på konfigurasjonen.
     */
    public void updateHighlights() {
        if (highlightConfig.isHighlightLocalPlayerEnabled() || highlightConfig.isHighlightAttackableEnabled()) {
            enableHighlight();
        } else {
            disableHighlight();
        }
    }

    private void enableHighlight() {
        if (!isOverlayActive) {
            configureOverlay();
            overlayManager.add(highlightOverlay);
            isOverlayActive = true;
            log.info("Overlay enabled.");
        } else {
            configureOverlay(); // Oppdaterer selv om overlay allerede er aktivt
        }
    }

    private void disableHighlight() {
        if (isOverlayActive) {
            overlayManager.remove(highlightOverlay);
            isOverlayActive = false;
            log.info("Overlay disabled.");
        }
    }

    private void configureOverlay() {
        // Konfigurerer overlay for Local Player
        highlightOverlay.configureOverlay(
                highlightConfig.shouldHighlightTile(),
                highlightConfig.shouldHighlightOutline(),
                highlightConfig.shouldHighlightHull(),
                highlightConfig.shouldHighlightMinimap(),
                highlightConfig.getLocalPlayerColor(),
                highlightConfig.getMinimapAnimation()
        );

        // Konfigurerer overlay for Attackable Players
        highlightOverlay.configureAttackableOverlay(
                highlightConfig.shouldHighlightAttackableTile(),
                highlightConfig.shouldHighlightAttackableOutline(),
                highlightConfig.shouldHighlightAttackableHull(),
                highlightConfig.shouldHighlightAttackableMinimap(),
                highlightConfig.getAttackablePlayerColor(),
                highlightConfig.getAttackableMinimapAnimation()
        );

        log.info("Overlay configured: {}", highlightConfig.debugConfig());
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!"pvptools".equals(event.getGroup())) {
            return;
        }
        log.info("Configuration changed: {}", event.getKey());
        updateHighlights();
    }
}
