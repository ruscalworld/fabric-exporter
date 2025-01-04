package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.server.world.ServerWorld;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

public class TotalLoadedChunks extends Metric {
    private final IdentifierFormatter identifierFormatter;

    public TotalLoadedChunks(IdentifierFormatter identifierFormatter) {
        super("total_loaded_chunks", "Amount of total loaded chunks on server", "world");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            this.getGauge().labels(identifierFormatter.getWorldName(world)).set(world.getChunkManager().getTotalChunksLoadedCount());
        }
    }
}
