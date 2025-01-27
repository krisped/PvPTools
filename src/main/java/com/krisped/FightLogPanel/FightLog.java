package com.krisped.FightLogPanel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FightLog extends JPanel
{
    private static final String BASE_PATH = System.getProperty("user.home") + "/Documents/KP Plugins/Fight Log/";
    private static final String FILE_PATH = BASE_PATH + "fightlog.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, PlayerStats> playerStatsMap;
    private final Client client;
    private final Runnable onBackButtonPressed;
    private final JTextArea logArea;

    public FightLog(Client client, Runnable onBackButtonPressed)
    {
        if (client == null)
        {
            throw new IllegalArgumentException("Client cannot be null!");
        }
        if (onBackButtonPressed == null)
        {
            throw new IllegalArgumentException("Runnable cannot be null!");
        }

        this.client = client;
        this.onBackButtonPressed = onBackButtonPressed;

        setLayout(new BorderLayout());

        // Last inn data
        Map<String, PlayerStats> loadedData = loadFightLog();
        playerStatsMap = (loadedData != null) ? loadedData : new HashMap<>();

        JLabel title = new JLabel("Fight Log", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        updateLogArea();
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBackButtonPressed.run());
        add(backButton, BorderLayout.SOUTH);
    }

    public void logFight(String playerName, boolean isKill)
    {
        PlayerStats stats = playerStatsMap.getOrDefault(playerName, new PlayerStats(playerName));
        if (isKill)
        {
            stats.incrementWins();
            sendChatMessage("Kill logged to Fight Log");
        }
        else
        {
            stats.incrementLosses();
            sendChatMessage("Death logged to Fight Log");
        }
        playerStatsMap.put(playerName, stats);

        SwingUtilities.invokeLater(this::updateLogArea);
        saveFightLog();
    }

    private void saveFightLog()
    {
        try
        {
            ensureDirectoryExists();
            String jsonData = gson.toJson(playerStatsMap);
            Files.write(Paths.get(FILE_PATH), jsonData.getBytes());
        }
        catch (IOException e)
        {
            System.err.println("Failed to save FightLog: " + e.getMessage());
        }
    }

    private Map<String, PlayerStats> loadFightLog()
    {
        try
        {
            ensureDirectoryExists();
            File file = new File(FILE_PATH);
            if (!file.exists())
            {
                // Opprett testdata
                Map<String, PlayerStats> testData = new HashMap<>();
                testData.put("Test", new PlayerStats("Test", 1, 5));
                saveTestFile(testData);
                return testData;
            }

            String jsonData = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            return gson.fromJson(jsonData, new TypeToken<Map<String, PlayerStats>>(){}.getType());
        }
        catch (IOException e)
        {
            System.err.println("Failed to load FightLog: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void ensureDirectoryExists() throws IOException
    {
        File dir = new File(BASE_PATH);
        if (!dir.exists() && !dir.mkdirs())
        {
            throw new IOException("Failed to create directory: " + BASE_PATH);
        }
    }

    private void updateLogArea()
    {
        if (playerStatsMap.isEmpty())
        {
            logArea.setText("No data available.");
            return;
        }
        logArea.setText("");
        // For enkelhet, bare liste opp data
        for (Map.Entry<String, PlayerStats> e : playerStatsMap.entrySet())
        {
            String name = e.getKey();
            PlayerStats st = e.getValue();
            logArea.append(name + " => Wins=" + st.getWins() + ", Losses=" + st.getLosses() + "\n");
        }
    }

    private void sendChatMessage(String message)
    {
        if (client != null)
        {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[KP] PvP Tools: " + message, null);
        }
    }

    private void saveTestFile(Map<String, PlayerStats> testData)
    {
        try
        {
            String json = gson.toJson(testData);
            Files.write(Paths.get(FILE_PATH), json.getBytes());
        }
        catch (IOException e)
        {
            System.err.println("Failed to save test data: " + e.getMessage());
        }
    }

    private static class PlayerStats
    {
        private final String playerName;
        private int wins;
        private int losses;

        public PlayerStats(String playerName)
        {
            this.playerName = playerName;
        }

        public PlayerStats(String playerName, int wins, int losses)
        {
            this.playerName = playerName;
            this.wins = wins;
            this.losses = losses;
        }

        public void incrementWins()
        {
            wins++;
        }

        public void incrementLosses()
        {
            losses++;
        }

        public int getWins()
        {
            return wins;
        }

        public int getLosses()
        {
            return losses;
        }
    }
}
