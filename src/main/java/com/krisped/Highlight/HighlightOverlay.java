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

public class HighlightOverlay extends Overlay
{
    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final HighlightConfig highlightConfig;

    // Varbits for Wilderness-logikk
    private static final int IN_WILDERNESS = 5963;
    private static final int WILDERNESS_LEVEL = 5964;

    // Lokale parametre for hver kategori (Local, Attackable, Friends, Ignore)
    // Disse settes av configureXxxOverlay(...) og brukes i render().
    // -- Local
    private boolean localHighlightTile;
    private boolean localHighlightOutline;
    private boolean localHighlightHull;
    private boolean localHighlightMinimap;
    private Color localPlayerColor;
    private PvPToolsConfig.MinimapAnimation localMinimapAnimation;

    // -- Attackable
    private boolean attackableHighlightTile;
    private boolean attackableHighlightOutline;
    private boolean attackableHighlightHull;
    private boolean attackableHighlightMinimap;
    private Color attackablePlayerColor;
    private PvPToolsConfig.MinimapAnimation attackableMinimapAnimation;

    // -- Friends
    private boolean friendsHighlightTile;
    private boolean friendsHighlightOutline;
    private boolean friendsHighlightHull;
    private boolean friendsHighlightMinimap;
    private Color friendsHighlightColor;
    private PvPToolsConfig.MinimapAnimation friendsMinimapAnimation;

    // -- Ignore
    private boolean ignoreHighlightTile;
    private boolean ignoreHighlightOutline;
    private boolean ignoreHighlightHull;
    private boolean ignoreHighlightMinimap;
    private Color ignoreHighlightColor;
    private PvPToolsConfig.MinimapAnimation ignoreMinimapAnimation;

    @Inject
    public HighlightOverlay(Client client, ModelOutlineRenderer modelOutlineRenderer, HighlightConfig highlightConfig)
    {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.highlightConfig = highlightConfig;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    // Konfigurasjoner for Local
    public void configureLocalOverlay(
            boolean highlightTile,
            boolean highlightOutline,
            boolean highlightHull,
            boolean highlightMinimap,
            Color color,
            PvPToolsConfig.MinimapAnimation minimapAnimation
    )
    {
        this.localHighlightTile = highlightTile;
        this.localHighlightOutline = highlightOutline;
        this.localHighlightHull = highlightHull;
        this.localHighlightMinimap = highlightMinimap;
        this.localPlayerColor = color;
        this.localMinimapAnimation = minimapAnimation;
    }

    // Konfigurasjoner for Attackable
    public void configureAttackableOverlay(
            boolean highlightTile,
            boolean highlightOutline,
            boolean highlightHull,
            boolean highlightMinimap,
            Color color,
            PvPToolsConfig.MinimapAnimation minimapAnimation
    )
    {
        this.attackableHighlightTile = highlightTile;
        this.attackableHighlightOutline = highlightOutline;
        this.attackableHighlightHull = highlightHull;
        this.attackableHighlightMinimap = highlightMinimap;
        this.attackablePlayerColor = color;
        this.attackableMinimapAnimation = minimapAnimation;
    }

    // Konfigurasjoner for Friends
    public void configureFriendsOverlay(
            boolean highlightTile,
            boolean highlightOutline,
            boolean highlightHull,
            boolean highlightMinimap,
            Color color,
            PvPToolsConfig.MinimapAnimation minimapAnimation
    )
    {
        this.friendsHighlightTile = highlightTile;
        this.friendsHighlightOutline = highlightOutline;
        this.friendsHighlightHull = highlightHull;
        this.friendsHighlightMinimap = highlightMinimap;
        this.friendsHighlightColor = color;
        this.friendsMinimapAnimation = minimapAnimation;
    }

    // Konfigurasjoner for Ignore
    public void configureIgnoreOverlay(
            boolean highlightTile,
            boolean highlightOutline,
            boolean highlightHull,
            boolean highlightMinimap,
            Color color,
            PvPToolsConfig.MinimapAnimation minimapAnimation
    )
    {
        this.ignoreHighlightTile = highlightTile;
        this.ignoreHighlightOutline = highlightOutline;
        this.ignoreHighlightHull = highlightHull;
        this.ignoreHighlightMinimap = highlightMinimap;
        this.ignoreHighlightColor = color;
        this.ignoreMinimapAnimation = minimapAnimation;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null)
        {
            return null;
        }

        // 1) Local player
        if (highlightConfig.isHighlightLocalPlayerEnabled())
        {
            renderPlayerHighlights(
                    graphics,
                    localPlayer,
                    localPlayerColor,
                    localHighlightTile,
                    localHighlightOutline,
                    localHighlightHull,
                    localHighlightMinimap,
                    localMinimapAnimation,
                    highlightConfig.getPlayerNameLocationLocal()
            );
        }

        // 2) Attackable players
        if (highlightConfig.isHighlightAttackableEnabled())
        {
            for (Player player : client.getPlayers())
            {
                if (player != null && player != localPlayer && isAttackable(localPlayer, player))
                {
                    renderPlayerHighlights(
                            graphics,
                            player,
                            attackablePlayerColor,
                            attackableHighlightTile,
                            attackableHighlightOutline,
                            attackableHighlightHull,
                            attackableHighlightMinimap,
                            attackableMinimapAnimation,
                            highlightConfig.getPlayerNameLocationAttackable()
                    );
                }
            }
        }

        // 3) Friends
        if (highlightConfig.isHighlightFriendsEnabled())
        {
            for (Player player : client.getPlayers())
            {
                if (player != null && player != localPlayer && client.isFriended(player.getName(), false))
                {
                    renderPlayerHighlights(
                            graphics,
                            player,
                            friendsHighlightColor,
                            friendsHighlightTile,
                            friendsHighlightOutline,
                            friendsHighlightHull,
                            friendsHighlightMinimap,
                            friendsMinimapAnimation,
                            highlightConfig.getFriendsNameLocation()
                    );
                }
            }
        }

        // 4) Ignore
        if (highlightConfig.isHighlightIgnoreEnabled())
        {
            for (Player player : client.getPlayers())
            {
                if (player != null && isIgnored(player.getName()))
                {
                    renderPlayerHighlights(
                            graphics,
                            player,
                            ignoreHighlightColor,
                            ignoreHighlightTile,
                            ignoreHighlightOutline,
                            ignoreHighlightHull,
                            ignoreHighlightMinimap,
                            ignoreMinimapAnimation,
                            PvPToolsConfig.PlayerNameLocation.ABOVE_HEAD // eller en annen om du vil
                    );
                }
            }
        }

        return null;
    }

    /**
     * Hjelpemetode for å avgjøre om en spiller er på ignore-listen.
     */
    private boolean isIgnored(String playerName)
    {
        if (playerName == null || client == null)
        {
            return false;
        }

        if (client.getIgnoreContainer() != null)
        {
            return client.getIgnoreContainer().findByName(playerName) != null;
        }

        return false;
    }

    /**
     * Hjelpemetode for å avgjøre om en spiller er "attackable" i Wilderness/PvP-worlds.
     */
    private boolean isAttackable(Player localPlayer, Player otherPlayer)
    {
        int localCombat = localPlayer.getCombatLevel();
        int otherCombat = otherPlayer.getCombatLevel();

        // Wilderness
        if (client.getVarbitValue(IN_WILDERNESS) == 1)
        {
            int wildernessLevel = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= wildernessLevel;
        }
        // PVP-world
        else if (isPvpWorld())
        {
            int wildernessLevel = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= (15 + wildernessLevel);
        }

        return false;
    }

    private boolean isPvpWorld()
    {
        EnumSet<WorldType> worldTypes = client.getWorldType();
        return worldTypes.contains(WorldType.PVP) || worldTypes.contains(WorldType.HIGH_RISK);
    }

    /**
     * Selve tegningen av highlights for en gitt spiller.
     */
    private void renderPlayerHighlights(
            Graphics2D graphics,
            Player player,
            Color color,
            boolean highlightTile,
            boolean highlightOutline,
            boolean highlightHull,
            boolean highlightMinimap,
            PvPToolsConfig.MinimapAnimation minimapAnimation,
            PvPToolsConfig.PlayerNameLocation nameLocation
    )
    {
        // 1) Tile
        if (highlightTile)
        {
            renderTileBorder(graphics, player, color);
        }

        // 2) Outline
        if (highlightOutline)
        {
            renderOutline(player, color);
        }

        // 3) Hull
        if (highlightHull)
        {
            renderHull(graphics, player, color);
        }

        // 4) Minimap dot
        if (highlightMinimap)
        {
            highlightMinimapDot(graphics, player, color, minimapAnimation);
        }

        // 5) Navn og level
        if (nameLocation != PvPToolsConfig.PlayerNameLocation.DISABLED)
        {
            renderNameAndLevel(graphics, player, nameLocation, color);
        }
    }

    private void renderTileBorder(Graphics2D graphics, Player player, Color color)
    {
        LocalPoint playerLocation = player.getLocalLocation();
        if (playerLocation == null)
        {
            return;
        }
        Polygon tilePolygon = Perspective.getCanvasTilePoly(client, playerLocation);
        if (tilePolygon != null)
        {
            graphics.setColor(color);
            graphics.drawPolygon(tilePolygon);
        }
    }

    private void renderOutline(Player player, Color color)
    {
        if (player.getModel() == null)
        {
            return;
        }
        modelOutlineRenderer.drawOutline(player, 2, color, 255);
    }

    private void renderHull(Graphics2D graphics, Player player, Color color)
    {
        Shape hull = player.getConvexHull();
        if (hull != null)
        {
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
            graphics.fill(hull);
            graphics.setColor(color);
            graphics.draw(hull);
        }
    }

    private void highlightMinimapDot(Graphics2D graphics, Player player, Color color, PvPToolsConfig.MinimapAnimation minimapAnimation)
    {
        final Point minimapLocation = player.getMinimapLocation();
        if (minimapLocation == null)
        {
            return;
        }

        int dotSize = 6;
        int centerX = minimapLocation.getX();
        int centerY = minimapLocation.getY();

        switch (minimapAnimation)
        {
            case Static:
                drawDot(graphics, centerX, centerY, dotSize, color);
                break;
            case Pulse:
            {
                long pulseSize = (System.currentTimeMillis() % 1000L) / 250;
                drawDot(graphics, centerX, centerY, dotSize + (int) pulseSize * 2, color);
                break;
            }
            case Blink:
            {
                if ((System.currentTimeMillis() / 500L) % 2 == 0)
                {
                    drawDot(graphics, centerX, centerY, dotSize, color);
                }
                break;
            }
            case Sonar:
            {
                long sonarSize = (System.currentTimeMillis() % 2000L) / 250;
                drawTransparentCircle(graphics, centerX, centerY, dotSize + (int) sonarSize * 2, color);
                break;
            }
        }
    }

    private void drawDot(Graphics2D graphics, int x, int y, int size, Color color)
    {
        graphics.setColor(color);
        graphics.fillOval(x - size / 2, y - size / 2, size, size);
    }

    private void drawTransparentCircle(Graphics2D graphics, int x, int y, int size, Color color)
    {
        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        graphics.drawOval(x - size / 2, y - size / 2, size, size);
    }

    private void renderNameAndLevel(Graphics2D graphics, Player player, PvPToolsConfig.PlayerNameLocation nameLocation, Color color)
    {
        String nameText = player.getName() + " (" + player.getCombatLevel() + ")";
        Point textLocation = null;

        switch (nameLocation)
        {
            case ABOVE_HEAD:
                textLocation = player.getCanvasTextLocation(graphics, nameText, player.getLogicalHeight() + 20);
                break;
            case CENTER_OF_MODEL:
                textLocation = player.getCanvasTextLocation(graphics, nameText, player.getLogicalHeight() / 2);
                break;
            case RIGHT_OF_MODEL:
                textLocation = player.getCanvasTextLocation(graphics, nameText, player.getLogicalHeight());
                if (textLocation != null)
                {
                    textLocation = new Point(textLocation.getX() + 10, textLocation.getY() - 5);
                }
                break;
            default:
                // DISABLED eller ikke håndtert
                return;
        }

        if (textLocation != null)
        {
            graphics.setFont(new Font("Arial", Font.PLAIN, 11));
            graphics.setColor(new Color(0, 0, 0, 128));
            graphics.drawString(nameText, textLocation.getX() + 1, textLocation.getY() + 1);
            graphics.setColor(color);
            graphics.drawString(nameText, textLocation.getX(), textLocation.getY());
        }
    }
}
