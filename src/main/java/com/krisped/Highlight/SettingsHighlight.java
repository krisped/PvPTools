package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;

/**
 * Henter globale highlight-innstillinger (tile/outline/hull thickness,
 * pluss minimap-størrelse og anim-hastighet).
 *
 * minmapAnimSpeed: Bruker config-verdi 1..10, vi ganger med 200 => 200..2000ms
 */
public class SettingsHighlight
{
    private final PvPToolsConfig config;

    public SettingsHighlight(PvPToolsConfig config)
    {
        this.config = config;
    }

    public int getTileThickness()
    {
        return config.tileThickness();
    }

    public int getOutlineThickness()
    {
        return config.outlineThickness();
    }

    public int getHullThickness()
    {
        return config.hullThickness();
    }

    /**
     * Minimapsirkel base-størrelse i piksler
     */
    public int getMinimapCircleSize()
    {
        return config.minimapCircleSize();
    }

    /**
     * Ganger config-verdi (1..10) med 200 => 200..2000 ms
     */
    public int getMinimapAnimSpeed()
    {
        return config.minimapAnimSpeed() * 200;
    }
}
