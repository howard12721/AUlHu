package com.gmail.kobun127.aulhu.Abilities.Flame.Mera;

import com.gmail.kobun127.aulhu.AUlHu;
import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Flame.FlameAbility;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.Random;

public class MeraEvent implements Listener {
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

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (block != null) {
                if ((block.getState() instanceof Container) && !player.isSneaking()) {
                    return;
                }
            }
            if (itemStack.getType().equals(Material.FIRE_CHARGE)) {
                if (CooldownManager.isCooldown(player, Material.FIRE_CHARGE)) {
                    return;
                }

                Location playerEyeLocation = player.getEyeLocation();
                World world = player.getWorld();
                RayTraceResult rayTraceResult = world.rayTrace(
                        playerEyeLocation,
                        playerEyeLocation.getDirection(),
                        36,
                        FluidCollisionMode.NEVER,
                        true,
                        0.5,
                        entity -> entity != player
                );

                if (rayTraceResult == null) return;

                Location hitLocation = rayTraceResult.getHitPosition().toLocation(world);
                Location spawnLocation = hitLocation.clone();
                spawnLocation.add(new Random().nextInt(140) - 70, 180, new Random().nextInt(140) - 70);
                Fireball meteor = world.spawn(spawnLocation, Fireball.class, fireball -> {
                    fireball.setShooter(player);
                    fireball.setBounce(false);
                    fireball.setDirection(hitLocation.toVector().subtract(spawnLocation.toVector()).normalize().multiply(2.5));
                    fireball.setIsIncendiary(false);
                    fireball.setYield(8);
                });
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (meteor.isDead()) cancel();
                        world.spawnParticle(Particle.LAVA, meteor.getLocation(), 30, 2, 2, 2, 0, null, true);
                        player.spawnParticle(Particle.REDSTONE, hitLocation, 8, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.RED, 2));
                    }
                }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
                CooldownManager.setCooldown(player, Material.FIRE_CHARGE, 600);
            }
        }
    }
}
