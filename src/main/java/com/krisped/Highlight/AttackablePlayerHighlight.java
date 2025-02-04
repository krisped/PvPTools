package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.EnumSet;

public class AttackablePlayerHighlight extends BaseHighlight
{
    private static final int IN_WILDERNESS    = 5963;
    private static final int WILDERNESS_LEVEL = 5964;

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
        if (!config.enableAttackablePlayers())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local)
            {
                continue;
            }
            if (isAttackable(local, p))
            {
                // Tegn tile, outline, hull og evt. navn
                // (men ikke minimap - det tar vi i renderMinimap).
                Color c = config.highlightColorAttackable();
                boolean showName = (config.playerNameLocationAttackable() != PvPToolsConfig.PlayerNameLocation.DISABLED);

                if (config.highlightTileAttackable())
                {
                    drawTile(g, p, c);
                }
                if (config.highlightOutlineAttackable())
                {
                    drawOutline(p, c);
                }
                if (config.highlightHullAttackable())
                {
                    drawHull(g, p, c);
                }
                if (showName)
                {
                    String txt = p.getName() + " (" + p.getCombatLevel() + ")";
                    drawName(g, p, txt, c, config.playerNameLocationAttackable());
                }
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if (!config.enableAttackablePlayers())
        {
            return;
        }

        Player local = client.getLocalPlayer();
        if (local == null)
        {
            return;
        }

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local)
            {
                continue;
            }
            if (isAttackable(local, p))
            {
                if (config.highlightMinimapAttackable())
                {
                    drawMinimapDot(g, p, config.highlightColorAttackable(), config.minimapAnimationAttackable());
                }
            }
        }
    }

    private boolean isAttackable(Player local, Player other)
    {
        int localCombat = local.getCombatLevel();
        int otherCombat = other.getCombatLevel();

        // wilderness
        if (client.getVarbitValue(IN_WILDERNESS) == 1)
        {
            int wLvl = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= wLvl;
        }
        // pvp world
        else if (isPvpWorld())
        {
            int wLvl = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= (15 + wLvl);
        }
        return false;
    }

    private boolean isPvpWorld()
    {
        EnumSet<WorldType> types = client.getWorldType();
        return types.contains(WorldType.PVP) || types.contains(WorldType.HIGH_RISK);
    }
}
