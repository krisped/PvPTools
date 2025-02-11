package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import net.runelite.api.Client;
import net.runelite.api.Ignore;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class IgnoreHighlight extends BaseHighlight
{
    public IgnoreHighlight(Client client,
                           PvPToolsConfig config,
                           ModelOutlineRenderer modelOutlineRenderer,
                           SettingsHighlight settingsHighlight)
    {
        super(client,config,modelOutlineRenderer,settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableIgnoreHighlight())return;

        for(Player p: client.getPlayers())
        {
            if(p==null)continue;
            if(isIgnored(p.getName()))
            {
                Color c= config.ignoreHighlightColor();
                if(config.ignoreOutline()) drawOutline(p,c);
                if(config.ignoreHull())    drawHull(g,p,c);
                if(config.ignoreTile())    drawTile(g,p,c);

                String nameTxt= p.getName()+" ("+p.getCombatLevel()+")";
                PvPToolsConfig.PlayerNameLocation nmLoc= config.ignoreNameLocation();

                String lbTxt= "Ignored";
                PvPToolsConfig.PlayerNameLocation lbLoc= config.ignoreLabelLocation();

                LabelColorMode mode= config.ignoreLabelColorMode();
                Color lbColor= (mode==LabelColorMode.WHITE)?Color.WHITE:c;

                drawNameAndLabel(g,p,nameTxt,nmLoc,c,lbTxt,lbLoc,lbColor);
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableIgnoreHighlight())return;
        for(Player p: client.getPlayers())
        {
            if(p==null)continue;
            if(isIgnored(p.getName()))
            {
                drawMinimapDot(g,p, config.ignoreHighlightColor(), config.ignoreMinimapAnimation());
            }
        }
    }

    private boolean isIgnored(String name)
    {
        if(name==null||client.getIgnoreContainer()==null)return false;
        Ignore i= client.getIgnoreContainer().findByName(name);
        return (i!=null);
    }
}
