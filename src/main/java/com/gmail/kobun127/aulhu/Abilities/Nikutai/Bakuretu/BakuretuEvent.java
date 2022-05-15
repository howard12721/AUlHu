package com.gmail.kobun127.aulhu.Abilities.Nikutai.Bakuretu;

import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.NikutaiAbility;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BakuretuEvent implements Listener {
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

        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
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
                CooldownManager.setCooldown(player, Material.FIRE_CHARGE, 100);
                World world = player.getWorld();
                for (int i = 2; i <= 6; i++) {
                    world.createExplosion(player.getLocation().add(player.getEyeLocation().getDirection().multiply(i)), 2, false, false, player);
                }
                player.setVelocity(player.getLocation().getDirection().multiply(-2));
            }
        }
    }
}
