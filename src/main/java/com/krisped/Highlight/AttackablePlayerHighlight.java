package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
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
    public void render(Graphics2D graphics)
    {
        if (!config.enableAttackablePlayers())
        {
            return;
        }

        var local = client.getLocalPlayer();
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

    private boolean isAttackable(Player localPlayer, Player otherPlayer)
    {
        int localCombat = localPlayer.getCombatLevel();
        int otherCombat = otherPlayer.getCombatLevel();

        // Wilderness
        if (client.getVarbitValue(IN_WILDERNESS) == 1)
        {
            int wildernessLevel = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= wildernessLevel;
        }
        // PVP/HIGH_RISK
        else if (isPvpWorld())
        {
            int wildernessLevel = client.getVarbitValue(WILDERNESS_LEVEL);
            return Math.abs(localCombat - otherCombat) <= (15 + wildernessLevel);
        }
        return false;
    }

    private boolean isPvpWorld()
    {
        EnumSet<WorldType> wt = client.getWorldType();
        return wt.contains(WorldType.PVP) || wt.contains(WorldType.HIGH_RISK);
    }
}
