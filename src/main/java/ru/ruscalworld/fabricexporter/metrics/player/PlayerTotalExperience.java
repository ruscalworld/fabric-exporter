package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerTotalExperience extends IndividualMetric {
    public PlayerTotalExperience() {
        super("player_total_experience", "Total experience of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.totalExperience;
    }
}
