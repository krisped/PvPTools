package com.krisped;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PluginLauncher
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PvPToolsPlugin.class); // Endret til din plugin-klasse
		RuneLite.main(args);
	}
}
