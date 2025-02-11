package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class FriendsHighlight extends BaseHighlight
{
    public FriendsHighlight(Client client,
                            PvPToolsConfig config,
                            ModelOutlineRenderer modelOutlineRenderer,
                            SettingsHighlight settingsHighlight)
    {
        super(client,config,modelOutlineRenderer,settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableFriendsHighlight())return;

        Player local= client.getLocalPlayer();
        if(local==null)return;

        for(Player p: client.getPlayers())
        {
            if(p==null||p==local)continue;
            if(client.isFriended(p.getName(), false))
            {
                Color c= config.friendsHighlightColor();
                if(config.friendsOutline()) drawOutline(p,c);
                if(config.friendsHull())    drawHull(g,p,c);
                if(config.friendsTile())    drawTile(g,p,c);

                String nameTxt= p.getName()+" ("+p.getCombatLevel()+")";
                PvPToolsConfig.PlayerNameLocation nmLoc= config.friendsNameLocation();

                String lbTxt= "Friend";
                PvPToolsConfig.PlayerNameLocation lbLoc= config.friendsLabelLocation();

                LabelColorMode mode= config.friendsLabelColorMode();
                Color lbColor= (mode==LabelColorMode.WHITE)?Color.WHITE:c;

                drawNameAndLabel(g,p,nameTxt,nmLoc,c,lbTxt,lbLoc,lbColor);
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableFriendsHighlight())return;
        Player local= client.getLocalPlayer();
        if(local==null)return;

        for(Player p: client.getPlayers())
        {
            if(p==null||p==local)continue;
            if(client.isFriended(p.getName(), false))
            {
                drawMinimapDot(g,p, config.friendsHighlightColor(), config.friendsMinimapAnimation());
            }
        }
    }
}
