package com.gmail.kobun127.aulhu.Abilities.Flame.Kaenhai;

import com.gmail.kobun127.aulhu.AUlHu;
import com.gmail.kobun127.aulhu.Abilities.CooldownManager;
import com.gmail.kobun127.aulhu.Abilities.Flame.FlameAbility;
import com.gmail.kobun127.aulhu.HowaDraw.Circle.HowaCircle;
import com.gmail.kobun127.aulhu.HowaDraw.Runnable.HowaDrawRunnable;
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

import java.util.Objects;

public class KaenhaiEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!FlameAbility.isFlamer(player)) {
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
            if (itemStack.getType().equals(Material.BLAZE_ROD)) {
                if (CooldownManager.isCooldown(player, Material.BLAZE_ROD)) {
                    return;
                }
                CooldownManager.setCooldown(player, Material.BLAZE_ROD, 100);

                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
                for (int i = 8; i >= 3; i--) {
                    new HowaCircle(AUlHu.getPlugin()).setParticle(Particle.LAVA)
                            .setCenter(player.getLocation().add(0, 0.5, 0))
                            .setRange(i)
                            .setTicks(80)
                            .setDensity(3)
                            .setTheta(360 * 6)
                            .setAngle(player.getLocation().getYaw() + 45)
                            .setTask(new HowaDrawRunnable() {
                                @Override
                                public void run(Location location) {
                                    World world = Objects.requireNonNull(location.getWorld());
                                    for (Entity entity : world.getNearbyEntities(location, 1, 1, 1, entity -> entity != player)) {
                                        if (!(entity instanceof LivingEntity)) continue;
                                        LivingEntity target = (LivingEntity) entity;
                                        target.damage(0.1, player);
                                        target.setFireTicks(Math.max(target.getFireTicks(), 40));
                                        target.setNoDamageTicks(0);
                                    }
                                }
                            })
                            .draw();
                }
            }
        }
    }
}
