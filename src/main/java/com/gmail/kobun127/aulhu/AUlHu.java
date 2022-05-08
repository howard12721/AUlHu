package com.gmail.kobun127.aulhu;

import com.gmail.kobun127.aulhu.Abilities.Control.Buwaa.BuwaaEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.ControlAbility;
import com.gmail.kobun127.aulhu.Abilities.Control.Hikiyose.HikiyoseEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.Kotei.KoteiEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.Nenriki.NenrikiEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.Tama.TamaEvent;
import com.gmail.kobun127.aulhu.Abilities.MainLoop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AUlHu extends JavaPlugin {

    private static Plugin plugin;

    @Override

    public void onEnable() {
        plugin = this;
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new NenrikiEvent(), this);
        pluginManager.registerEvents(new BuwaaEvent(), this);
        pluginManager.registerEvents(new KoteiEvent(), this);
        pluginManager.registerEvents(new HikiyoseEvent(), this);
        pluginManager.registerEvents(new TamaEvent(), this);
        MainLoop.run();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ControlAbility.registerController(player);
        }
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}