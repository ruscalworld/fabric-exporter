package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.ducks.IChunkStatusByCountArrayGetter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

import java.util.Locale;

public class LoadedChunks extends Metric {
    private final IdentifierFormatter identifierFormatter;

    public LoadedChunks(IdentifierFormatter identifierFormatter) {
        super("loaded_chunks", "Amount of currently loaded chunks on server", "world", "full_chunk_status");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            var worldName = identifierFormatter.getWorldName(world);
            var counts = ((IChunkStatusByCountArrayGetter)world).fabricexporter$getChunkStatusCounter();
            for (var status : FullChunkStatus.values()) {
                if (status == FullChunkStatus.INACCESSIBLE) continue;//We skip over this as its negative and represents the sum of all other status
                this.getGauge().labels(worldName, status.name().toLowerCase(Locale.ROOT)).set(counts[status.ordinal()]);
            }
            this.getGauge().labels(worldName, "raw").set(world.getChunkSource().getLoadedChunksCount());
        }
    }
}
