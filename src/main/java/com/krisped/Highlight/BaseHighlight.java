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

    private static final int SAME_LOCATION_OFFSET=40;

    public BaseHighlight(Client client,
                         PvPToolsConfig config,
                         ModelOutlineRenderer modelOutlineRenderer,
                         SettingsHighlight settingsHighlight)
    {
        this.client=client;
        this.config=config;
        this.modelOutlineRenderer=modelOutlineRenderer;
        this.settingsHighlight=settingsHighlight;
    }

    public abstract void renderNormal(Graphics2D g);
    public abstract void renderMinimap(Graphics2D g);

    protected void drawTile(Graphics2D g, Player p, Color color)
    {
        LocalPoint lp= p.getLocalLocation();
        if(lp==null)return;

        Polygon poly= Perspective.getCanvasTilePoly(client, lp);
        if(poly==null)return;

        Stroke old= g.getStroke();
        g.setStroke(new BasicStroke(settingsHighlight.getTileThickness()));
        g.setColor(color);
        g.draw(poly);
        g.setStroke(old);
    }

    protected void drawOutline(Player p, Color color)
    {
        if(p.getModel()==null)return;
        modelOutlineRenderer.drawOutline(
                p,
                settingsHighlight.getOutlineThickness(),
                color,
                255
        );
    }

    protected void drawHull(Graphics2D g, Player p, Color color)
    {
        Shape hull= p.getConvexHull();
        if(hull==null)return;
        Stroke old= g.getStroke();
        g.setStroke(new BasicStroke(settingsHighlight.getHullThickness()));
        g.setColor(color);
        g.draw(hull);
        g.setStroke(old);
    }

    protected void drawNameAndLabel(Graphics2D g,
                                    Player p,
                                    String nameText,
                                    PvPToolsConfig.PlayerNameLocation nameLoc,
                                    Color nameColor,
                                    String labelText,
                                    PvPToolsConfig.PlayerNameLocation labelLoc,
                                    Color labelColor)
    {
        Font nameF = settingsHighlight.getNameFont();
        Font labelF= settingsHighlight.getLabelFont();

        if(labelLoc==nameLoc && labelLoc!= PvPToolsConfig.PlayerNameLocation.DISABLED)
        {
            // label 40 px over name
            drawText(g,p,labelText,labelLoc,labelF,labelColor,0,SAME_LOCATION_OFFSET);
            drawText(g,p,nameText,nameLoc,nameF,nameColor,0,0);
        }
        else
        {
            drawText(g,p,labelText,labelLoc,labelF,labelColor,0,0);
            drawText(g,p,nameText,nameLoc,nameF,nameColor,0,0);
        }
    }

    private void drawText(Graphics2D g,
                          Player p,
                          String text,
                          PvPToolsConfig.PlayerNameLocation loc,
                          Font font,
                          Color color,
                          int offsetX,
                          int offsetY)
    {
        if(loc==PvPToolsConfig.PlayerNameLocation.DISABLED)return;
        if(text==null||text.isEmpty())return;

        g.setFont(font);

        int base;
        switch(loc)
        {
            case ABOVE_HEAD:
                base= p.getLogicalHeight()+60;
                break;
            case CENTER_OF_MODEL:
                base= p.getLogicalHeight()/2;
                break;
            case UNDER_MODEL:
                base=0;
                break;
            default:
                return;
        }

        net.runelite.api.Point pt= p.getCanvasTextLocation(g,text,base);
        if(pt==null)return;

        int dx= pt.getX()+ offsetX;
        int dy= pt.getY()+ offsetY;
        if(loc== PvPToolsConfig.PlayerNameLocation.UNDER_MODEL)
        {
            dy+=20;
        }

        // skygge
        g.setColor(new Color(0,0,0,130));
        g.drawString(text,dx+1,dy+1);

        // farge
        g.setColor(color);
        g.drawString(text,dx,dy);
    }

    // Minimap
    protected void drawMinimapDot(Graphics2D g, Player p, Color color, PvPToolsConfig.MinimapAnimation anim)
    {
        if(anim==PvPToolsConfig.MinimapAnimation.NONE)return;
        net.runelite.api.Point mm= p.getMinimapLocation();
        if(mm==null)return;

        final int baseSize= settingsHighlight.getMinimapCircleSize();
        final long speed= settingsHighlight.getMinimapAnimSpeed();
        int x= mm.getX();
        int y= mm.getY();

        switch(anim)
        {
            case STATIC:
                fillCircle(g,x,y,baseSize,color);break;
            case PULSE:
            {
                long cycle= (System.currentTimeMillis()%speed)/(speed/4);
                int size= baseSize+(int)cycle*2;
                fillCircle(g,x,y,size,color);
                break;
            }
            case BLINK:
            {
                long half=speed/2;
                if((System.currentTimeMillis()%speed)<half)
                {
                    fillCircle(g,x,y,baseSize,color);
                }
                break;
            }
            case SONAR:
            {
                long factor=(System.currentTimeMillis()%(2*speed))/(speed/4);
                int size=baseSize+(int)factor*2;
                drawCircle(g,x,y,size,color);
                break;
            }
            case WAVE:
            {
                double t=(System.currentTimeMillis()%speed)/(double)speed;
                double wave=Math.sin(t*2*Math.PI);
                int radius=(int)(baseSize+ wave*baseSize);
                if(radius<2) radius=2;
                fillCircle(g,x,y,radius,color);
                break;
            }
            default:
                break;
        }
    }

    private void fillCircle(Graphics2D g,int cx,int cy,int size,Color c)
    {
        g.setColor(c);
        g.fillOval(cx-size/2, cy-size/2, size,size);
    }
    private void drawCircle(Graphics2D g,int cx,int cy,int size,Color c)
    {
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),120));
        g.drawOval(cx-size/2, cy-size/2, size,size);
    }
}
