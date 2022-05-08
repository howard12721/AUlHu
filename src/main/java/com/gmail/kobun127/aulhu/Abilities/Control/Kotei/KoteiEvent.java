package com.gmail.kobun127.aulhu.Abilities.Control.Kotei;

import com.gmail.kobun127.aulhu.AUlHu;
import com.gmail.kobun127.aulhu.Abilities.Control.ControlAbility;
import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
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
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class KoteiEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!ControlAbility.isController(player)) {
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
            if (itemStack.getType().equals(Material.NETHER_STAR)) {
                if (CooldownManager.isCooldown(player, Material.NETHER_STAR)) {
                    return;
                }
                World world = player.getWorld();
                Location playerEyeLocation = player.getEyeLocation();

                RayTraceResult rayTraceResult = world.rayTrace(
                        playerEyeLocation,
                        playerEyeLocation.getDirection(),
                        30,
                        FluidCollisionMode.NEVER,
                        true,
                        0,
                        entity -> entity != player
                );

                if (rayTraceResult == null) return;

                Entity targetEntity = rayTraceResult.getHitEntity();
                if (targetEntity == null) return;

                if (!(targetEntity instanceof LivingEntity)) return;

                player.playSound(playerEyeLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                CooldownManager.setCooldown(player, Material.NETHER_STAR, 600);
                new BukkitRunnable() {
                    int timer = 120;
                    @Override
                    public void run() {
                        targetEntity.setVelocity(new Vector(0, -1, 0));
                        targetEntity.getWorld().spawnParticle(Particle.SMOKE_LARGE, targetEntity.getLocation(), 3, 0.5, 0.5, 0.5, 0);
                        timer--;
                        if (timer % 5 == 0) {
                            ((LivingEntity) targetEntity).setNoDamageTicks(0);
                            ((LivingEntity) targetEntity).damage(1);
                        }
                        if (timer <= 0) {
                            cancel();
                        }
                    }
                }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
            }
        }
    }
}
