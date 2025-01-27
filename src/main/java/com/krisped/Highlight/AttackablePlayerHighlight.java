package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.EnumSet;

public class AttackablePlayerHighlight extends BaseHighlight
{
    // Varbits for wilderness
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
    public void render(Graphics2D graphics)
    {
        if (!config.enableAttackablePlayers()) return;

        Player local = client.getLocalPlayer();
        if (local == null) return;

        for (Player p : client.getPlayers())
        {
            if (p == null || p == local) continue;
            if (isAttackable(local, p))
            {
                renderPlayerHighlight(
                        graphics,
                        p,
                        config.highlightTileAttackable(),
                        config.highlightOutlineAttackable(),
                        config.highlightHullAttackable(),
                        config.highlightMinimapAttackable(),
                        config.highlightColorAttackable(),
                        config.minimapAnimationAttackable(),
                        config.playerNameLocationAttackable()
                );
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
