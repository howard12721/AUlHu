package com.gmail.kobun127.aulhu;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class AUlHu extends JavaPlugin {

    private static Plugin plugin;

    @Override

    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}