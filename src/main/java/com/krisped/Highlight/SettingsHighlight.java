package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class SettingsHighlight
{
    private final PvPToolsConfig config;
    private Font cachedNameFont=null;
    private Font cachedLabelFont=null;

    public SettingsHighlight(PvPToolsConfig config)
    {
        this.config = config;
    }

    // Sliders
    public int getTileThickness() {return config.tileThickness();}
    public int getOutlineThickness() {return config.outlineThickness();}
    public int getHullThickness() {return config.hullThickness();}
    public int getMinimapCircleSize() {return config.minimapCircleSize();}
    public int getMinimapAnimSpeed() {return config.minimapAnimSpeed()*200;}

    // Name
    public Font getNameFont()
    {
        if(cachedNameFont==null)
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

    // Label
    public Font getLabelFont()
    {
        if(cachedLabelFont==null)
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

    public void resetFontCache()
    {
        cachedNameFont=null;
        cachedLabelFont=null;
    }

    private Font buildFont(PvPToolsConfig.NameFont f, boolean bold, boolean italic, boolean underline)
    {
        String fname= mapFontName(f);
        int style= Font.PLAIN;
        if(bold && italic) style= Font.BOLD|Font.ITALIC;
        else if(bold) style= Font.BOLD;
        else if(italic) style= Font.ITALIC;

        Font base=new Font(fname, style, 12);

        if(underline)
        {
            Map<TextAttribute,Object> map=new HashMap<>();
            map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            base= base.deriveFont(map);
        }
        return base;
    }

    private String mapFontName(PvPToolsConfig.NameFont f)
    {
        switch(f)
        {
            case CALIBRI: return "Calibri";
            case VERDANA: return "Verdana";
            case TAHOMA: return "Tahoma";
            case CONSOLAS:return "Consolas";
            case COMIC_SANS:return "Comic Sans MS";
            case TIMES_NEW_ROMAN:return "Times New Roman";
            case DIALOG:return "Dialog";
            case MONOSPACED:return "Monospaced";
            case SERIF:return "Serif";
            default:
                return "Arial";
        }
    }
}
