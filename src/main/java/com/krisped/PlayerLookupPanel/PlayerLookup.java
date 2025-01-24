package com.krisped.PlayerLookupPanel;

import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
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

public class PlayerLookup extends JPanel {

    private final Client client;
    private final ClientThread clientThread;
    private final HiscoreClient hiscoreClient;
    private final ItemManager itemManager;
    private final SpriteManager spriteManager;

    private final JLabel playerNameLabel;
    private final JPanel hiscorePanel;
    private final JPanel equipmentPanel;

    private Runnable onBackButtonPressed;  // Store the action for the back button
    private String currentTargetName = null;

    public PlayerLookup(Client client, ClientThread clientThread, HiscoreClient hiscoreClient, ItemManager itemManager, SpriteManager spriteManager) {
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
        titlePanel.add(Box.createVerticalStrut(30), BorderLayout.SOUTH); // Space between title and player name

        add(titlePanel, BorderLayout.NORTH);

        // Player Name Panel
        JPanel playerNamePanel = new JPanel(new BorderLayout());
        playerNamePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        playerNamePanel.setBorder(BorderFactory.createTitledBorder("Player Name"));

        playerNameLabel = new JLabel("No target selected", SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerNameLabel.setForeground(Color.RED);
        playerNamePanel.add(playerNameLabel, BorderLayout.CENTER);

        JButton clearTargetButton = new JButton("Clear Target");
        clearTargetButton.setForeground(Color.WHITE);
        clearTargetButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        clearTargetButton.addActionListener(e -> clearTarget());
        playerNamePanel.add(clearTargetButton, BorderLayout.SOUTH);

        // Hiscore Panel
        hiscorePanel = new JPanel();
        hiscorePanel.setLayout(new BoxLayout(hiscorePanel, BoxLayout.Y_AXIS));
        hiscorePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        hiscorePanel.setBorder(BorderFactory.createTitledBorder("Hiscores"));

        JScrollPane hiscoreScrollPane = new JScrollPane(hiscorePanel);
        hiscoreScrollPane.setPreferredSize(new Dimension(300, 180));
        hiscoreScrollPane.setBorder(null);

        // Equipment Panel
        equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.Y_AXIS));
        equipmentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        equipmentPanel.setBorder(BorderFactory.createTitledBorder("Equipment"));

        JScrollPane equipmentScrollPane = new JScrollPane(equipmentPanel);
        equipmentScrollPane.setPreferredSize(new Dimension(300, 450));
        equipmentScrollPane.setBorder(null);

        // Back Button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            if (onBackButtonPressed != null) {
                onBackButtonPressed.run(); // Execute the back button action if set
            }
        });

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        contentPanel.add(hiscoreScrollPane);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(equipmentScrollPane);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(backButton); // Adds back button

        add(playerNamePanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.SOUTH);

        monitorInteraction();
    }

    // Set action for the back button
    public void setOnBackButtonPressed(Runnable onBackButtonPressed) {
        this.onBackButtonPressed = onBackButtonPressed;  // Save the action for the back button
    }

    private void monitorInteraction() {
        new Thread(() -> {
            while (true) {
                try {
                    updateTarget();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void updateTarget() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return;
        }

        if (localPlayer.getInteracting() instanceof Player) {
            Player target = (Player) localPlayer.getInteracting();
            if (target != null && target.getName() != null) {
                if (!target.getName().equals(currentTargetName)) {
                    currentTargetName = target.getName();
                    SwingUtilities.invokeLater(() -> {
                        playerNameLabel.setText(target.getName());
                        playerNameLabel.setForeground(Color.GREEN);
                        fetchAndDisplayHiscores(target.getName());
                        fetchAndDisplayEquipment(target);
                    });
                }
            }
        }
    }

    private void clearTarget() {
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

    private void fetchAndDisplayHiscores(String playerName) {
        hiscorePanel.removeAll();
        try {
            HiscoreResult hiscoreResult = hiscoreClient.lookup(playerName);
            if (hiscoreResult != null) {
                Arrays.asList(HiscoreSkill.ATTACK, HiscoreSkill.STRENGTH, HiscoreSkill.DEFENCE,
                                HiscoreSkill.HITPOINTS, HiscoreSkill.RANGED, HiscoreSkill.MAGIC, HiscoreSkill.PRAYER)
                        .forEach(skill -> addStatToPanel(skill, hiscoreResult.getSkill(skill).getLevel()));
            } else {
                JLabel errorLabel = new JLabel("No hiscore data available for " + playerName);
                errorLabel.setForeground(Color.RED);
                hiscorePanel.add(errorLabel);
            }
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Error fetching hiscore data for " + playerName);
            errorLabel.setForeground(Color.RED);
            hiscorePanel.add(errorLabel);
        }

        hiscorePanel.revalidate();
        hiscorePanel.repaint();
    }

    private void addStatToPanel(HiscoreSkill skill, int level) {
        JPanel statPanel = new JPanel(new BorderLayout(2, 0));
        statPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel iconLabel = new JLabel();
        spriteManager.getSpriteAsync(skillToSpriteId(skill), 0, sprite -> {
            if (sprite != null) {
                iconLabel.setIcon(new ImageIcon(sprite));
            }
        });
        statPanel.add(iconLabel, BorderLayout.WEST);

        JLabel textLabel = new JLabel(skill.getName() + ": " + level);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statPanel.add(textLabel, BorderLayout.CENTER);

        hiscorePanel.add(statPanel);
    }

    private void fetchAndDisplayEquipment(Player target) {
        equipmentPanel.removeAll();

        clientThread.invoke(() -> {
            PlayerComposition composition = target.getPlayerComposition();
            if (composition == null) {
                JLabel noEquipmentLabel = new JLabel("No equipment available.");
                noEquipmentLabel.setForeground(Color.RED);
                equipmentPanel.add(noEquipmentLabel);
            } else {
                AtomicLong totalValue = new AtomicLong(0);

                for (KitType kitType : KitType.values()) {
                    int itemId = composition.getEquipmentId(kitType);
                    if (itemId > 0) {
                        ItemComposition item = itemManager.getItemComposition(itemId);
                        int price = itemManager.getItemPrice(itemId);

                        totalValue.addAndGet(price);
                        addEquipmentItem(item, kitType, price);
                    }
                }

                addTotalValues(totalValue.get());
            }

            equipmentPanel.revalidate();
            equipmentPanel.repaint();
        });
    }

    private void addEquipmentItem(ItemComposition item, KitType kitType, int price) {
        JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
        itemPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel itemImageLabel = new JLabel();
        AsyncBufferedImage itemImage = itemManager.getImage(item.getId());
        itemImage.addTo(itemImageLabel);

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel itemNameLabel = new JLabel(item.getName());
        itemNameLabel.setForeground(Color.WHITE);

        JLabel itemTypeLabel = new JLabel(capitalize(kitType.name()));
        itemTypeLabel.setForeground(Color.LIGHT_GRAY);
        itemTypeLabel.setFont(FontManager.getRunescapeSmallFont());

        JLabel itemPriceLabel = new JLabel(QuantityFormatter.quantityToStackSize(price));
        itemPriceLabel.setForeground(price >= 10_000_000 ? Color.GREEN : Color.YELLOW);

        textPanel.add(itemNameLabel);
        textPanel.add(itemTypeLabel);
        textPanel.add(itemPriceLabel);

        itemPanel.add(itemImageLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);

        equipmentPanel.add(itemPanel);
    }

    private void addTotalValues(long totalValue) {
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel coinIcon = new JLabel();
        spriteManager.getSpriteAsync(1046, 0, sprite -> {
            if (sprite != null) {
                coinIcon.setIcon(new ImageIcon(sprite));
            }
        });

        JLabel totalLabel = new JLabel("Total: " + QuantityFormatter.quantityToStackSize(totalValue));
        totalLabel.setForeground(Color.GREEN);
        totalLabel.setFont(FontManager.getRunescapeFont());

        totalPanel.add(coinIcon);
        totalPanel.add(totalLabel);
        equipmentPanel.add(totalPanel);
    }

    private int skillToSpriteId(HiscoreSkill skill) {
        switch (skill) {
            case ATTACK:
                return 197;
            case STRENGTH:
                return 198;
            case DEFENCE:
                return 199;
            case HITPOINTS:
                return 200;
            case RANGED:
                return 201;
            case MAGIC:
                return 202;
            case PRAYER:
                return 203;
            default:
                throw new IllegalArgumentException("Unsupported skill: " + skill);
        }
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
