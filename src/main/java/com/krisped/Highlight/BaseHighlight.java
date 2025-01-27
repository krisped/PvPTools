package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public abstract class BaseHighlight
{
    protected final Client client;
    protected final PvPToolsConfig config;
    protected final ModelOutlineRenderer modelOutlineRenderer;
    protected final SettingsHighlight settingsHighlight;

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

    public abstract void render(Graphics2D graphics);

    /**
     * Fellesmetode som tegner tile, outline, hull, minimap og ev. navn.
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

        if (highlightTile)
        {
            renderTile(g, player, color);
        }
        if (highlightOutline)
        {
            renderOutline(player, color);
        }
        if (highlightHull)
        {
            renderHull(g, player, color);
        }
        if (highlightMinimap)
        {
            renderMinimapDot(g, player, color, minimapAnim);
        }
        if (nameLoc != PvPToolsConfig.PlayerNameLocation.DISABLED)
        {
            renderNameAndLevel(g, player, nameLoc, color);
        }
    }

    // ----------------------------------------------------
    // 1) Tile
    // ----------------------------------------------------
    private void renderTile(Graphics2D g, Player p, Color color)
    {
        LocalPoint lp = p.getLocalLocation();
        if (lp == null) return;
        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly == null) return;

        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(settingsHighlight.getTileThickness()));
        g.setColor(color);
        g.draw(poly);
        g.setStroke(old);
    }

    // ----------------------------------------------------
    // 2) Outline
    // ----------------------------------------------------
    private void renderOutline(Player p, Color color)
    {
        if (p.getModel() == null) return;
        modelOutlineRenderer.drawOutline(
                p,
                settingsHighlight.getOutlineThickness(),
                color,
                255
        );
    }

    // ----------------------------------------------------
    // 3) Hull
    // ----------------------------------------------------
    private void renderHull(Graphics2D g, Player p, Color color)
    {
        Shape hull = p.getConvexHull();
        if (hull == null) return;

        // Fyll
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        g.fill(hull);

        // Kant
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(settingsHighlight.getHullThickness()));
        g.setColor(color);
        g.draw(hull);
        g.setStroke(old);
    }

    // ----------------------------------------------------
    // 4) Minimap
    // ----------------------------------------------------
    private void renderMinimapDot(Graphics2D g,
                                  Player p,
                                  Color color,
                                  PvPToolsConfig.MinimapAnimation anim)
    {
        net.runelite.api.Point mmLoc = p.getMinimapLocation();
        if (mmLoc == null) return;

        final int baseSize = settingsHighlight.getMinimapCircleSize();
        final long speed   = settingsHighlight.getMinimapAnimSpeed();

        int x = mmLoc.getX();
        int y = mmLoc.getY();

        switch (anim)
        {
            case Static:
                fillCircle(g, x, y, baseSize, color);
                break;

            case Pulse:
            {
                long cycle = (System.currentTimeMillis() % speed) / (speed/4);
                int size = baseSize + (int)cycle * 2;
                fillCircle(g, x, y, size, color);
                break;
            }
            case Blink:
            {
                long halfCycle = speed / 2;
                if ((System.currentTimeMillis() % speed) < halfCycle)
                {
                    fillCircle(g, x, y, baseSize, color);
                }
                break;
            }
            case Sonar:
            {
                long factor = (System.currentTimeMillis() % (2*speed)) / (speed/4);
                int size = baseSize + (int)factor*2;
                drawCircle(g, x, y, size, color);
                break;
            }
            case Wave:
            {
                double t = (System.currentTimeMillis() % speed) / (double)speed;
                double wave = Math.sin(t*2*Math.PI);
                int radius = (int)(baseSize + wave*baseSize);
                if (radius<2) radius=2;
                fillCircle(g, x, y, radius, color);
                break;
            }
        }
    }

    private void fillCircle(Graphics2D g, int cx, int cy, int size, Color c)
    {
        g.setColor(c);
        g.fillOval(cx - size/2, cy - size/2, size, size);
    }

    private void drawCircle(Graphics2D g, int cx, int cy, int size, Color c)
    {
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 120));
        g.drawOval(cx - size/2, cy - size/2, size, size);
    }

    // ----------------------------------------------------
    // 5) Navn + Level
    // ----------------------------------------------------
    private void renderNameAndLevel(Graphics2D g,
                                    Player p,
                                    PvPToolsConfig.PlayerNameLocation loc,
                                    Color color)
    {
        String text = p.getName() + " (" + p.getCombatLevel() + ")";
        net.runelite.api.Point txtPt = null;

        switch (loc)
        {
            case ABOVE_HEAD:
                txtPt = p.getCanvasTextLocation(g, text, p.getLogicalHeight()+20);
                break;
            case CENTER_OF_MODEL:
                txtPt = p.getCanvasTextLocation(g, text, p.getLogicalHeight()/2);
                break;
            case RIGHT_OF_MODEL:
                txtPt = p.getCanvasTextLocation(g, text, p.getLogicalHeight());
                if (txtPt!=null)
                {
                    txtPt = new net.runelite.api.Point(txtPt.getX()+10, txtPt.getY()-5);
                }
                break;
            default:
                return;
        }

        if (txtPt!=null)
        {
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            g.setColor(new Color(0,0,0,130));
            g.drawString(text, txtPt.getX()+1, txtPt.getY()+1);

            g.setColor(color);
            g.drawString(text, txtPt.getX(), txtPt.getY());
        }
    }
}
