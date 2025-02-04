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
        if (!config.enableChatChannelHighlight())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        Set<String> fcMembers = getFriendsChatMembers();
        if (fcMembers.isEmpty())
        {
            return;
        }

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local)
            {
                continue;
            }

            String nameLc = cleanName(p.getName());
            if (fcMembers.contains(nameLc))
            {
                Color c = config.chatChannelColor();
                boolean showName = (config.chatChannelNameLocation() != PvPToolsConfig.PlayerNameLocation.DISABLED);

                if (config.highlightChatChannelTile())
                {
                    drawTile(g, p, c);
                }
                if (config.highlightChatChannelOutline())
                {
                    drawOutline(p, c);
                }
                if (config.highlightChatChannelHull())
                {
                    drawHull(g, p, c);
                }
                if (showName)
                {
                    String txt = p.getName() + " (" + p.getCombatLevel() + ")";
                    drawName(g, p, txt, c, config.chatChannelNameLocation());
                }
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableChatChannelHighlight())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        Set<String> fcMembers = getFriendsChatMembers();
        if (fcMembers.isEmpty())
        {
            return;
        }

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local)
            {
                continue;
            }

            String nameLc = cleanName(p.getName());
            if (fcMembers.contains(nameLc))
            {
                if (config.highlightChatChannelMinimap())
                {
                    drawMinimapDot(g, p, config.chatChannelColor(), config.chatChannelMinimapAnimation());
                }
            }
        }
    }

    private Set<String> getFriendsChatMembers()
    {
        Set<String> names = new HashSet<>();

        FriendsChatManager manager = client.getFriendsChatManager();
        if (manager == null)
        {
            return names;
        }

        for (FriendsChatMember mem : manager.getMembers())
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
                .replace('\u00A0', ' ')
                .toLowerCase()
                .trim();
    }
}
