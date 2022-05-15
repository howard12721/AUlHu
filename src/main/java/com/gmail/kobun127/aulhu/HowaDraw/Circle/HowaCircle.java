package com.gmail.kobun127.aulhu.HowaDraw.Circle;

import com.gmail.kobun127.aulhu.HowaDraw.Runnable.HowaDrawRunnable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HowaCircle {
    public HowaCircle(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Plugin plugin;
    private Location center;
    private double range = 3;
    private double density = 10;
    private double angle = 0;
    private double theta = 0;
    private int ticks = 1;
    private Particle particle = Particle.END_ROD;
    private HowaDrawRunnable task = null;

    public HowaCircle setCenter(Location center) {
        this.center = center.clone();
        return this;
    }

    public HowaCircle setDensity(double density) {
        this.density = density;
        return this;
    }

    public HowaCircle setRange(double range) {
        this.range = range;
        return this;
    }

    public HowaCircle setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    public HowaCircle setTheta(double theta) {
        this.theta = theta;
        return this;
    }

    public HowaCircle setTicks(int ticks) {
        if (ticks == 0) ticks = 1;
        this.ticks = ticks;
        return this;
    }

    public HowaCircle setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public HowaCircle setTask(HowaDrawRunnable howaDrawRunnable) {
        this.task = howaDrawRunnable;
        return this;
    }

    public void draw() {
        World world = center.getWorld();
        if (world == null) {
            return;
        }
        new BukkitRunnable() {
            int time = 0;
            final double startAngle = HowaCircle.this.angle;
            double angle = startAngle;
            final double period = theta / ticks;

            @Override
            public void run() {
                time++;
                while (angle - startAngle < period * time) {
                    Location location = center.clone().add(range * Math.cos(Math.toRadians(angle)), 0, range * Math.sin(Math.toRadians(angle)));
                    if (task != null) task.run(location);
                    world.spawnParticle(particle, location, 1, 0, 0, 0, 0);
                    angle += density;
                }
                if (angle >= theta + startAngle) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
