package ru.ruscalworld.fabricexporter.metrics.player;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerArmor extends IndividualMetric {
    public PlayerArmor() {
        super("player_armor", "Current armor level of online players");
    }

    @Override
    protected double getValue(@NotNull ServerPlayerEntity player) {
        return player.getArmor();
    }
}
