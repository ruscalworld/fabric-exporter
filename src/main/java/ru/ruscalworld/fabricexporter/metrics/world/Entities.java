package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.TextUtil;

import java.util.HashMap;

public class Entities extends Metric {
    private static final TypeFilter<Entity, ?> ENTITY_FILTER = new TypeFilter<>() {
        @Override
        public Entity downcast(Entity entity) {
            return entity;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    };

    public Entities() {
        super("entities", "Amount of currently loaded entities", "world", "group", "type");
    }

    @Override
    public void update(@NotNull FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            HashMap<String, Integer> currentWorldEntities = new HashMap<>();
            Registries.ENTITY_TYPE.getIds().forEach(id -> currentWorldEntities.put(id.getPath(), 0));

            world.getEntitiesByType(ENTITY_FILTER, entity -> true).forEach(entity -> {
                String name = Registries.ENTITY_TYPE.getId(entity.getType()).getPath();
                Integer typeCount = currentWorldEntities.getOrDefault(name, 0);
                currentWorldEntities.put(name, typeCount + 1);
            });

            for (String type : currentWorldEntities.keySet()) {
                Integer count = currentWorldEntities.get(type);
                EntityType<?> entityType = Registries.ENTITY_TYPE.get(Identifier.of(type));
                this.getGauge().labels(TextUtil.getWorldName(world), entityType.getSpawnGroup().getName(), type).set(count);
            }
        }
    }
}
