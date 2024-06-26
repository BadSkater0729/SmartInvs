package fr.minuskube.inv;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static fr.minuskube.inv.util.Misc.debugMsg;

public class FoliaSchedulerManager implements SchedulerManager {
    private final JavaPlugin plugin;
    private final Map<Player, ScheduledTask> tasks = new HashMap<>();

    public FoliaSchedulerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runTask(BukkitRunnable task, Player player, long delay, long period, SchedulerType type) {
        // Convert BukkitRunnable to Folia task
        debugMsg("Using FoliaSchedulerManager, converting bukkitrun to task...");
        Consumer<ScheduledTask> consumerTask = (scheduledTask) -> task.run();

        // Begin type check
        switch (type) {
            case ENTITY:
                debugMsg("Entity scheduler requested");
                ScheduledTask scheduledTask;
                if (delay == 0 && period == 0) {
                    debugMsg("run()");
                    scheduledTask = player.getScheduler().run(plugin, consumerTask, null);
                } else if (delay != 0 && period == 0) {
                    debugMsg("runDelayed() <-- delay");
                    scheduledTask = player.getScheduler().runDelayed(plugin, consumerTask, null, delay);
                } else {
                    debugMsg("runAtFixedRate() <-- delay + repeat");
                    scheduledTask = player.getScheduler().runAtFixedRate(plugin, consumerTask, null, delay, period);
                }
                if (scheduledTask != null) {
                    debugMsg("Added scheduledTask to list. List size: " + tasks.size());
                    tasks.put(player, scheduledTask);
                } else {
                    debugMsg("scheduledTask was null? Not adding to list! List size: " + tasks.size());
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
            debugMsg("Task cancelled :) List size: " + tasks.size());
            task.cancel();
        } else {
            debugMsg("Unable to cancel task, list remove() returned null. List size: " + tasks.size());
        }
    }
}