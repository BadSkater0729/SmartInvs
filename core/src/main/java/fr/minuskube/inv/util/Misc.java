package fr.minuskube.inv.util;

import fr.minuskube.inv.SmartInvsPlugin;

public class Misc {
    public static void debugMsg(String msg) {
        // if (debugEnabled)
        if (true) { SmartInvsPlugin.instance().getServer().getLogger().warning("SmartInvs Debug: " + msg); }
    }
}
