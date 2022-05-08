package com.gmail.kobun127.aulhu.Abilities.Control.Tama;

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
import org.bukkit.util.Vector;

import java.util.Objects;

public class TamaEvent implements Listener {
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
            if (itemStack.getType().equals(Material.DIAMOND)) {
                if (CooldownManager.isCooldown(player, Material.DIAMOND)) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
                CooldownManager.setCooldown(player, Material.DIAMOND, 70);
                new BukkitRunnable() {
                    final Location centerLocation = player.getLocation().add(0, 0.8, 0);
                    final Vector moveVector = centerLocation.getDirection().multiply(0.8);
                    int timer = 70;
                    @Override
                    public void run() {
                        World world = centerLocation.getWorld();
                        Objects.requireNonNull(world).spawnParticle(Particle.REDSTONE, centerLocation, 60, 0.5, 0.5, 0.5, 0, new Particle.DustOptions(Color.AQUA, 1));
                        centerLocation.add(moveVector);
                        for (Entity entity : Objects.requireNonNull(world).getNearbyEntities(centerLocation, 0.7, 0.7, 0.7)) {
                            if (entity == player) continue;
                            if (entity instanceof LivingEntity) {
                                ((LivingEntity) entity).damage(16, player);
                            }
                        }
                        timer--;
                        if (timer <= 0) {
                            cancel();
                        }
                    }
                }.runTaskTimer(AUlHu.getPlugin(), 0, 1);
            }
        }
    }
}
