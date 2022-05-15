package com.gmail.kobun127.aulhu.Abilities.Nikutai.Tuyosou;

import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.NikutaiAbility;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class TuyosouEvent implements Listener {
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
            if (itemStack.getType().equals(Material.NETHER_STAR)) {
                if (CooldownManager.isCooldown(player, Material.NETHER_STAR)) {
                    return;
                }
                CooldownManager.setCooldown(player, Material.NETHER_STAR, 20);
                World world = player.getWorld();
                world.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
                world.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
                world.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                world.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
                world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                world.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            }
        }
    }
}
