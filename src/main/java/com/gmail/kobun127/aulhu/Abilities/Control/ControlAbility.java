package com.gmail.kobun127.aulhu.Abilities.Control;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class ControlAbility {
    private static final Set<UUID> controllers = new HashSet<>();
    private static final Map<UUID, BukkitTask> tasks = new HashMap<>();

    public static void registerController(Player player) {
        controllers.add(player.getUniqueId());
    }

    public static boolean isController(Player player) {
        return controllers.contains(player.getUniqueId());
    }

    public static Set<Player> getControllerPlayers() {
        return controllers.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public static void putTask(Player player, BukkitTask task) {
        tasks.put(player.getUniqueId(), task);
    }

    public static void cancelTask(Player player) {
        tasks.get(player.getUniqueId()).cancel();
        tasks.remove(player.getUniqueId());
    }

    public static boolean usingAbility(Player player) {
        return tasks.containsKey(player.getUniqueId());
    }
}
