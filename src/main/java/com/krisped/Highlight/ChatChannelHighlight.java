package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ChatChannelHighlight extends BaseHighlight
{
    public ChatChannelHighlight(Client client,
                                PvPToolsConfig config,
                                ModelOutlineRenderer modelOutlineRenderer,
                                SettingsHighlight settingsHighlight)
    {
        super(client,config,modelOutlineRenderer,settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableChatChannelHighlight())return;

        Player local= client.getLocalPlayer();
        if(local==null)return;

        Set<String> fc= getFriendsChatMembers();
        if(fc.isEmpty())return;

        for(Player p: client.getPlayers())
        {
            if(p==null||p==local)continue;
            String cName= cleanName(p.getName());
            if(fc.contains(cName))
            {
                Color c= config.chatChannelColor();
                if(config.chatChannelOutline()) drawOutline(p,c);
                if(config.chatChannelHull())    drawHull(g,p,c);
                if(config.chatChannelTile())    drawTile(g,p,c);

                String nameTxt= p.getName()+" ("+p.getCombatLevel()+")";
                PvPToolsConfig.PlayerNameLocation nLoc= config.chatChannelNameLocation();

                String lbTxt= "Chat Channel";
                PvPToolsConfig.PlayerNameLocation lbLoc= config.chatChannelLabelLocation();

                LabelColorMode mode= config.chatChannelLabelColorMode();
                Color lbColor= (mode== LabelColorMode.WHITE)?Color.WHITE:c;

                drawNameAndLabel(g,p,nameTxt,nLoc,c,lbTxt,lbLoc,lbColor);
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableChatChannelHighlight())return;

        Player local=client.getLocalPlayer();
        if(local==null)return;

        Set<String> fc= getFriendsChatMembers();
        if(fc.isEmpty())return;

        for(Player p: client.getPlayers())
        {
            if(p==null||p==local)continue;
            String cName= cleanName(p.getName());
            if(fc.contains(cName))
            {
                drawMinimapDot(g,p, config.chatChannelColor(), config.chatChannelMinimapAnimation());
            }
        }
    }

    private Set<String> getFriendsChatMembers()
    {
        Set<String> set= new HashSet<>();
        FriendsChatManager mgr= client.getFriendsChatManager();
        if(mgr==null)return set;

        for(FriendsChatMember mem: mgr.getMembers())
        {
            if(mem==null)continue;
            String cc= cleanName(mem.getName());
            if(!cc.isEmpty()) set.add(cc);
        }
        return set;
    }

    private String cleanName(String raw)
    {
        if(raw==null)return"";
        return raw.replaceAll("<.*?>","")
                .replace('\u00A0',' ')
                .toLowerCase().trim();
    }
}
