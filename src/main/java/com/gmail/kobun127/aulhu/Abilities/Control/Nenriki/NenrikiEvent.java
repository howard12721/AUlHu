package com.gmail.kobun127.aulhu.Abilities.Control.Nenriki;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class NenrikiEvent implements Listener {
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
            if (itemStack.getType().equals(Material.BLAZE_POWDER)) {
                if (CooldownManager.isCooldown(player, Material.BLAZE_POWDER)) {
                    return;
                }
                if (Nenriki.usingAbility(player)) {
                    Nenriki.cancelTask(player);
                    player.stopSound(Sound.BLOCK_BEACON_ACTIVATE);
                    player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                    CooldownManager.setCooldown(player, Material.BLAZE_POWDER, 100);
                    return;
                }

                World world = player.getWorld();
                Location location = player.getEyeLocation();

                RayTraceResult rayTraceResult = world.rayTrace(
                        location,
                        location.getDirection(),
                        24.0,
                        FluidCollisionMode.NEVER,
                        true,
                        0,
                        entity -> entity != player
                );

                if (rayTraceResult == null) return;
                Entity entity = rayTraceResult.getHitEntity();
                if (entity == null) return;
                if (!(entity instanceof LivingEntity)) return;
                player.playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
                Nenriki.putTask(player, new BukkitRunnable() {
                    final LivingEntity target = (LivingEntity) entity;
                    final double distance = player.getEyeLocation().distance(rayTraceResult.getHitPosition().toLocation(player.getWorld()));
                    int timer = 90;
                    final double maxSpeed = 2.2;
                    final double offset = -target.getEyeHeight() / 2;
                    Location recentTargetLocation = target.getLocation();
                    @Override
                    public void run() {
                        Location nowLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(distance)).add(0, offset, 0);
                        Vector velocity = nowLocation.toVector().subtract(target.getLocation().toVector());
                        if (velocity.length() >= maxSpeed) {
                            velocity.multiply(maxSpeed / velocity.length());
                        }
                        target.setVelocity(velocity);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1, false, false));
                        HowaDraw.drawCircle(Particle.SPELL_WITCH, player.getLocation().add(0, 1.2, 0), 4);
                        HowaDraw.drawLineTimer(Particle.ENCHANTMENT_TABLE, player.getLocation().add(0, 1, 0), target.getLocation().add(0, -offset, 0), 18, 1);
                        HowaDraw.drawLineTimer(Particle.ENCHANTMENT_TABLE, player.getLocation().add(2.5, 3, 0), target.getLocation().add(0, -offset, 0), 18, 1);
                        HowaDraw.drawLineTimer(Particle.ENCHANTMENT_TABLE, player.getLocation().add(-2.5, 3, 0), target.getLocation().add(0, -offset, 0), 18, 1);
                        HowaDraw.drawLineTimer(Particle.ENCHANTMENT_TABLE, player.getLocation().add(0, 3, 2.5), target.getLocation().add(0, -offset, 0), 18, 1);
                        HowaDraw.drawLineTimer(Particle.ENCHANTMENT_TABLE, player.getLocation().add(0, 3, -2.5), target.getLocation().add(0, -offset, 0), 18, 1);
                        HowaDraw.drawLineTimer(Particle.END_ROD, recentTargetLocation.add(0, -offset, 0), target.getLocation().add(0, -offset, 0), 15, 0.2);
                        recentTargetLocation = target.getLocation();
                        timer--;
                        if (timer <= 0 || target.isDead()) {
                            CooldownManager.setCooldown(player, Material.BLAZE_POWDER, 200);
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                            cancel();
                        }
                    }
                }.runTaskTimer(AUlHu.getPlugin(), 0, 1));
            }
        }
    }
}
