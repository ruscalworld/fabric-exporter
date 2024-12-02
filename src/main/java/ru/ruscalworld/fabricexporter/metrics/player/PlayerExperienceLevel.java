package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerExperienceLevel extends IndividualMetric {
    public PlayerExperienceLevel() {
        super("player_experience_level", "Current experience levels of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.experienceLevel;
    }
}
