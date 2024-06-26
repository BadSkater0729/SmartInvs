package fr.minuskube.inv;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public interface SchedulerManager {

    enum SchedulerType {
        GLOBAL,
        ENTITY,
        REGION,
        ASYNC
    }

    // If we ever generalize these methods, replace player with Object
    void runTask(BukkitRunnable task, Player player, long delay, long period, SchedulerType type);
    void cancelTaskByPlayer(Player player);

}
