package com.gmail.kobun127.aulhu.Abilities.Nikutai.Kyouka;

import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Nikutai.NikutaiAbility;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KyoukaEvent implements Listener {
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
            if (itemStack.getType().equals(Material.LIGHT_BLUE_DYE)) {
                if (CooldownManager.isCooldown(player, Material.LIGHT_BLUE_DYE)) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.removeEnchant(Enchantment.LUCK);
                    itemStack.setItemMeta(itemMeta);
                    player.removePotionEffect(PotionEffectType.SPEED);
                } else {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                    itemStack.setItemMeta(itemMeta);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 6, false, false));
                }
            }
            if (itemStack.getType().equals(Material.ORANGE_DYE)) {
                if (CooldownManager.isCooldown(player, Material.ORANGE_DYE)) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                player.setVelocity(player.getVelocity().setY(1.2));
                CooldownManager.setCooldown(player, Material.ORANGE_DYE, 50);
            }
            if (itemStack.getType().equals(Material.RED_DYE)) {
                if (CooldownManager.isCooldown(player, Material.RED_DYE)) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.removeEnchant(Enchantment.LUCK);
                    itemStack.setItemMeta(itemMeta);
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                } else {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                    itemStack.setItemMeta(itemMeta);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 5, false, false));
                }
            }
            if (itemStack.getType().equals(Material.CYAN_DYE)) {
                if (CooldownManager.isCooldown(player, Material.CYAN_DYE)) {
                    return;
                }
                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.removeEnchant(Enchantment.LUCK);
                    itemStack.setItemMeta(itemMeta);
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                } else {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                    itemStack.setItemMeta(itemMeta);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 1, false, false));
                }
            }
        }
    }
}