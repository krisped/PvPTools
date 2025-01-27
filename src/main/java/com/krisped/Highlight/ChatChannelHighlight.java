package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Highlighter spillere i "Friends Chat" (2 gule smileys),
 * ofte kalt "Chat-channel" i OSRS.
 *
 * Bruker FriendsChatManager i eldre RuneLite, i stedet for FriendsChat.
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
        // Sjekk config: er chat channel highlight aktivert?
        if (!config.enableChatChannelHighlight())
        {
            return;
        }

        // Må ha en local player
        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        // Hent navneliste fra "FriendsChat"
        Set<String> fcMembers = getFriendsChatMembers();
        if (fcMembers.isEmpty())
        {
            return;
        }

        // Loop over players i spillet
        for (Player p : client.getPlayers())
        {
            if (p == null || p == local)
            {
                continue;
            }

            String nameLc = cleanName(p.getName());
            if (fcMembers.contains(nameLc))
            {
                // Kall fellesmetoden i BaseHighlight
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
     * Henter alle medlemmer i "Friends Chat" (2 gule smileys)
     * via FriendsChatManager (eldre RL-versjon).
     *
     * Returnerer et Set<String> i lowercase.
     */
    private Set<String> getFriendsChatMembers()
    {
        Set<String> names = new HashSet<>();

        // Hent manager
        FriendsChatManager manager = client.getFriendsChatManager();
        if (manager == null)
        {
            return names;
        }

        // manager.getMembers() gir en liste av FriendsChatMember
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

    /**
     * Fjerner fargekoder, NBSP og senker til lowerCase()
     */
    private String cleanName(String raw)
    {
        if (raw == null) return "";
        return raw.replaceAll("<.*?>","")
                .replace('\u00A0',' ')
                .toLowerCase()
                .trim();
    }
}
