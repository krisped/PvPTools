package com.krisped.PlayerLookupPanel;

import net.runelite.api.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.hiscore.HiscoreClient;
import net.runelite.client.hiscore.HiscoreResult;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.QuantityFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class PlayerLookup extends JPanel
{
    private final Client client;
    private final ClientThread clientThread;
    private final HiscoreClient hiscoreClient;
    private final ItemManager itemManager;
    private final SpriteManager spriteManager;

    private final JLabel playerNameLabel;
    private final JPanel hiscorePanel;
    private final JPanel equipmentPanel;

    private Runnable onBackButtonPressed;
    private String currentTargetName = null;

    public PlayerLookup(Client client,
                        ClientThread clientThread,
                        HiscoreClient hiscoreClient,
                        ItemManager itemManager,
                        SpriteManager spriteManager)
    {
        this.client = client;
        this.clientThread = clientThread;
        this.hiscoreClient = hiscoreClient;
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Target Information");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(Box.createVerticalStrut(30), BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);

        // PlayerName panel
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        namePanel.setBorder(BorderFactory.createTitledBorder("Player Name"));

        playerNameLabel = new JLabel("No target selected", SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerNameLabel.setForeground(Color.RED);
        namePanel.add(playerNameLabel, BorderLayout.CENTER);

        JButton clearButton = new JButton("Clear Target");
        clearButton.setForeground(Color.WHITE);
        clearButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        clearButton.addActionListener(e -> clearTarget());
        namePanel.add(clearButton, BorderLayout.SOUTH);

        // Hiscore
        hiscorePanel = new JPanel();
        hiscorePanel.setLayout(new BoxLayout(hiscorePanel, BoxLayout.Y_AXIS));
        hiscorePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        hiscorePanel.setBorder(BorderFactory.createTitledBorder("Hiscores"));
        JScrollPane hiscoreScroll = new JScrollPane(hiscorePanel);
        hiscoreScroll.setPreferredSize(new Dimension(300, 180));
        hiscoreScroll.setBorder(null);

        // Equipment
        equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.Y_AXIS));
        equipmentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        equipmentPanel.setBorder(BorderFactory.createTitledBorder("Equipment"));
        JScrollPane equipScroll = new JScrollPane(equipmentPanel);
        equipScroll.setPreferredSize(new Dimension(300, 600)); // Forstørret
        equipScroll.setBorder(null);

        // Back
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            if (onBackButtonPressed != null)
            {
                onBackButtonPressed.run();
            }
        });

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        contentPanel.add(hiscoreScroll);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(equipScroll);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(backButton);

        add(namePanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.SOUTH);

        monitorInteraction();
    }

    public void setOnBackButtonPressed(Runnable onBackButtonPressed)
    {
        this.onBackButtonPressed = onBackButtonPressed;
    }

    private void monitorInteraction()
    {
        new Thread(() -> {
            while (true)
            {
                try
                {
                    updateTarget();
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void updateTarget()
    {
        Player local = client.getLocalPlayer();
        if (local == null) return;

        if (local.getInteracting() instanceof Player)
        {
            Player target = (Player) local.getInteracting();
            if (target != null && target.getName() != null)
            {
                if (!target.getName().equals(currentTargetName))
                {
                    currentTargetName = target.getName();
                    SwingUtilities.invokeLater(() -> {
                        playerNameLabel.setText(currentTargetName);
                        playerNameLabel.setForeground(Color.GREEN);
                        fetchAndDisplayHiscores(currentTargetName);
                        fetchAndDisplayEquipment(target);
                    });
                }
            }
        }
    }

    private void clearTarget()
    {
        currentTargetName = null;
        playerNameLabel.setText("No target selected");
        playerNameLabel.setForeground(Color.RED);
        hiscorePanel.removeAll();
        equipmentPanel.removeAll();
        hiscorePanel.revalidate();
        hiscorePanel.repaint();
        equipmentPanel.revalidate();
        equipmentPanel.repaint();
    }

    private void fetchAndDisplayHiscores(String playerName)
    {
        hiscorePanel.removeAll();
        try
        {
            HiscoreResult result = hiscoreClient.lookup(playerName);
            if (result != null)
            {
                Arrays.asList(
                        HiscoreSkill.ATTACK,
                        HiscoreSkill.STRENGTH,
                        HiscoreSkill.DEFENCE,
                        HiscoreSkill.HITPOINTS,
                        HiscoreSkill.RANGED,
                        HiscoreSkill.MAGIC,
                        HiscoreSkill.PRAYER
                ).forEach(skill -> {
                    int lvl = result.getSkill(skill).getLevel();
                    addStatToPanel(skill, lvl);
                });
            }
            else
            {
                JLabel err = new JLabel("No hiscore data for " + playerName);
                err.setForeground(Color.RED);
                hiscorePanel.add(err);
            }
        }
        catch (Exception ex)
        {
            JLabel err = new JLabel("Error fetching hiscore data for " + playerName);
            err.setForeground(Color.RED);
            hiscorePanel.add(err);
        }
        hiscorePanel.revalidate();
        hiscorePanel.repaint();
    }

    private void addStatToPanel(HiscoreSkill skill, int level)
    {
        JPanel statPanel = new JPanel(new BorderLayout(2,0));
        statPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel iconLabel = new JLabel();
        spriteManager.getSpriteAsync(skillToSpriteId(skill), 0, sprite -> {
            if (sprite != null)
            {
                iconLabel.setIcon(new ImageIcon(sprite));
            }
        });
        statPanel.add(iconLabel, BorderLayout.WEST);

        JLabel txt = new JLabel(skill.getName() + ": " + level);
        txt.setForeground(Color.WHITE);
        txt.setFont(new Font("Arial", Font.PLAIN, 12));
        statPanel.add(txt, BorderLayout.CENTER);

        hiscorePanel.add(statPanel);
    }

    private int skillToSpriteId(HiscoreSkill skill)
    {
        switch (skill)
        {
            case ATTACK: return 197;
            case STRENGTH: return 198;
            case DEFENCE: return 199;
            case HITPOINTS: return 200;
            case RANGED: return 201;
            case MAGIC: return 202;
            case PRAYER: return 203;
            default: throw new IllegalArgumentException("Unsupported skill: "+skill);
        }
    }

    private void fetchAndDisplayEquipment(Player target)
    {
        equipmentPanel.removeAll();

        clientThread.invoke(() -> {
            PlayerComposition comp = target.getPlayerComposition();
            if (comp == null)
            {
                JLabel noEquip = new JLabel("No equipment available.");
                noEquip.setForeground(Color.RED);
                equipmentPanel.add(noEquip);
            }
            else
            {
                AtomicLong totalValue = new AtomicLong(0);
                for (KitType kt : KitType.values())
                {
                    int itemId = comp.getEquipmentId(kt);
                    if (itemId > 0)
                    {
                        var itemComp = itemManager.getItemComposition(itemId);
                        int price = itemManager.getItemPrice(itemId);
                        totalValue.addAndGet(price);
                        addEquipmentItem(itemComp, kt, price);
                    }
                }
                addTotalValues(totalValue.get());
            }
            equipmentPanel.revalidate();
            equipmentPanel.repaint();
        });
    }

    private void addEquipmentItem(net.runelite.api.ItemComposition item, KitType kitType, int price)
    {
        JPanel row = new JPanel(new BorderLayout(5,0));
        row.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel iconLbl = new JLabel();
        net.runelite.client.util.AsyncBufferedImage img = itemManager.getImage(item.getId());
        img.addTo(iconLbl);

        JPanel textPanel = new JPanel(new GridLayout(3,1));
        textPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel itemNameLabel = new JLabel(item.getName());
        itemNameLabel.setForeground(Color.WHITE);

        JLabel itemTypeLabel = new JLabel(capitalize(kitType.name()));
        itemTypeLabel.setForeground(Color.LIGHT_GRAY);
        itemTypeLabel.setFont(FontManager.getRunescapeSmallFont());

        JLabel priceLabel = new JLabel(QuantityFormatter.quantityToStackSize(price));
        priceLabel.setForeground(price >= 10_000_000 ? Color.GREEN : Color.YELLOW);

        textPanel.add(itemNameLabel);
        textPanel.add(itemTypeLabel);
        textPanel.add(priceLabel);

        row.add(iconLbl, BorderLayout.WEST);
        row.add(textPanel, BorderLayout.CENTER);

        equipmentPanel.add(row);
    }

    private void addTotalValues(long totalValue)
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel coinIcon = new JLabel();
        spriteManager.getSpriteAsync(1046, 0, sprite -> {
            if (sprite != null)
            {
                coinIcon.setIcon(new ImageIcon(sprite));
            }
        });

        JLabel totalLbl = new JLabel("Total: " + QuantityFormatter.quantityToStackSize(totalValue));
        totalLbl.setForeground(Color.GREEN);
        totalLbl.setFont(FontManager.getRunescapeFont());

        panel.add(coinIcon);
        panel.add(totalLbl);
        equipmentPanel.add(panel);
    }

    private String capitalize(String s)
    {
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
