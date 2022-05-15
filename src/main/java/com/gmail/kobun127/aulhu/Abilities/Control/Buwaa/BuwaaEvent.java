package com.gmail.kobun127.aulhu.Abilities.Control.Buwaa;

import com.gmail.kobun127.aulhu.AUlHu;
import com.gmail.kobun127.aulhu.Abilities.Control.ControlAbility;
import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.HowaDraw.HowaDraw;
import com.gmail.kobun127.aulhu.HowaDraw.Line.HowaLine;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
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

public class BuwaaEvent implements Listener {
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
            if (itemStack.getType().equals(Material.GLOWSTONE_DUST)) {
                if (CooldownManager.isCooldown(player, Material.GLOWSTONE_DUST)) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1, 1);
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
                CooldownManager.setCooldown(player, Material.GLOWSTONE_DUST, 14);
                new BukkitRunnable() {
                    final Location centerLocation = player.getLocation().add(0, 0.8, 0);
                    final float yaw = centerLocation.getYaw();
                    final Vector moveVector = centerLocation.getDirection().multiply(0.5);
                    Location leftEdge;
                    Location rightEdge;
                    int timer = 60;

                    @Override
                    public void run() {
                        leftEdge = centerLocation.clone().add(HowaDraw.getPolar(2, yaw)).add(0, Math.sin(Math.toRadians(timer * 20)) / 1.4, 0);
                        rightEdge = centerLocation.clone().add(HowaDraw.getPolar(2, yaw + 180)).add(0, Math.cos(Math.toRadians(timer * 20)) / 1.4, 0);
                        new HowaLine(AUlHu.getPlugin())
                                .setParticle(Particle.SWEEP_ATTACK)
                                .setBegin(leftEdge)
                                .setEnd(rightEdge)
                                .setDensity(0.8)
                                .draw();
                        centerLocation.add(moveVector);
                        World world = centerLocation.getWorld();
                        for (Entity entity : Objects.requireNonNull(world).getNearbyEntities(centerLocation, 2, 1, 2)) {
                            if (entity == player) continue;
                            entity.setVelocity(moveVector.clone().multiply(0.8).setY(1.2));
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
