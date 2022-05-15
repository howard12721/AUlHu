package com.gmail.kobun127.aulhu.HowaDraw.Line;

import com.gmail.kobun127.aulhu.HowaDraw.Runnable.HowaDrawRunnable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HowaLine {
    public HowaLine(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Plugin plugin;
    private Location begin;
    private Location end;
    private double density = 0.3;
    private int ticks = 1;
    private Particle particle = Particle.END_ROD;
    private HowaDrawRunnable task = null;

    public HowaLine setDensity(double density) {
        this.density = density;
        return this;
    }

    public HowaLine setBegin(Location begin) {
        this.begin = begin;
        return this;
    }

    public HowaLine setEnd(Location end) {
        this.end = end;
        return this;
    }

    public HowaLine setTicks(int ticks) {
        if (ticks == 0) ticks = 1;
        this.ticks = ticks;
        return this;
    }

    public HowaLine setParticle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public HowaLine setTask(HowaDrawRunnable howaDrawRunnable) {
        this.task = howaDrawRunnable;
        return this;
    }

    public void draw() {
        World world = begin.getWorld();
        if (world == null) {
            return;
        }
        new BukkitRunnable() {
            int time = 0;
            final double distance = begin.distance(end);
            final double period = distance / ticks;
            double dens = 0;
            final Vector unitVector = new Vector(
                    (end.getX() - begin.getX()),
                    (end.getY() - begin.getY()),
                    (end.getZ() - begin.getZ())
            ).multiply(1 / distance);
            final Location drawingPos = begin.clone();
            final Vector velocity = unitVector.multiply(density);

            @Override
            public void run() {
                time++;
                while (dens <= period * time) {
                    if (task != null) task.run(drawingPos);
                    world.spawnParticle(particle, drawingPos, 1, 0, 0, 0, 0);
                    drawingPos.add(velocity);
                    dens += density;
                }

                if (dens >= distance) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
