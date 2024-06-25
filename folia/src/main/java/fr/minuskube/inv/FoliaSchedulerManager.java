package fr.minuskube.inv;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FoliaSchedulerManager implements SchedulerManager {
    private final JavaPlugin plugin;
    private final Map<Player, ScheduledTask> tasks = new HashMap<>();

    public FoliaSchedulerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTask(BukkitRunnable task, Player player, long delay, long period, SchedulerType type) {
        // Convert BukkitRunnable to Folia task
        Consumer<ScheduledTask> consumerTask = (scheduledTask) -> task.run();

        // Begin type check
        switch (type) {
            case ENTITY:
                ScheduledTask scheduledTask;
                if (delay == 0 && period == 0) {
                    scheduledTask = player.getScheduler().run(plugin, consumerTask, null);
                } else if (delay != 0 && period == 0) {
                    scheduledTask = player.getScheduler().runDelayed(plugin, consumerTask, null, delay);
                } else {
                    scheduledTask = player.getScheduler().runAtFixedRate(plugin, consumerTask, null, delay, period);
                }
                if (scheduledTask != null) {
                    tasks.put(player, scheduledTask);
                }
                break;
            case GLOBAL:
            case REGION:
            case ASYNC:
            default:
                plugin.getLogger().warning("Scheduler type " + type + " is not implemented yet.");
                break;
        }
    }

    @Override
    public void cancelTaskByPlayer(Player player) {
        ScheduledTask task = tasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }
}