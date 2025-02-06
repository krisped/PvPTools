package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Perspective;
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

    // Kalles i overlay UNDER_WIDGETS
    public abstract void renderNormal(Graphics2D g);

    // Kalles i overlay ABOVE_WIDGETS
    public abstract void renderMinimap(Graphics2D g);

    // ----------------------------------------------------
    // Tegningshjelp (tile/outline/hull)
    // ----------------------------------------------------
    protected void drawTile(Graphics2D g, Player p, Color color)
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

    protected void drawOutline(Player p, Color color)
    {
        if (p.getModel() == null) return;
        modelOutlineRenderer.drawOutline(
                p,
                settingsHighlight.getOutlineThickness(),
                color,
                255
        );
    }

    // Ingen fyll, kun ring
    protected void drawHull(Graphics2D g, Player p, Color color)
    {
        Shape hull = p.getConvexHull();
        if (hull == null) return;

        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(settingsHighlight.getHullThickness()));
        g.setColor(color);
        g.draw(hull);
        g.setStroke(old);
    }

    // ----------------------------------------------------
    // 1) Felles metode: Tegn label og name uten overlapping
    // ----------------------------------------------------
    protected void drawNameAndLabel(Graphics2D g,
                                    Player p,
                                    String nameString,
                                    PvPToolsConfig.PlayerNameLocation nameLoc,
                                    Color highlightColor,
                                    String labelString,
                                    PvPToolsConfig.PlayerNameLocation labelLoc)
    {
        // Fonter
        Font nameF  = settingsHighlight.getNameFont();
        Font labelF = settingsHighlight.getLabelFont();

        // Label‐farge (WHITE eller highlightColor)
        Color labelColor = settingsHighlight.getLabelColor(highlightColor);

        // NB: Label vises “over” name hvis location er lik
        if (labelLoc == nameLoc && labelLoc != PvPToolsConfig.PlayerNameLocation.DISABLED)
        {
            // Label over name => trekk label litt lenger opp
            drawText(g, p, labelString, labelLoc, labelF, labelColor, 25);
            drawText(g, p, nameString, nameLoc, nameF, highlightColor, 0);
        }
        else
        {
            // Ulike loc eller en er DISABLED => bare tegn hver for seg
            drawText(g, p, labelString, labelLoc, labelF, labelColor, 0);
            drawText(g, p, nameString, nameLoc, nameF, highlightColor, 0);
        }
    }

    // ----------------------------------------------------
    // 2) Intern metode: Tegn en tekst (med offset)
    // ----------------------------------------------------
    private void drawText(Graphics2D g,
                          Player p,
                          String text,
                          PvPToolsConfig.PlayerNameLocation loc,
                          Font font,
                          Color color,
                          int offsetY)
    {
        if (loc == PvPToolsConfig.PlayerNameLocation.DISABLED || text == null || text.isEmpty())
        {
            return;
        }

        g.setFont(font);

        int textBase;
        switch (loc)
        {
            case ABOVE_HEAD:
                textBase = p.getLogicalHeight() + 40 + offsetY;
                break;
            case CENTER_OF_MODEL:
                textBase = p.getLogicalHeight() / 2 + offsetY;
                break;
            case UNDER_MODEL:
                // litt under føttene
                textBase = offsetY;
                break;
            default:
                return;
        }

        net.runelite.api.Point txtPt = p.getCanvasTextLocation(g, text, textBase);
        if (txtPt == null) return;

        if (loc == PvPToolsConfig.PlayerNameLocation.UNDER_MODEL)
        {
            // skyver litt ekstra ned
            txtPt = new net.runelite.api.Point(txtPt.getX(), txtPt.getY() + 20);
        }

        // Skugge
        g.setColor(new Color(0,0,0,130));
        g.drawString(text, txtPt.getX()+1, txtPt.getY()+1);

        // Farge
        g.setColor(color);
        g.drawString(text, txtPt.getX(), txtPt.getY());
    }

    // ----------------------------------------------------
    // Minimap
    // ----------------------------------------------------
    protected void drawMinimapDot(Graphics2D g, Player p, Color color, PvPToolsConfig.MinimapAnimation anim)
    {
        if (anim == PvPToolsConfig.MinimapAnimation.NONE)
        {
            return; // tegner ingenting
        }

        net.runelite.api.Point mmLoc = p.getMinimapLocation();
        if (mmLoc == null) return;

        final int baseSize = settingsHighlight.getMinimapCircleSize();
        final long speed   = settingsHighlight.getMinimapAnimSpeed();
        int x = mmLoc.getX();
        int y = mmLoc.getY();

        switch (anim)
        {
            case STATIC:
                fillCircle(g, x, y, baseSize, color);
                break;
            case PULSE:
            {
                long cycle = (System.currentTimeMillis() % speed) / (speed/4);
                int size = baseSize + (int)cycle * 2;
                fillCircle(g, x, y, size, color);
                break;
            }
            case BLINK:
            {
                long halfCycle = speed / 2;
                if ((System.currentTimeMillis() % speed) < halfCycle)
                {
                    fillCircle(g, x, y, baseSize, color);
                }
                break;
            }
            case SONAR:
            {
                long factor = (System.currentTimeMillis() % (2*speed)) / (speed/4);
                int size = baseSize + (int)factor * 2;
                drawCircle(g, x, y, size, color);
                break;
            }
            case WAVE:
            {
                double t = (System.currentTimeMillis() % speed) / (double)speed;
                double wave = Math.sin(t * 2 * Math.PI);
                int radius = (int)(baseSize + wave * baseSize);
                if (radius < 2) radius = 2;
                fillCircle(g, x, y, radius, color);
                break;
            }
            default:
                break;
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
}
