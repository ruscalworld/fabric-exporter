package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerFoodLevel extends IndividualMetric {
    public PlayerFoodLevel() {
        super("player_food_level", "Current food level of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.getHungerManager().getFoodLevel();
    }
}
