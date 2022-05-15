package com.gmail.kobun127.aulhu.Abilities.Flame;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FlameAbility {
    private static final Set<UUID> flamer = new HashSet<>();

    public static void registerFlamer(Player player) {
        flamer.add(player.getUniqueId());
    }

    public static boolean isFlamer(Player player) {
        return flamer.contains(player.getUniqueId());
    }

    public static Set<Player> getFlamerPlayers() {
        return flamer.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }
}
