package ru.ruscalworld.fabricexporter.metrics.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;

public abstract class IndividualMetric extends Metric {
    public IndividualMetric(String name, String help) {
        super(name, help, "uuid", "name");
    }

    @Override
    public final void update(@NotNull FabricExporter exporter) {
        PlayerManager playerManager = exporter.getServer().getPlayerManager();
        this.getGauge().clear();

        playerManager.getPlayerList().forEach(player -> {
            GameProfile profile = player.getGameProfile();

            this.getGauge().labels(
                    profile.getId().toString(),
                    profile.getName()
            ).set(this.getValue(player));
        });
    }

    protected abstract double getValue(@NotNull ServerPlayerEntity player);
}
