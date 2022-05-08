package com.gmail.kobun127.aulhu.Abilities.Control.Nenriki;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Nenriki {

    private static final Map<UUID, BukkitTask> tasks = new HashMap<>();

    public static void putTask(Player player, BukkitTask task) {
        tasks.put(player.getUniqueId(), task);
    }

    public static void cancelTask(Player player) {
        tasks.get(player.getUniqueId()).cancel();
        tasks.remove(player.getUniqueId());
    }

    public static boolean usingAbility(Player player) {
        if (!tasks.containsKey(player.getUniqueId())) {
            return false;
        }
        if (tasks.get(player.getUniqueId()).isCancelled()) {
            tasks.remove(player.getUniqueId());
            return false;
        }
        return true;
    }
}
