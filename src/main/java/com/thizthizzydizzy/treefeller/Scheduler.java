package com.thizthizzydizzy.treefeller;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getLogger;

/**
 * Utility class for scheduling tasks with support for Folia.
 */
public final class Scheduler {

    private static final boolean isFolia;
    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            folia = true;
            getLogger().warning("Scheduler You are running FOLIA");
        } catch (ClassNotFoundException e) {
            folia = false;
            getLogger().warning("Scheduler You are running BUKKIT");
        }
        isFolia = folia;
    }

    public static void run(Runnable runnable) {
        if (isFolia)
            Bukkit.getGlobalRegionScheduler().execute(TreeFeller.getInstance(), runnable);
        else
            Bukkit.getScheduler().runTask(TreeFeller.getInstance(), runnable);
    }

    public static Task runLater(Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            run(runnable);
            return new Task(null);
        }
        if (isFolia)
            return new Task(Bukkit.getGlobalRegionScheduler()
                    .runDelayed(TreeFeller.getInstance(), t -> runnable.run(), delayTicks));
        else
            return new Task(Bukkit.getScheduler().runTaskLater(TreeFeller.getInstance(), runnable, delayTicks));
    }

    public static Task runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia)
            return new Task(Bukkit.getGlobalRegionScheduler()
                    .runAtFixedRate(TreeFeller.getInstance(), t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
        else
            return new Task(Bukkit.getScheduler().runTaskTimer(TreeFeller.getInstance(), runnable, delayTicks, periodTicks));
    }

    public static void runAsync(Runnable runnable) {
        if (isFolia)
            Bukkit.getAsyncScheduler().runNow(TreeFeller.getInstance(), t -> runnable.run());
        else
            Bukkit.getScheduler().runTaskAsynchronously(TreeFeller.getInstance(), runnable);
    }

    public static Task runAsyncLater(Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            runAsync(runnable);
            return new Task(null);
        }
        if (isFolia)
            return new Task(Bukkit.getAsyncScheduler().runDelayed(TreeFeller.getInstance(), t -> runnable.run(), delayTicks * 50L, TimeUnit.MILLISECONDS));
        else
            return new Task(Bukkit.getScheduler().runTaskLaterAsynchronously(TreeFeller.getInstance(), runnable, delayTicks));
    }

    public static Task runAsyncTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia)
            return new Task(Bukkit.getAsyncScheduler().runAtFixedRate(TreeFeller.getInstance(), t -> runnable.run(), (delayTicks < 1 ? 1 : delayTicks) * 50L, periodTicks * 50L, TimeUnit.MILLISECONDS));
        else
            return new Task(Bukkit.getScheduler().runTaskTimerAsynchronously(TreeFeller.getInstance(), runnable, delayTicks, periodTicks));
    }

    public static void run(Location location, Runnable runnable) {
        if (isFolia)
            Bukkit.getRegionScheduler().execute(TreeFeller.getInstance(), location, runnable);
        else
            Bukkit.getScheduler().runTask(TreeFeller.getInstance(), runnable);
    }

    public static Task runLater(Location location, Runnable runnable, long delayTicks) {
        if (delayTicks <= 0) {
            run(location, runnable);
            return new Task(null);
        }
        if (isFolia)
            return new Task(Bukkit.getRegionScheduler().runDelayed(TreeFeller.getInstance(), location, t -> runnable.run(), delayTicks));
        else
            return new Task(Bukkit.getScheduler().runTaskLater(TreeFeller.getInstance(), runnable, delayTicks));
    }

    public static Task runTimer(Location location, Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia)
            return new Task(Bukkit.getRegionScheduler().runAtFixedRate(TreeFeller.getInstance(), location, t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
        else
            return new Task(Bukkit.getScheduler().runTaskTimer(TreeFeller.getInstance(), runnable, delayTicks, periodTicks));
    }

    public static boolean isFolia() {
        return isFolia;
    }

    public static void cancelCurrentTask() {
    }

    public static class Task {

        private Object foliaTask;
        private BukkitTask bukkitTask;

        Task(Object foliaTask) {
            this.foliaTask = foliaTask;
        }

        Task(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        public void cancel() {
            if (foliaTask != null) {
                ((ScheduledTask) foliaTask).cancel();
            } else if (bukkitTask != null) {
                bukkitTask.cancel();
            }
        }
    }
}
