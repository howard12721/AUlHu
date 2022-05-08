package com.gmail.kobun127.aulhu.Abilities;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static final Map<UUID, Map<Material, CooldownData>> cooldownMaps = new HashMap<>();

    public static void setCooldown(Player player, Material key, int tick) {
        if (cooldownMaps.containsKey(player.getUniqueId())) {
            cooldownMaps.get(player.getUniqueId()).put(key, new CooldownData(tick));
        } else {
            Map<Material, CooldownData> cooldownMap = new HashMap<>();
            cooldownMap.put(key, new CooldownData(tick));
            cooldownMaps.put(player.getUniqueId(), cooldownMap);
        }
    }

    public static boolean isCooldown(Player player, Material key) {
        Map<Material, CooldownData> cooldownMap = cooldownMaps.get(player.getUniqueId());
        if (cooldownMap == null) return false;
        return cooldownMap.containsKey(key);
    }

    public static void update() {
        for (Iterator<UUID> UUIDIterator = cooldownMaps.keySet().iterator(); UUIDIterator.hasNext(); ) {
            UUID uuid = UUIDIterator.next();
            Map<Material, CooldownData> cooldownMap = cooldownMaps.get(uuid);
            for (Iterator<Material> iterator = cooldownMap.keySet().iterator(); iterator.hasNext(); ) {
                Material key = iterator.next();
                CooldownData cooldownData = cooldownMap.get(key);
                cooldownData.count();
                if (cooldownData.isEnded()) iterator.remove();
            }
            if (cooldownMap.size() <= 0) {
                UUIDIterator.remove();
            }
        }
    }

    public static void updateExpBar(Player player) {
        player.setLevel(0);
        player.setExp(1.0f);
        Material material = player.getInventory().getItemInMainHand().getType();
        if (cooldownMaps.containsKey(player.getUniqueId())) {
            CooldownData cooldownData = cooldownMaps.get(player.getUniqueId()).get(material);
            if (cooldownData == null) return;
            player.setExp((float) (1 - (cooldownData.getRemainCooldown() / cooldownData.getTotalCooldown())));
        }
    }
}
