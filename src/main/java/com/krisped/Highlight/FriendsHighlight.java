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
    public void render(Graphics2D graphics)
    {
        if (!config.enableFriendsHighlight()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;

            // er p en friend?
            if (client.isFriended(p.getName(), false))
            {
                renderPlayerHighlight(
                        graphics,
                        p,
                        config.highlightFriendsTile(),
                        config.highlightFriendsOutline(),
                        config.highlightFriendsHull(),
                        config.highlightFriendsMinimap(),
                        config.friendsHighlightColor(),
                        config.friendsMinimapAnimation(),
                        config.friendsNameLocation()
                );
            }
        }
    }
}
