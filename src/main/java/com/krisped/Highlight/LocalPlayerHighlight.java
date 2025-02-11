package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class LocalPlayerHighlight extends BaseHighlight
{
    public LocalPlayerHighlight(Client client,
                                PvPToolsConfig config,
                                ModelOutlineRenderer modelOutlineRenderer,
                                SettingsHighlight settingsHighlight)
    {
        super(client,config,modelOutlineRenderer,settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableLocalPlayerHighlight())return;

        Player local= client.getLocalPlayer();
        if(local==null)return;

        // (2) Color
        Color c= config.localPlayerColor();

        // (3) Outline
        if(config.localPlayerOutline()) drawOutline(local,c);

        // (4) Hull
        if(config.localPlayerHull()) drawHull(g,local,c);

        // (5) Tile
        if(config.localPlayerTile()) drawTile(g,local,c);

        // (7) Name
        String nameTxt= local.getName()+" ("+local.getCombatLevel()+")";
        PvPToolsConfig.PlayerNameLocation nameLoc= config.localPlayerNameLocation();

        // (8) Label
        String labelTxt= "Local Player";
        PvPToolsConfig.PlayerNameLocation labelLoc= config.localPlayerLabelLocation();

        // label color mode
        LabelColorMode mode= config.localLabelColorMode();
        Color lblColor= (mode==LabelColorMode.WHITE)? Color.WHITE: c;

        drawNameAndLabel(g,
                local,
                nameTxt, nameLoc, c,
                labelTxt, labelLoc, lblColor
        );
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableLocalPlayerHighlight())return;
        // (6) Minimap
        Player local= client.getLocalPlayer();
        if(local==null)return;

        drawMinimapDot(g, local, config.localPlayerColor(), config.localPlayerMinimapAnimation());
    }
}
