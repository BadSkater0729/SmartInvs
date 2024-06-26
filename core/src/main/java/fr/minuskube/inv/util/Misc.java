package fr.minuskube.inv.util;

import org.bukkit.plugin.java.JavaPlugin;

public class Misc {

    private static final boolean debugEnabled;

    static {
        debugEnabled = Boolean.parseBoolean(System.getProperty("SMART_INVS_DEBUG"));
    }

    public static void debugMsg(String msg, JavaPlugin plugin) {
        if (debugEnabled) {
            plugin.getServer().getLogger().warning("SmartInvs Debug: " + msg);
        }
    }
}
