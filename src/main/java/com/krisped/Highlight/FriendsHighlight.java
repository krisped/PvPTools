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
                Color c = config.friendsHighlightColor();
                boolean showName = (config.friendsNameLocation() != PvPToolsConfig.PlayerNameLocation.DISABLED);

                if (config.highlightFriendsTile())
                {
                    drawTile(g, p, c);
                }
                if (config.highlightFriendsOutline())
                {
                    drawOutline(p, c);
                }
                if (config.highlightFriendsHull())
                {
                    drawHull(g, p, c);
                }
                if (showName)
                {
                    String txt = p.getName() + " (" + p.getCombatLevel() + ")";
                    drawName(g, p, txt, c, config.friendsNameLocation());
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
                if (config.highlightFriendsMinimap())
                {
                    drawMinimapDot(g, p, config.friendsHighlightColor(), config.friendsMinimapAnimation());
                }
            }
        }
    }
}
