package fr.minuskube.inv.util;

import org.bukkit.plugin.java.JavaPlugin;

public class Misc {

    private static final boolean debugEnabled;

    static {
        debugEnabled = System.getProperty("SMART_INVS_DEBUG").equalsIgnoreCase("true");
    }

    public static void debugMsg(String msg, JavaPlugin p) {
        if (debugEnabled) { p.getServer().getLogger().warning("SmartInvs Debug: " + msg); }
    }
}
