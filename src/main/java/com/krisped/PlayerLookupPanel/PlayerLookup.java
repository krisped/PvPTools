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

    private Runnable onBackButtonPressed;
    private String currentTargetName = null;

    public PlayerLookup(Client client, ClientThread clientThread, HiscoreClient hiscoreClient, ItemManager itemManager, SpriteManager spriteManager) {
        this.client = client;
        this.clientThread = clientThread;
        this.hiscoreClient = hiscoreClient;
        this.itemManager = itemManager;
        this.spriteManager = spriteManager;

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel title = new JLabel("Player Lookup");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel playerNamePanel = new JPanel(new BorderLayout());
        playerNamePanel.setBorder(BorderFactory.createTitledBorder("Player Name"));
        playerNamePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        playerNameLabel = new JLabel("No target selected", SwingConstants.CENTER);
        playerNameLabel.setFont(FontManager.getRunescapeFont());
        playerNameLabel.setForeground(Color.WHITE);
        playerNamePanel.add(playerNameLabel, BorderLayout.CENTER);

        // Adjusting the layout of hiscorePanel to use FlowLayout for better control over spacing
        hiscorePanel = new JPanel(new GridLayout(7, 1, 2, 2));  // Tighter grid with 2px gap
        hiscorePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        hiscorePanel.setBorder(BorderFactory.createTitledBorder("Hiscores"));

        equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.Y_AXIS));
        equipmentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        equipmentPanel.setBorder(BorderFactory.createTitledBorder("Equipment"));

        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        contentPanel.add(hiscorePanel);
        contentPanel.add(equipmentPanel);

        add(playerNamePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        monitorInteraction();
    }

    public void setOnBackButtonPressed(Runnable onBackButtonPressed) {
        this.onBackButtonPressed = onBackButtonPressed;
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
                        fetchAndDisplayHiscores(target.getName());
                        fetchAndDisplayEquipment(target);
                    });
                }
            }
        }
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
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        statPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        JLabel iconLabel = new JLabel();
        spriteManager.getSpriteAsync(skillToSpriteId(skill), 0, sprite -> {
            if (sprite != null) {
                iconLabel.setIcon(new ImageIcon(sprite));
            }
        });
        statPanel.add(iconLabel);

        JLabel textLabel = new JLabel(skill.getName() + ": " + level);
        textLabel.setForeground(Color.WHITE);
        statPanel.add(textLabel);

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
                AtomicLong maxItemValue = new AtomicLong(0);
                AtomicLong secondMaxItemValue = new AtomicLong(0);
                AtomicLong thirdMaxItemValue = new AtomicLong(0);

                for (KitType kitType : KitType.values()) {
                    int itemId = composition.getEquipmentId(kitType);
                    if (itemId > 0) {
                        ItemComposition item = itemManager.getItemComposition(itemId);
                        int price = itemManager.getItemPrice(itemId);
                        int highAlchValue = item.getHaPrice();

                        totalValue.addAndGet(price);

                        if (highAlchValue > maxItemValue.get()) {
                            thirdMaxItemValue.set(secondMaxItemValue.get());
                            secondMaxItemValue.set(maxItemValue.get());
                            maxItemValue.set(highAlchValue);
                        } else if (highAlchValue > secondMaxItemValue.get()) {
                            thirdMaxItemValue.set(secondMaxItemValue.get());
                            secondMaxItemValue.set(highAlchValue);
                        } else if (highAlchValue > thirdMaxItemValue.get()) {
                            thirdMaxItemValue.set(highAlchValue);
                        }

                        addEquipmentItem(item, kitType, price);
                    }
                }

                addTotalValues(totalValue.get(), maxItemValue.get(), secondMaxItemValue.get(), thirdMaxItemValue.get());
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

    private void addTotalValues(long totalValue, long maxItemValue, long secondMaxItemValue, long thirdMaxItemValue) {
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

        // First total: Coin Stack
        JLabel coinIcon = new JLabel();
        spriteManager.getSpriteAsync(1046, 0, sprite -> { // Coin stack sprite ID
            if (sprite != null) {
                coinIcon.setIcon(new ImageIcon(sprite));
            }
        });

        JLabel totalLabel = new JLabel("Total: " + QuantityFormatter.quantityToStackSize(totalValue));
        totalLabel.setForeground(Color.GREEN);
        totalLabel.setFont(FontManager.getRunescapeFont());

        // Second total: Protect Item
        JLabel protectIcon = new JLabel();
        spriteManager.getSpriteAsync(123, 0, sprite -> { // Protect Item sprite ID
            if (sprite != null) {
                protectIcon.setIcon(new ImageIcon(sprite));
            }
        });

        JLabel protectLabel = new JLabel("Protect Item Total: " + QuantityFormatter.quantityToStackSize(totalValue - maxItemValue));
        protectLabel.setForeground(Color.CYAN);
        protectLabel.setFont(FontManager.getRunescapeFont());

        // Third total: 4x Protect Item
        JLabel protectFourIcon = new JLabel();
        spriteManager.getSpriteAsync(912, 0, sprite -> { // Custom Protect Item 4x Sprite ID
            if (sprite != null) {
                protectFourIcon.setIcon(new ImageIcon(sprite));
            }
        });

        JLabel protectFourLabel = new JLabel("With Protect (4x): " + QuantityFormatter.quantityToStackSize(totalValue - maxItemValue - secondMaxItemValue - thirdMaxItemValue));
        protectFourLabel.setForeground(Color.MAGENTA);
        protectFourLabel.setFont(FontManager.getRunescapeFont());

        totalPanel.add(coinIcon);
        totalPanel.add(totalLabel);
        equipmentPanel.add(totalPanel);

        totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        totalPanel.add(protectIcon);
        totalPanel.add(protectLabel);
        equipmentPanel.add(totalPanel);

        totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        totalPanel.add(protectFourIcon);
        totalPanel.add(protectFourLabel);
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
