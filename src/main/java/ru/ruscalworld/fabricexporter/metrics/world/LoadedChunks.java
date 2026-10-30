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
        // Drop series for worlds that no longer exist (e.g. dynamically
        // created dimensions), otherwise their last value lingers forever.
        this.getGauge().clear();
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            var worldName = identifierFormatter.getWorldName(world);
            var counts = ((IChunkStatusByCountArrayGetter)world).fabricexporter$getChunkStatusCounter();
            int accessibleCount = 0;
            for (var status : FullChunkStatus.values()) {
                // The counter of INACCESSIBLE is never incremented when a chunk gets loaded,
                // so it just holds the negated sum of other statuses and can't be used directly
                if (status == FullChunkStatus.INACCESSIBLE) continue;
                accessibleCount += counts[status.ordinal()];
                this.getGauge().labels(worldName, status.name().toLowerCase(Locale.ROOT)).set(counts[status.ordinal()]);
            }

            // Loaded chunks that haven't reached FULL status yet (e.g. ones being generated).
            // Values are read off the server thread, so the difference may briefly become negative.
            int inaccessibleCount = Math.max(0, world.getChunkSource().getLoadedChunksCount() - accessibleCount);
            this.getGauge().labels(worldName, FullChunkStatus.INACCESSIBLE.name().toLowerCase(Locale.ROOT)).set(inaccessibleCount);
        }
    }
}
