package ru.ruscalworld.fabricexporter.metrics;

import net.minecraft.server.world.ServerWorld;
import ru.ruscalworld.fabricexporter.FabricExporter;

public class OnlinePlayers extends Metric {
    public OnlinePlayers() {
        super("players_online", "Amount of currently online players on the server", "world");
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            this.getGauge().labels(world.getRegistryKey().getValue().getPath()).set(world.getPlayers().size());
        }
    }
}
