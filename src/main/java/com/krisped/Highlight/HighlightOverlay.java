package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Perspective;
import net.runelite.api.WorldType;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;
import java.util.EnumSet;

public class HighlightOverlay extends Overlay {

    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final HighlightConfig highlightConfig;

    // Varbits-konstanter integrert i denne klassen
    private static final int IN_WILDERNESS = 5963; // Indikerer om spilleren er i Wilderness
    private static final int WILDERNESS_LEVEL = 5964; // Representerer Wilderness-nivå

    // Local Player configurations
    private boolean highlightTile;
    private boolean highlightOutline;
    private boolean highlightHull;
    private boolean highlightMinimap;
    private Color localPlayerColor;
    private PvPToolsConfig.MinimapAnimation minimapAnimation;

    // Attackable Players configurations
    private boolean highlightAttackableTile;
    private boolean highlightAttackableOutline;
    private boolean highlightAttackableHull;
    private boolean highlightAttackableMinimap;
    private Color attackablePlayerColor;
    private PvPToolsConfig.MinimapAnimation attackableMinimapAnimation;

    @Inject
    public HighlightOverlay(Client client, ModelOutlineRenderer modelOutlineRenderer, HighlightConfig highlightConfig) {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.highlightConfig = highlightConfig;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    /**
     * Konfigurerer overlay for Local Player.
     */
    public void configureOverlay(
            boolean highlightTile,
            boolean highlightOutline,
            boolean highlightHull,
            boolean highlightMinimap,
            Color localPlayerColor,
            PvPToolsConfig.MinimapAnimation minimapAnimation
    ) {
        this.highlightTile = highlightTile;
        this.highlightOutline = highlightOutline;
        this.highlightHull = highlightHull;
        this.highlightMinimap = highlightMinimap;
        this.localPlayerColor = localPlayerColor;
        this.minimapAnimation = minimapAnimation;
    }

    /**
     * Konfigurerer overlay for Attackable Players.
     */
    public void configureAttackableOverlay(
            boolean highlightAttackableTile,
            boolean highlightAttackableOutline,
            boolean highlightAttackableHull,
            boolean highlightAttackableMinimap,
            Color attackablePlayerColor,
            PvPToolsConfig.MinimapAnimation attackableMinimapAnimation
    ) {
        this.highlightAttackableTile = highlightAttackableTile;
        this.highlightAttackableOutline = highlightAttackableOutline;
        this.highlightAttackableHull = highlightAttackableHull;
        this.highlightAttackableMinimap = highlightAttackableMinimap;
        this.attackablePlayerColor = attackablePlayerColor;
        this.attackableMinimapAnimation = attackableMinimapAnimation;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return null;
        }

        // Render Local Player highlights
        if (highlightConfig.isHighlightLocalPlayerEnabled()) {
            renderPlayerHighlights(graphics, localPlayer, localPlayerColor, true, minimapAnimation, highlightConfig.getPlayerNameLocationLocal());
        }

        // Render Attackable Players highlights
        if (highlightConfig.isHighlightAttackableEnabled()) {
            for (Player player : client.getPlayers()) {
                if (player != null && player != localPlayer && isAttackable(localPlayer, player)) {
                    renderPlayerHighlights(graphics, player, attackablePlayerColor, false, attackableMinimapAnimation, highlightConfig.getPlayerNameLocationAttackable());
                }
            }
        }

        return null;
    }

    private boolean isAttackable(Player localPlayer, Player otherPlayer) {
        int localCombat = localPlayer.getCombatLevel();
        int otherCombat = otherPlayer.getCombatLevel();

        if (client.getVarbitValue(IN_WILDERNESS) == 1) {
            int wildernessLevel = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= wildernessLevel;
        } else if (isPvpWorld()) {
            int wildernessLevel = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= 15 + wildernessLevel;
        }
        return false;
    }

    private boolean isPvpWorld() {
        EnumSet<WorldType> worldTypes = client.getWorldType();
        return worldTypes.contains(WorldType.PVP) || worldTypes.contains(WorldType.HIGH_RISK);
    }

    private void renderPlayerHighlights(Graphics2D graphics, Player player, Color color, boolean isLocalPlayer, PvPToolsConfig.MinimapAnimation minimapAnimation, PvPToolsConfig.PlayerNameLocation nameLocation) {
        if ((isLocalPlayer && highlightTile) || (!isLocalPlayer && highlightAttackableTile)) {
            renderTileBorder(graphics, player, color);
        }

        if ((isLocalPlayer && highlightOutline) || (!isLocalPlayer && highlightAttackableOutline)) {
            renderOutline(player, color);
        }

        if ((isLocalPlayer && highlightHull) || (!isLocalPlayer && highlightAttackableHull)) {
            renderHull(graphics, player, color);
        }

        if ((isLocalPlayer && highlightMinimap) || (!isLocalPlayer && highlightAttackableMinimap)) {
            highlightMinimapDot(graphics, player, color, minimapAnimation);
        }

        if (nameLocation != PvPToolsConfig.PlayerNameLocation.DISABLED) {
            renderNameAndLevel(graphics, player, nameLocation, color);
        }
    }

    private void renderTileBorder(Graphics2D graphics, Player player, Color color) {
        LocalPoint playerLocation = player.getLocalLocation();
        if (playerLocation == null) {
            return;
        }

        Polygon tilePolygon = Perspective.getCanvasTilePoly(client, playerLocation);
        if (tilePolygon != null) {
            graphics.setColor(color);
            graphics.drawPolygon(tilePolygon);
        }
    }

    private void renderOutline(Player player, Color color) {
        if (player.getModel() == null) {
            return;
        }
        modelOutlineRenderer.drawOutline(player, 2, color, 255);
    }

    private void renderHull(Graphics2D graphics, Player player, Color color) {
        Shape hull = player.getConvexHull();
        if (hull != null) {
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
            graphics.fill(hull);
            graphics.setColor(color);
            graphics.draw(hull);
        }
    }

    private void highlightMinimapDot(Graphics2D graphics, Player player, Color color, PvPToolsConfig.MinimapAnimation minimapAnimation) {
        final Point minimapLocation = player.getMinimapLocation();
        if (minimapLocation == null) {
            return;
        }

        int dotSize = 6;
        int centerX = minimapLocation.getX();
        int centerY = minimapLocation.getY();

        switch (minimapAnimation) {
            case Static:
                drawDot(graphics, centerX, centerY, dotSize, color);
                break;

            case Pulse:
                long pulseSize = (System.currentTimeMillis() % 1000L) / 250;
                drawDot(graphics, centerX, centerY, dotSize + (int) pulseSize * 2, color);
                break;

            case Blink:
                if ((System.currentTimeMillis() / 500L) % 2 == 0) {
                    drawDot(graphics, centerX, centerY, dotSize, color);
                }
                break;

            case Sonar:
                long sonarSize = (System.currentTimeMillis() % 2000L) / 250;
                drawTransparentCircle(graphics, centerX, centerY, dotSize + (int) sonarSize * 2, color);
                break;
        }
    }

    private void drawDot(Graphics2D graphics, int x, int y, int size, Color color) {
        graphics.setColor(color);
        graphics.fillOval(x - size / 2, y - size / 2, size, size);
    }

    private void drawTransparentCircle(Graphics2D graphics, int x, int y, int size, Color color) {
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        graphics.drawOval(x - size / 2, y - size / 2, size, size);
    }

    private void renderNameAndLevel(Graphics2D graphics, Player player, PvPToolsConfig.PlayerNameLocation nameLocation, Color color) {
        String nameText = player.getName() + " (" + player.getCombatLevel() + ")";
        Point textLocation = null;

        switch (nameLocation) {
            case ABOVE_HEAD:
                textLocation = player.getCanvasTextLocation(graphics, nameText, player.getLogicalHeight() + 20);
                break;

            case CENTER_OF_MODEL:
                textLocation = player.getCanvasTextLocation(graphics, nameText, player.getLogicalHeight() / 2);
                break;

            case RIGHT_OF_MODEL:
                textLocation = player.getCanvasTextLocation(graphics, nameText, player.getLogicalHeight());
                if (textLocation != null) {
                    textLocation = new Point(textLocation.getX() + 10, textLocation.getY() - 5); // Justert til høyre
                }
                break;
        }

        if (textLocation != null) {
            graphics.setFont(new Font("Arial", Font.PLAIN, 11)); // Mindre font
            graphics.setColor(new Color(0, 0, 0, 128)); // Skygge (sort, halvtransparent)
            graphics.drawString(nameText, textLocation.getX() + 1, textLocation.getY() + 1); // Skyggeeffekt
            graphics.setColor(color); // Bruker fargen fra konfigurasjonen
            graphics.drawString(nameText, textLocation.getX(), textLocation.getY());
        }
    }
}
