package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.server.level.ServerLevel;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

public class SpawnableChunks extends Metric {
    private final IdentifierFormatter identifierFormatter;

    public SpawnableChunks(IdentifierFormatter identifierFormatter) {
        super("spawnable_chunks", "Spawnable chunk count from spawn data", "world");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            var spawnState = world.getChunkSource().getLastSpawnState();
            int count = 0;
            if (spawnState != null) {
                count = spawnState.getSpawnableChunkCount();
            }
            this.getGauge().labels(this.identifierFormatter.getWorldName(world)).set(count);
        }
    }
}
