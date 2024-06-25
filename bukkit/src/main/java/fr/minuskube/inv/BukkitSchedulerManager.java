package fr.minuskube.inv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BukkitSchedulerManager implements SchedulerManager {
    private final JavaPlugin plugin;
    private final Map<Player, BukkitTask> tasks = new HashMap<>();

    public BukkitSchedulerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTask(BukkitRunnable task, Player player, long delay, long period, SchedulerType type) {
        BukkitTask scheduledTask;

        if (delay == 0 && period == 0) {
            scheduledTask = task.runTask(plugin);
        } else if (delay != 0 && period == 0) {
            scheduledTask = task.runTaskLater(plugin, delay);
        } else {
            scheduledTask = task.runTaskTimer(plugin, delay, period);
        }

        if (scheduledTask != null) {
            tasks.put(player, scheduledTask);
        }
    }

    @Override
    public void cancelTaskByPlayer(Player player) {
        BukkitTask task = tasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }
}