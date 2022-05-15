package com.gmail.kobun127.aulhu.Abilities.Nikutai.Baan;

import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.NikutaiAbility;
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

public class BaanEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!NikutaiAbility.isNikutaier(player)) {
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
            if (itemStack.getType().equals(Material.BROWN_DYE)) {
                if (CooldownManager.isCooldown(player, Material.BROWN_DYE)) {
                    return;
                }
                CooldownManager.setCooldown(player, Material.BROWN_DYE, 60);
                World world = player.getWorld();
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1);
                world.spawnParticle(Particle.BLOCK_DUST, player.getLocation(), 600, 4, 0, 4, 0, Bukkit.createBlockData(Material.DIRT));
                for (Entity target : world.getNearbyEntities(player.getLocation(), 4, 1, 4, entity -> entity != player)) {
                    if (target instanceof LivingEntity) {
                        ((LivingEntity) target).damage(12, player);
                    }
                }
            }
        }
    }
}
