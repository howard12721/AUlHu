package com.gmail.kobun127.aulhu.Abilities.Control.Hikiyose;

import com.gmail.kobun127.aulhu.AUlHu;
import com.gmail.kobun127.aulhu.Abilities.Control.ControlAbility;
import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.HowaDraw;
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

public class HikiyoseEvent implements Listener {
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
            if (itemStack.getType().equals(Material.GREEN_DYE)) {
                if (CooldownManager.isCooldown(player, Material.GREEN_DYE)) {
                    return;
                }
                World world = player.getWorld();
                Location playerEyeLocation = player.getEyeLocation();

                RayTraceResult rayTraceResult = world.rayTrace(
                        playerEyeLocation,
                        playerEyeLocation.getDirection(),
                        36,
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
                player.playSound(playerEyeLocation, Sound.BLOCK_PISTON_CONTRACT, 1, 1);
                CooldownManager.setCooldown(player, Material.GREEN_DYE, 140);
                new BukkitRunnable() {
                    int timer = 40;
                    @Override
                    public void run() {
                        Vector subtract = player.getLocation().toVector().subtract(targetEntity.getLocation().toVector());
                        targetEntity.setVelocity(subtract.clone().normalize().add(new Vector(0, 0.3, 0)));
                        HowaDraw.drawLine(Particle.CRIT, targetEntity.getLocation().add(0, targetEntity.getHeight() / 2, 0), player.getLocation());
                        timer--;
                        if (timer <= 0 || subtract.length() <= 1.5) {
                            cancel();
                        }
                    }
                }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
            }
        }
    }
}
