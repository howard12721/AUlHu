package com.gmail.kobun127.aulhu.Abilities.Nikutai;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class NikutaiAbility {
    private static final Set<UUID> nikutaiers = new HashSet<>();

    public static void registerNikutaier(Player player) {
        nikutaiers.add(player.getUniqueId());
    }

    public static boolean isNikutaier(Player player) {
        return nikutaiers.contains(player.getUniqueId());
    }

    public static Set<Player> getNikutaiPlayers() {
        return nikutaiers.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }
}
