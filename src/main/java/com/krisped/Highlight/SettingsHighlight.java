package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;

/**
 * Holder globale highlight-innstillinger, f.eks. outline-tykkelse.
 * Henter verdien fra "Settings Highlight" i PvPToolsConfig.
 */
public class SettingsHighlight
{
    private final PvPToolsConfig config;

    public SettingsHighlight(PvPToolsConfig config)
    {
        this.config = config;
    }

    public int getOutlineThickness()
    {
        return config.outlineThickness();
    }
}
