package com.gmail.kobun127.aulhu.Abilities;

import com.gmail.kobun127.aulhu.AUlHu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainLoop {
    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                CooldownManager.update();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    CooldownManager.updateExpBar(player);
                }
            }
        }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
    }
}
