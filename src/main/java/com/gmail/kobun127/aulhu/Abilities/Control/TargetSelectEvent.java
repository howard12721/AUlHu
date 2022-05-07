package com.gmail.kobun127.aulhu.Abilities.Control;

import com.gmail.kobun127.aulhu.AUlHu;
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

public class TargetSelectEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!ControlAbility.isController(player))  {
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
                if (ControlAbility.usingAbility(player)) {
                    ControlAbility.cancelTask(player);
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

                ControlAbility.putTask(player, new BukkitRunnable() {
                    final LivingEntity target = (LivingEntity) entity;
                    final double distance = player.getEyeLocation().distance(rayTraceResult.getHitPosition().toLocation(player.getWorld()));
                    Location recentLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(distance));
                    int timer = 120;
                    @Override
                    public void run() {
                        Location nowLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(distance));
                        target.setVelocity(nowLocation.toVector().subtract(recentLocation.toVector()));
                        recentLocation = nowLocation;
                        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1, false, false));
                        timer--;
                        if (timer <= 0) cancel();
                    }
                }.runTaskTimer(AUlHu.getPlugin(), 0, 1));
            }
        }
    }
}
