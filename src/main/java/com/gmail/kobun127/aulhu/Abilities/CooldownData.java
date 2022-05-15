package com.gmail.kobun127.aulhu.Abilities;

public class CooldownData {
    private final double totalCooldown;
    private double remainCooldown;
    public CooldownData(int cooldown) {
        totalCooldown = cooldown;
        remainCooldown = cooldown;
    }

    public void count() {
        remainCooldown -= 1.0;
    }

    public boolean isEnded() {
        return remainCooldown <= 0;
    }

    public double getTotalCooldown() {
        return totalCooldown;
    }

    public double getRemainCooldown() {
        return remainCooldown;
    }
}
