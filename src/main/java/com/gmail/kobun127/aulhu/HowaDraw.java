package com.gmail.kobun127.aulhu;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HowaDraw {
    public static Vector getPolar(double r, double deg) {
        return new Vector(r * Math.cos(Math.toRadians(deg)), 0, r * Math.sin(Math.toRadians(deg)));
    }

    public static Vector getVector(Location start, Location end) {
        return start.clone().multiply(-1).add(end).multiply(1 / start.distance(end)).toVector();
    }

    //通常ライン描画
    public static void drawLine(Particle particle, Location begin, Location end) {
        drawLine(particle, begin, end, 0.3);
    }

    //ライン描画 密度パラメータあり
    public static void drawLine(Particle particle, Location begin, Location end, double density) {

        double distance = begin.distance(end);
        World world = begin.getWorld();
        if (world == null) {
            return;
        }
        Vector unitVector = new Vector(
                (end.getX() - begin.getX()),
                (end.getY() - begin.getY()),
                (end.getZ() - begin.getZ())
        ).multiply(1 / distance);

        Location drawingPos = begin.clone();
        Vector velocity = unitVector.multiply(density);
        for (double dens = 0; dens <= distance; dens += density) {
            world.spawnParticle(particle, drawingPos, 1, 0, 0, 0, 0);
            drawingPos.add(velocity);
        }
    }

    //ラインアニメーション描画 速度パラメータあり
    public static void drawLineTimer(Particle particle, Location begin, Location end, int tick) {
        drawLineTimer(particle, begin, end, tick, 0.3);
    }

    //ラインアニメーション描画 密度パラメータあり
    public static void drawLineTimer(Particle particle, Location begin, Location end, double density) {
        drawLineTimer(particle, begin, end, 20, density);
    }

    //ラインアニメーション描画 速度、密度パラメータあり
    public static void drawLineTimer(Particle particle, Location begin, Location end, int tick, double density) {
        World world = begin.getWorld();
        if (world == null) {
            return;
        }
        new BukkitRunnable() {
            int time = 0;
            final double distance = begin.distance(end);
            final double period = distance / tick;
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
                    world.spawnParticle(particle, drawingPos, 1, 0, 0, 0, 0);
                    drawingPos.add(velocity);
                    dens += density;
                }

                if (dens >= distance) {
                    cancel();
                }
            }
        }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
    }

    //通常円描画
    public static void drawCircle(Particle particle, Location center, double r) {
        drawCircle(particle, center, r, 10);
    }

    //円描画 密度パラメータあり
    public static void drawCircle(Particle particle, Location center, double r, double density) {
        World world = center.getWorld();
        if (world == null) {
            return;
        }

        for (double angle = 0; angle < 360; angle += density) {
            world.spawnParticle(particle, center.clone().add(r * Math.cos(Math.toRadians(angle)), 0, r * Math.sin(Math.toRadians(angle))), 1, 0, 0, 0, 0);
        }
    }

    //円アニメーション描画 速度パラメータあり
    public static void drawCircleTimer(Particle particle, Location center, double r, int tick) {
        drawCircleTimer(particle, center, r, tick, 10);
    }

    //円アニメーション描画 密度パラメータあり
    public static void drawCircleTimer(Particle particle, Location center, double r, double density) {
        drawCircleTimer(particle, center, r, 20, density);
    }

    //円アニメーション描画 速度、密度パラメータあり
    public static void drawCircleTimer(Particle particle, Location center, double r, int tick, double density) {
        World world = center.getWorld();
        if (world == null) {
            return;
        }
        new BukkitRunnable() {
            int time = 0;
            double angle = 0;
            final double period = 360.0 / tick;

            @Override
            public void run() {
                time++;
                while (angle < period * time) {
                    world.spawnParticle(particle, center.clone().add(r * Math.cos(Math.toRadians(angle)), 0, r * Math.sin(Math.toRadians(angle))), 1, 0, 0, 0, 0);
                    angle += density;
                }
                if (angle >= 360) {
                    cancel();
                }
            }
        }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
    }
}

