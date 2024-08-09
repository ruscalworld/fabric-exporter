package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.TextUtil;

public class LoadedChunks extends Metric {
    public LoadedChunks() {
        super("loaded_chunks", "Amount of currently loaded chunks on server", "world");
    }

    @Override
    public void update(@NotNull FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            this.getGauge().labels(TextUtil.getWorldName(world)).set(world.getChunkManager().getLoadedChunkCount());
        }
    }
}
