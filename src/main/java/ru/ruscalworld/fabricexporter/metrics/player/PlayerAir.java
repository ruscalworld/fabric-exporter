package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerAir extends IndividualMetric {
    public PlayerAir() {
        super("player_air", "Current air level of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.getAir();
    }
}
