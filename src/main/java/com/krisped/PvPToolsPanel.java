package com.krisped;

import com.krisped.PlayerLookupPanel.PlayerLookup;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

public class PvPToolsPanel extends PluginPanel {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public PvPToolsPanel(PlayerLookup playerLookup) {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel homePanel = createHomePanel();
        mainPanel.add(homePanel, "home");

        mainPanel.add(playerLookup, "playerLookup");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "home");
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("[KP] PvP Tools");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        homePanel.add(title, BorderLayout.NORTH);

        JButton playerLookupButton = new JButton("Player Lookup");
        playerLookupButton.addActionListener(e -> cardLayout.show(mainPanel, "playerLookup"));
        homePanel.add(playerLookupButton, BorderLayout.CENTER);

        return homePanel;
    }

    public void switchToHome() {
        cardLayout.show(mainPanel, "home");
    }
}
