package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class HighlightOverlay extends Overlay {

    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;

    private boolean highlightTile;
    private boolean highlightOutline;
    private boolean highlightHull;
    private boolean highlightMinimap;
    private Color localPlayerColor;
    private PvPToolsConfig.MinimapAnimation minimapAnimation;

    @Inject
    public HighlightOverlay(Client client, ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

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

    @Override
    public Dimension render(Graphics2D graphics) {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return null;
        }

        // Render highlights for the local player
        if (highlightTile) {
            renderTileBorder(graphics, localPlayer, localPlayerColor);
        }
        if (highlightOutline) {
            renderOutline(localPlayer, localPlayerColor);
        }
        if (highlightHull) {
            renderHull(graphics, localPlayer, localPlayerColor);
        }
        if (highlightMinimap) {
            highlightMinimapDot(graphics, localPlayer);
        }

        return null;
    }

    private void renderTileBorder(Graphics2D graphics, Player player, Color color) {
        LocalPoint playerLocation = player.getLocalLocation();
        if (playerLocation == null) {
            return;
        }

        Polygon tilePolygon = Perspective.getCanvasTilePoly(client, playerLocation);
        if (tilePolygon != null) {
            graphics.setColor(color);
            graphics.drawPolygon(tilePolygon); // Only draw the border of the tile
        }
    }

    private void renderOutline(Player player, Color color) {
        if (player.getModel() == null) {
            return;
        }

        // Render the player's model outline using ModelOutlineRenderer
        modelOutlineRenderer.drawOutline(player, 2, color, 255);
    }

    private void renderHull(Graphics2D graphics, Player player, Color color) {
        Shape hull = player.getConvexHull();
        if (hull != null) {
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50)); // Semi-transparent
            graphics.fill(hull);
            graphics.setColor(color);
            graphics.draw(hull);
        }
    }

    private void highlightMinimapDot(Graphics2D graphics, Player player) {
        final Point minimapLocation = player.getMinimapLocation();
        if (minimapLocation == null) {
            return;
        }

        int dotSize = 6; // Standard dot size
        int centerX = minimapLocation.getX();
        int centerY = minimapLocation.getY();

        switch (minimapAnimation) {
            case Static:
                drawDot(graphics, centerX, centerY, dotSize);
                break;

            case Pulse:
                long pulseSize = (System.currentTimeMillis() % 1000L) / 250;
                drawDot(graphics, centerX, centerY, dotSize + (int) pulseSize * 2);
                break;

            case Blink:
                if ((System.currentTimeMillis() / 500L) % 2 == 0) {
                    drawDot(graphics, centerX, centerY, dotSize);
                }
                break;

            case Sonar:
                long sonarSize = (System.currentTimeMillis() % 2000L) / 250;
                drawTransparentCircle(graphics, centerX, centerY, dotSize + (int) sonarSize * 2);
                break;
        }
    }

    private void drawDot(Graphics2D graphics, int x, int y, int size) {
        graphics.setColor(localPlayerColor);
        graphics.fillOval(x - size / 2, y - size / 2, size, size);
    }

    private void drawTransparentCircle(Graphics2D graphics, int x, int y, int size) {
        graphics.setColor(new Color(localPlayerColor.getRed(), localPlayerColor.getGreen(),
                localPlayerColor.getBlue(), 100)); // Transparent color
        graphics.drawOval(x - size / 2, y - size / 2, size, size);
    }
}
