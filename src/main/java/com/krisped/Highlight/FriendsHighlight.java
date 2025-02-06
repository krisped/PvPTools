package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
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
        super(client, config, modelOutlineRenderer, settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if (!config.enableFriendsHighlight()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;
            if (client.isFriended(p.getName(), false))
            {
                // Name
                String nameTxt = p.getName() + " (" + p.getCombatLevel() + ")";
                PvPToolsConfig.PlayerNameLocation nameLoc = config.friendsNameLocation();

                // Label
                String labelTxt = "Friend";
                PvPToolsConfig.PlayerNameLocation labelLoc = config.friendsLabelLocation();

                drawNameAndLabel(g, p, nameTxt, nameLoc, config.friendsHighlightColor(), labelTxt, labelLoc);

                // Outline
                if (config.friendsOutline())
                {
                    drawOutline(p, config.friendsHighlightColor());
                }

                // Hull
                if (config.friendsHull())
                {
                    drawHull(g, p, config.friendsHighlightColor());
                }

                // Tile
                if (config.friendsTile())
                {
                    drawTile(g, p, config.friendsHighlightColor());
                }
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableFriendsHighlight()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;
            if (client.isFriended(p.getName(), false))
            {
                drawMinimapDot(g, p, config.friendsHighlightColor(), config.friendsMinimapAnimation());
            }
        }
    }
}
