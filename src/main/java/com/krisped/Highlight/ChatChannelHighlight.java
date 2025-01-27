package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.*;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Highlighter spillere som er i samme "chat channel" (clan) som deg,
 * forutsatt at "enableChatChannelHighlight" er true.
 */
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
    public void render(Graphics2D graphics)
    {
        if (!config.enableChatChannelHighlight())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null) return;

        // Hent medlemmene i clan channel
        Set<String> clanMemberNames = getClanMemberNames();
        if (clanMemberNames.isEmpty())
        {
            return;
        }

        // Loop over players og highlight de som er i clanMemberNames
        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;

            if (clanMemberNames.contains(cleanName(p.getName())))
            {
                renderPlayerHighlight(
                        graphics,
                        p,
                        config.highlightChatChannelTile(),
                        config.highlightChatChannelOutline(),
                        config.highlightChatChannelHull(),
                        config.highlightChatChannelMinimap(),
                        config.chatChannelColor(),
                        config.chatChannelMinimapAnimation(),
                        config.chatChannelNameLocation()
                );
            }
        }
    }

    /**
     * Eksempel-metode for å hente medlemmene i "main clan channel".
     * Juster hvis du har flere (f.eks. ClanID.CLAN, ClanID.GROUP_IRONMAN, etc.).
     */
    private Set<String> getClanMemberNames()
    {
        Set<String> names = new HashSet<>();

        ClanChannel clanChannel = client.getClanChannel();
        if (clanChannel == null)
        {
            return names;
        }

        for (ClanChannelMember member : clanChannel.getMembers())
        {
            String nm = cleanName(member.getName());
            names.add(nm);
        }

        return names;
    }

    private String cleanName(String n)
    {
        if (n == null) return "";
        return n.replace('\u00A0', ' ').toLowerCase().trim(); // fjerner ev. tags og gjør lowerCase
    }
}
