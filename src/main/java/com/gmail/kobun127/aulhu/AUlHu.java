package com.gmail.kobun127.aulhu;

import com.gmail.kobun127.aulhu.Abilities.Control.Buwaa.BuwaaEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.ControlAbility;
import com.gmail.kobun127.aulhu.Abilities.Control.Hikiyose.HikiyoseEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.Kotei.KoteiEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.Nenriki.NenrikiEvent;
import com.gmail.kobun127.aulhu.Abilities.Control.Tama.TamaEvent;
import com.gmail.kobun127.aulhu.Abilities.Flame.Mera.MeraEvent;
import com.gmail.kobun127.aulhu.Abilities.Flame.FlameAbility;
import com.gmail.kobun127.aulhu.Abilities.Flame.Hienbasira.HienbasiraEvent;
import com.gmail.kobun127.aulhu.Abilities.Flame.Kaenhai.KaenhaiEvent;
import com.gmail.kobun127.aulhu.Abilities.MainLoop;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.Baan.BaanEvent;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.Bakuretu.BakuretuEvent;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.Kyouka.KyoukaEvent;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.NikutaiAbility;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.Tuyosou.TuyosouEvent;
import org.bukkit.Bukkit;
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
        pluginManager.registerEvents(new KyoukaEvent(), this);
        pluginManager.registerEvents(new BakuretuEvent(), this);
        pluginManager.registerEvents(new BaanEvent(), this);
        pluginManager.registerEvents(new TuyosouEvent(), this);
        pluginManager.registerEvents(new HienbasiraEvent(), this);
        pluginManager.registerEvents(new KaenhaiEvent(), this);
        pluginManager.registerEvents(new MeraEvent(), this);
        MainLoop.run();
        ControlAbility.registerController(Bukkit.getPlayer("tubugai"));
        NikutaiAbility.registerNikutaier(Bukkit.getPlayer("tubugai"));
        FlameAbility.registerFlamer(Bukkit.getPlayer("tubugai"));
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}