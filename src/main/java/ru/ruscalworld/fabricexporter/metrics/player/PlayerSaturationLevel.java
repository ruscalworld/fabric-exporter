package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerSaturationLevel extends IndividualMetric {
    public PlayerSaturationLevel() {
        super("player_saturation_level", "Current saturation level of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.getHungerManager().getSaturationLevel();
    }
}
