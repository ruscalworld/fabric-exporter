package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerHealth extends IndividualMetric {
    public PlayerHealth() {
        super("player_health", "Current health of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.getHealth();
    }
}
