package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.server.world.ServerWorld;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.TextUtil;

public class TotalLoadedChunks extends Metric {
    public TotalLoadedChunks() {
        super("total_loaded_chunks", "Amount of total loaded chunks on server", "world");
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            this.getGauge().labels(TextUtil.getWorldName(world)).set(world.getChunkManager().getTotalChunksLoadedCount());
        }
    }
}
