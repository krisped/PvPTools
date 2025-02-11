package com.krisped.Highlight;

import com.krisped.PvPToolsConfig;
import com.krisped.PvPToolsConfig.LabelColorMode;
import com.krisped.PvPToolsConfig.TagMenuOption;
import net.runelite.api.*;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class TagPlayerHighlight extends BaseHighlight
{
    private final ConfigManager configManager;
    private EventBus eventBus;
    private boolean eventsRegistered=false;

    public TagPlayerHighlight(
            Client client,
            PvPToolsConfig config,
            ModelOutlineRenderer modelOutlineRenderer,
            SettingsHighlight settingsHighlight,
            ConfigManager configManager
    )
    {
        super(client, config, modelOutlineRenderer, settingsHighlight);
        this.configManager= configManager;
    }

    public void registerEvents(EventBus eventBus)
    {
        if(!eventsRegistered)
        {
            this.eventBus= eventBus;
            eventBus.register(this);
            eventsRegistered=true;
        }
    }

    public void unregisterEvents()
    {
        if(eventsRegistered && eventBus!=null)
        {
            eventBus.unregister(this);
            eventBus=null;
            eventsRegistered=false;
        }
    }

    @Override
    public void renderNormal(Graphics2D g)
    {
        if(!config.enableTagPlayerHighlight())return;

        Set<String> tset= parseTaggedPlayersList();
        for(Player p: client.getPlayers())
        {
            if(p==null)continue;
            String nm= cleanNameForList(p.getName());
            if(tset.contains(nm))
            {
                Color c= config.tagHighlightColor();
                if(config.tagOutline()) drawOutline(p,c);
                if(config.tagHull())    drawHull(g,p,c);
                if(config.tagTile())    drawTile(g,p,c);

                String nameTxt= p.getName()+" ("+p.getCombatLevel()+")";
                PvPToolsConfig.PlayerNameLocation nLoc= config.tagNameLocation();

                String lbTxt= "Tagged";
                PvPToolsConfig.PlayerNameLocation lbLoc= config.tagLabelLocation();

                LabelColorMode mode= config.tagLabelColorMode();
                Color lbColor= (mode== LabelColorMode.WHITE)? Color.WHITE: c;

                drawNameAndLabel(g,p,
                        nameTxt,nLoc,c,
                        lbTxt,lbLoc,lbColor
                );
            }
        }
    }

    @Override
    public void renderMinimap(Graphics2D g)
    {
        if(!config.enableTagPlayerHighlight())return;

        Set<String> tset= parseTaggedPlayersList();
        for(Player p: client.getPlayers())
        {
            if(p==null)continue;
            String nm= cleanNameForList(p.getName());
            if(tset.contains(nm))
            {
                drawMinimapDot(g,p, config.tagHighlightColor(), config.tagMinimapAnimation());
            }
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded e)
    {
        if(!config.enableTagPlayerHighlight())return;

        TagMenuOption opt= config.tagMenuOption();
        if(opt== TagMenuOption.OFF) return;

        // NB: CUSTOM_RIGHT_CLICK => ingen SHIFT-løsning her. Gjør ingenting.
        if(opt== TagMenuOption.CUSTOM_RIGHT_CLICK)
        {
            // Skipper. (Ingen SHIFT)
            return;
        }

        // RIGHT_CLICK => vis "Tag"
        MenuEntry me= e.getMenuEntry();
        if(me==null|| me.getOption()==null) return;

        if(me.getOption().equalsIgnoreCase("Trade with"))
        {
            Color c= config.tagHighlightColor();
            String col= String.format("<col=%02X%02X%02X>", c.getRed(), c.getGreen(), c.getBlue());
            client.createMenuEntry(client.getMenuEntries().length)
                    .setOption(col+"Tag</col>")
                    .setTarget(me.getTarget())
                    .setType(MenuAction.RUNELITE);
        }
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked e)
    {
        if(!config.enableTagPlayerHighlight())return;

        TagMenuOption opt= config.tagMenuOption();
        if(opt== TagMenuOption.OFF) return;
        if(opt== TagMenuOption.CUSTOM_RIGHT_CLICK)
        {
            // Vi gjør ingenting.
            return;
        }

        // RIGHT_CLICK
        String rawOp= e.getMenuOption();
        if(rawOp==null)return;
        String noCol= rawOp.replaceAll("<[^>]*>","").trim();
        if(!noCol.equalsIgnoreCase("Tag"))return;

        String rawTarget= e.getMenuTarget();
        if(rawTarget==null) rawTarget="";

        String cl= cleanNameForList(rawTarget);
        addPlayerToTaggedList(cl);
    }

    private Set<String> parseTaggedPlayersList()
    {
        Set<String> set= new HashSet<>();
        String raw= config.taggedPlayersList();
        if(raw==null|| raw.trim().isEmpty()) return set;

        String[] lines= raw.split("\\r?\\n");
        for(String ln: lines)
        {
            String nm= cleanNameForList(ln);
            if(!nm.isEmpty()) set.add(nm);
        }
        return set;
    }

    private void addPlayerToTaggedList(String name)
    {
        if(name.isEmpty())return;

        Set<String> all= parseTaggedPlayersList();
        if(all.contains(name))return;

        String exist= config.taggedPlayersList();
        if(exist==null) exist="";
        String newVal= exist.isEmpty()? name : exist+"\n"+ name;

        configManager.setConfiguration("pvptools","taggedPlayersList", newVal);
    }

    private String cleanNameForList(String raw)
    {
        if(raw==null)return"";
        String noC= raw.replaceAll("<.*?>","");
        String noL= noC.replaceAll("\\(level-\\d+\\)","");
        String rep= noL.replace('\u00A0',' ');
        return rep.trim().toLowerCase();
    }
}
