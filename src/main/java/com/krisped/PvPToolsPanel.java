package com.krisped;

import com.krisped.FightLogPanel.FightLog;
import com.krisped.PlayerLookupPanel.PlayerLookup;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;

public class PvPToolsPanel extends PluginPanel
{
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public PvPToolsPanel(PlayerLookup playerLookup, FightLog fightLog)
    {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel homePanel = createHomePanel();
        mainPanel.add(homePanel, "home");

        mainPanel.add(playerLookup, "playerLookup");
        mainPanel.add(fightLog, "fightLog");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "home");
    }

    private JPanel createHomePanel()
    {
        JPanel homePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("[KP] PvP Tools");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        homePanel.add(title, BorderLayout.NORTH);

        JButton lookupButton = new JButton("Player Lookup");
        lookupButton.addActionListener(e -> cardLayout.show(mainPanel, "playerLookup"));
        homePanel.add(lookupButton, BorderLayout.CENTER);

        JButton fightLogButton = new JButton("Fight Log");
        fightLogButton.addActionListener(e -> cardLayout.show(mainPanel, "fightLog"));
        homePanel.add(fightLogButton, BorderLayout.SOUTH);

        return homePanel;
    }

    public void switchToHome()
    {
        cardLayout.show(mainPanel, "home");
    }
}
