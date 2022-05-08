package com.gmail.kobun127.aulhu.Abilities.Control;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class ControlAbility {
    private static final Set<UUID> controllers = new HashSet<>();

    public static void registerController(Player player) {
        controllers.add(player.getUniqueId());
    }

    public static boolean isController(Player player) {
        return controllers.contains(player.getUniqueId());
    }

    public static Set<Player> getControllerPlayers() {
        return controllers.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }
}
