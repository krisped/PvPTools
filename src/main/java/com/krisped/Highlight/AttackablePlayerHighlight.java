package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.EnumSet;

public class AttackablePlayerHighlight extends BaseHighlight
{
    private static final int IN_WILDERNESS=5963, WILDERNESS_LEVEL=5964;

    public AttackablePlayerHighlight(Client client,
                                     PvPToolsConfig config,
                                     ModelOutlineRenderer modelOutlineRenderer,
                                     SettingsHighlight settingsHighlight)
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableAttackablePlayersHighlight())return;

        Player local=client.getLocalPlayer();
        if(local==null)return;

        for(Player p: client.getPlayers())
        {
            if(p==null||p==local)continue;
            if(isAttackable(local,p))
            {
                Color c= config.attackableColor();
                if(config.attackableOutline()) drawOutline(p,c);
                if(config.attackableHull())    drawHull(g,p,c);
                if(config.attackableTile())    drawTile(g,p,c);

                // Name
                String nameTxt= p.getName()+" ("+p.getCombatLevel()+")";
                PvPToolsConfig.PlayerNameLocation nLoc= config.attackableNameLocation();

                // Label
                String lbTxt= "Attackable";
                PvPToolsConfig.PlayerNameLocation lbLoc= config.attackableLabelLocation();

                LabelColorMode mode= config.attackableLabelColorMode();
                Color lbColor= (mode==LabelColorMode.WHITE)?Color.WHITE:c;

                drawNameAndLabel(g,p,nameTxt,nLoc,c,lbTxt,lbLoc,lbColor);
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableAttackablePlayersHighlight())return;
        Player local=client.getLocalPlayer();
        if(local==null)return;

        for(Player p: client.getPlayers())
        {
            if(p==null||p==local)continue;
            if(isAttackable(local,p))
            {
                drawMinimapDot(g, p, config.attackableColor(), config.attackableMinimapAnimation());
            }
        }
    }

    private boolean isAttackable(Player local, Player other)
    {
        int lc= local.getCombatLevel(), oc= other.getCombatLevel();
        if(client.getVarbitValue(IN_WILDERNESS)==1)
        {
            int w= client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(lc-oc)<= w;
        }
        else if(isPvpWorld())
        {
            int w= client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(lc-oc)<= (15+w);
        }
        return false;
    }

    private boolean isPvpWorld()
    {
        EnumSet<WorldType> t=client.getWorldType();
        return t.contains(WorldType.PVP)|| t.contains(WorldType.HIGH_RISK);
    }
}
