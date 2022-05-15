package com.gmail.kobun127.aulhu.Abilities.Flame.Hienbasira;

import com.gmail.kobun127.aulhu.AUlHu;
import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Flame.FlameAbility;
import com.gmail.kobun127.aulhu.HowaDraw.Circle.HowaCircle;
import com.gmail.kobun127.aulhu.HowaDraw.HowaDraw;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class HienbasiraEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!FlameAbility.isFlamer(player)) {
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getItemMeta() == null) return;

        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (block != null) {
                if ((block.getState() instanceof Container) && !player.isSneaking()) {
                    return;
                }
            }
            if (itemStack.getType().equals(Material.BLAZE_ROD)) {
                if (CooldownManager.isCooldown(player, Material.BLAZE_ROD)) {
                    return;
                }
                CooldownManager.setCooldown(player, Material.BLAZE_ROD, 40);

                if (player.getWorld().getBlockAt(player.getLocation().add(0, -0.01, 0)).getType().equals(Material.AIR)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.setVelocity(new Vector(0, -1.5, 0));
                            if (!player.getWorld().getBlockAt(player.getLocation().add(0, -0.01, 0)).getType().equals(Material.AIR)) {
                                Location location = player.getLocation();
                                new BukkitRunnable() {
                                    int count = 0;

                                    @Override
                                    public void run() {
                                        hasira(player, location.add(location.getDirection().setY(0).normalize().multiply(3)));
                                        count++;
                                        if (count >= 4) {
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(AUlHu.getPlugin(), 0, 3);
                                cancel();
                            }
                        }
                    }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
                } else {
                    Location location = player.getLocation();
                    new BukkitRunnable() {
                        int count = 0;

                        @Override
                        public void run() {
                            hasira(player, location.add(location.getDirection().setY(0).normalize().multiply(3)));
                            count++;
                            if (count >= 4) {
                                cancel();
                            }
                        }
                    }.runTaskTimer(AUlHu.getPlugin(), 0, 3);
                }
            }
        }
    }

    public void hasira(Player owner, Location location) {
        new BukkitRunnable() {
            final World world = Objects.requireNonNull(location.getWorld());
            final Location fireBallPos = location.clone();
            int timer = 8;

            @Override
            public void run() {
                if (timer == 8) {
                    world.playSound(fireBallPos, Sound.ENTITY_GHAST_SHOOT, 1, 1);
                }
                new HowaCircle(AUlHu.getPlugin())
                        .setParticle(Particle.FLAME)
                        .setCenter(location)
                        .setRange(1)
                        .setDensity(20)
                        .draw();
                fireBallPos.add(0, 0.3, 0);
                world.spawnParticle(Particle.LAVA, fireBallPos, 12, 0.2, 0.2, 0.2, 0);
                world.spawnParticle(Particle.FLAME, fireBallPos, 10, 0.5, (fireBallPos.getY() - location.getY()) / 2, 0.5, 0);
                for (Entity entity : world.getNearbyEntities(fireBallPos, 1, 1, 1)) {
                    if (!(entity instanceof LivingEntity)) continue;
                    if (entity == owner) continue;
                    LivingEntity target = (LivingEntity) entity;
                    target.damage(8, owner);
                    target.setFireTicks(Math.max(target.getFireTicks(), 40));
                }
                timer--;
                if (timer <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
    }
}
