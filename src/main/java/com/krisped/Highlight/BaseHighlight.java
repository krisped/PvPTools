package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

/**
 * Baseklasse som håndterer tegning av tile, outline, hull,
 * minimap-dot og navn/level. Alle highlight-kategorier
 * kaller disse metodene.
 */
public abstract class BaseHighlight
{
    protected final Client client;
    protected final PvPToolsConfig config;
    protected final ModelOutlineRenderer modelOutlineRenderer;
    protected final SettingsHighlight settingsHighlight; // Holder bl.a. outline-tykkelse

    public BaseHighlight(Client client,
                         PvPToolsConfig config,
                         ModelOutlineRenderer modelOutlineRenderer,
                         SettingsHighlight settingsHighlight)
    {
        this.client = client;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.settingsHighlight = settingsHighlight;
    }

    /**
     * Kalles av plugin-ens overlay for å tegne denne highlight-kategoriens logikk.
     */
    public abstract void render(Graphics2D graphics);

    /**
     * Fellesmetode som tegner alt for én spiller (tile, outline, hull, minimap, navn).
     */
    protected void renderPlayerHighlight(Graphics2D g,
                                         Player player,
                                         boolean highlightTile,
                                         boolean highlightOutline,
                                         boolean highlightHull,
                                         boolean highlightMinimap,
                                         Color color,
                                         PvPToolsConfig.MinimapAnimation minimapAnim,
                                         PvPToolsConfig.PlayerNameLocation nameLoc)
    {
        if (player == null) return;

        // 1) Tile
        if (highlightTile)
        {
            renderTileBorder(g, player, color);
        }
        // 2) Outline
        if (highlightOutline)
        {
            renderOutline(player, color);
        }
        // 3) Hull
        if (highlightHull)
        {
            renderHull(g, player, color);
        }
        // 4) Minimap
        if (highlightMinimap)
        {
            renderMinimapDot(g, player, color, minimapAnim);
        }
        // 5) Navn
        if (nameLoc != PvPToolsConfig.PlayerNameLocation.DISABLED)
        {
            renderNameAndLevel(g, player, nameLoc, color);
        }
    }

    // -----------------------------------------------------
    // 1) Tile
    // -----------------------------------------------------
    private void renderTileBorder(Graphics2D g, Player player, Color color)
    {
        LocalPoint loc = player.getLocalLocation();
        if (loc == null) return;

        Polygon tilePoly = Perspective.getCanvasTilePoly(client, loc);
        if (tilePoly != null)
        {
            g.setColor(color);
            g.draw(tilePoly);
        }
    }

    // -----------------------------------------------------
    // 2) Outline
    // -----------------------------------------------------
    private void renderOutline(Player player, Color color)
    {
        if (player.getModel() == null) return;

        int thickness = settingsHighlight.getOutlineThickness();
        modelOutlineRenderer.drawOutline(player, thickness, color, 255);
    }

    // -----------------------------------------------------
    // 3) Hull
    // -----------------------------------------------------
    private void renderHull(Graphics2D g, Player player, Color color)
    {
        Shape hull = player.getConvexHull();
        if (hull != null)
        {
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
            g.fill(hull);

            g.setColor(color);
            g.draw(hull);
        }
    }

    // -----------------------------------------------------
    // 4) Minimap
    // -----------------------------------------------------
    private void renderMinimapDot(Graphics2D g,
                                  Player player,
                                  Color color,
                                  PvPToolsConfig.MinimapAnimation anim)
    {
        Point mmLoc = player.getMinimapLocation();
        if (mmLoc == null) return;

        final int baseSize = 6;
        int x = mmLoc.getX();
        int y = mmLoc.getY();

        switch (anim)
        {
            case Static:
                fillCircle(g, x, y, baseSize, color);
                break;
            case Pulse:
            {
                long factor = (System.currentTimeMillis() % 1000L) / 250;
                fillCircle(g, x, y, baseSize + (int)factor * 2, color);
                break;
            }
            case Blink:
            {
                if ((System.currentTimeMillis() / 500L) % 2 == 0)
                {
                    fillCircle(g, x, y, baseSize, color);
                }
                break;
            }
            case Sonar:
            {
                long factor = (System.currentTimeMillis() % 2000L) / 250;
                drawCircle(g, x, y, baseSize + (int)factor * 2, color);
                break;
            }
        }
    }

    // -----------------------------------------------------
    // 5) Navn + Combatlevel
    // -----------------------------------------------------
    private void renderNameAndLevel(Graphics2D g,
                                    Player player,
                                    PvPToolsConfig.PlayerNameLocation nameLoc,
                                    Color color)
    {
        String text = player.getName() + " (" + player.getCombatLevel() + ")";
        net.runelite.api.Point txtPoint = null;

        switch (nameLoc)
        {
            case ABOVE_HEAD:
                txtPoint = player.getCanvasTextLocation(g, text, player.getLogicalHeight() + 20);
                break;
            case CENTER_OF_MODEL:
                txtPoint = player.getCanvasTextLocation(g, text, player.getLogicalHeight() / 2);
                break;
            case RIGHT_OF_MODEL:
                txtPoint = player.getCanvasTextLocation(g, text, player.getLogicalHeight());
                if (txtPoint != null)
                {
                    txtPoint = new Point(txtPoint.getX() + 10, txtPoint.getY() - 5);
                }
                break;
            default:
                return;
        }

        if (txtPoint != null)
        {
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            // Skygge
            g.setColor(new Color(0, 0, 0, 128));
            g.drawString(text, txtPoint.getX() + 1, txtPoint.getY() + 1);

            // Tekst
            g.setColor(color);
            g.drawString(text, txtPoint.getX(), txtPoint.getY());
        }
    }

    // -----------------------------------------------------
    //  Hjelpe-metoder for å tegne sirkler
    // -----------------------------------------------------
    private void fillCircle(Graphics2D g, int cx, int cy, int size, Color c)
    {
        g.setColor(c);
        g.fillOval(cx - size / 2, cy - size / 2, size, size);
    }

    private void drawCircle(Graphics2D g, int cx, int cy, int size, Color c)
    {
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 120));
        g.drawOval(cx - size / 2, cy - size / 2, size, size);
    }
}
