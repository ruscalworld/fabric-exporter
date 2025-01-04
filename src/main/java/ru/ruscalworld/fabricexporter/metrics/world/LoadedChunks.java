package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.server.world.ServerWorld;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

public class LoadedChunks extends Metric {
    private final IdentifierFormatter identifierFormatter;

    public LoadedChunks(IdentifierFormatter identifierFormatter) {
        super("loaded_chunks", "Amount of currently loaded chunks on server", "world");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            this.getGauge().labels(identifierFormatter.getWorldName(world)).set(world.getChunkManager().getLoadedChunkCount());
        }
    }
}
