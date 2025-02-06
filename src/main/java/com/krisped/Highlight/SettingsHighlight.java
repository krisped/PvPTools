package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class SettingsHighlight
{
    private final PvPToolsConfig config;

    // Cache for de genererte fontene (slik vi slipper lage dem hver gang)
    private Font cachedNameFont = null;
    private Font cachedLabelFont = null;

    public SettingsHighlight(PvPToolsConfig config)
    {
        this.config = config;
    }

    // --------------------------
    //  Sliders
    // --------------------------
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

    public int getMinimapCircleSize()
    {
        return config.minimapCircleSize();
    }

    public int getMinimapAnimSpeed()
    {
        // multipliser 1..15 med 200 => 200..3000 ms
        return config.minimapAnimSpeed() * 200;
    }

    // --------------------------
    //  Name Font (bygger en Font + optional underline)
    // --------------------------
    public Font getNameFont()
    {
        if (cachedNameFont == null)
        {
            cachedNameFont = buildFont(
                    config.nameFont(),
                    config.nameBold(),
                    config.nameItalic(),
                    config.nameUnderline()
            );
        }
        return cachedNameFont;
    }

    // --------------------------
    //  Label Font
    // --------------------------
    public Font getLabelFont()
    {
        if (cachedLabelFont == null)
        {
            cachedLabelFont = buildFont(
                    config.labelFont(),
                    config.labelBold(),
                    config.labelItalic(),
                    config.labelUnderline()
            );
        }
        return cachedLabelFont;
    }

    /**
     * Returner farge for kategori‐label:
     *  - Hvis labelColorMode = WHITE => Color.WHITE
     *  - Ellers => bruk highlightfarge
     */
    public Color getLabelColor(Color highlightColor)
    {
        switch (config.labelColorMode())
        {
            case WHITE:
                return Color.WHITE;
            case CATEGORY_COLOR:
            default:
                return highlightColor;
        }
    }

    /**
     * Bygger en Java‐Font + optional underline via TextAttribute
     */
    private Font buildFont(PvPToolsConfig.NameFont fontChoice,
                           boolean bold,
                           boolean italic,
                           boolean underline)
    {
        // 1) Fontnavn
        String fontName = mapFontName(fontChoice);

        // 2) style (PLAIN=0, BOLD=1, ITALIC=2, BOLD+ITALIC=3)
        int style = Font.PLAIN;
        if (bold && italic)
        {
            style = Font.BOLD | Font.ITALIC; // 3
        }
        else if (bold)
        {
            style = Font.BOLD; //1
        }
        else if (italic)
        {
            style = Font.ITALIC; //2
        }

        // Start med en basic Font (size=12)
        Font f = new Font(fontName, style, 12);

        // 3) Underline via TextAttribute
        if (underline)
        {
            Map<TextAttribute, Object> map = new HashMap<>();
            map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            f = f.deriveFont(map);
        }
        return f;
    }

    /**
     * Map enum til faktisk fontnavn systemet forstår
     */
    private String mapFontName(PvPToolsConfig.NameFont f)
    {
        switch (f)
        {
            case CALIBRI:          return "Calibri";
            case VERDANA:          return "Verdana";
            case TAHOMA:           return "Tahoma";
            case CONSOLAS:         return "Consolas";
            case COMIC_SANS:       return "Comic Sans MS";
            case TIMES_NEW_ROMAN:  return "Times New Roman";
            case DIALOG:           return "Dialog";
            case MONOSPACED:       return "Monospaced";
            case SERIF:            return "Serif";
            default:
                // ARIAL
                return "Arial";
        }
    }

    /**
     * Slett cache (kalles f.eks. ved config endring)
     */
    public void resetFontCache()
    {
        cachedNameFont = null;
        cachedLabelFont = null;
    }
}
