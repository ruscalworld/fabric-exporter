package ru.ruscalworld.fabricexporter.metrics.world;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

public class SpawnedMobs extends Metric {
    private static final Object2IntMap<MobCategory> EMPTY = new Object2IntOpenHashMap<>(0);
    private final IdentifierFormatter identifierFormatter;

    public SpawnedMobs(IdentifierFormatter identifierFormatter) {
        super("spawned_mobs", "Per category mob counts from spawn data", "world", "category");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            var spawnState = world.getChunkSource().getLastSpawnState();
            var worldName = this.identifierFormatter.getWorldName(world);
            var counts = EMPTY;
            if (spawnState != null) {
                counts = spawnState.getMobCategoryCounts();
            }
            for (var category : MobCategory.values()) {
                this.getGauge().labels(worldName, category.getSerializedName()).set(counts.getOrDefault(category, 0));
            }
        }
    }
}

