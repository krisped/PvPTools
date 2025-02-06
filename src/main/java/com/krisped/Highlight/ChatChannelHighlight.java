package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
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
        super(client, config, modelOutlineRenderer, settingsHighlight);
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if (!config.enableChatChannelHighlight()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        Set<String> fcMembers = getFriendsChatMembers();
        if (fcMembers.isEmpty()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;

            String clean = cleanName(p.getName());
            if (fcMembers.contains(clean))
            {
                // Name
                String nameTxt = p.getName() + " (" + p.getCombatLevel() + ")";
                PvPToolsConfig.PlayerNameLocation nameLoc = config.chatChannelNameLocation();

                // Label
                String labelTxt = "Chat Channel";
                PvPToolsConfig.PlayerNameLocation labelLoc = config.chatChannelLabelLocation();

                drawNameAndLabel(g, p, nameTxt, nameLoc, config.chatChannelColor(), labelTxt, labelLoc);

                // Outline
                if (config.chatChannelOutline())
                {
                    drawOutline(p, config.chatChannelColor());
                }

                // Hull
                if (config.chatChannelHull())
                {
                    drawHull(g, p, config.chatChannelColor());
                }

                // Tile
                if (config.chatChannelTile())
                {
                    drawTile(g, p, config.chatChannelColor());
                }
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableChatChannelHighlight()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        Set<String> fcMembers = getFriendsChatMembers();
        if (fcMembers.isEmpty()) return;

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;

            String clean = cleanName(p.getName());
            if (fcMembers.contains(clean))
            {
                drawMinimapDot(g, p, config.chatChannelColor(), config.chatChannelMinimapAnimation());
            }
        }
    }

    private Set<String> getFriendsChatMembers()
    {
        Set<String> names = new HashSet<>();
        FriendsChatManager mgr = client.getFriendsChatManager();
        if (mgr == null) return names;

        for (FriendsChatMember mem : mgr.getMembers())
        {
            if (mem == null) continue;
            String c = cleanName(mem.getName());
            if (!c.isEmpty())
            {
                names.add(c);
            }
        }
        return names;
    }

    private String cleanName(String raw)
    {
        if (raw == null) return "";
        return raw.replaceAll("<.*?>", "")
                .replace('\u00A0',' ')
                .toLowerCase()
                .trim();
    }
}
