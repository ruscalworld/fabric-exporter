package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

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

    private final IdentifierFormatter identifierFormatter;

    public Entities(IdentifierFormatter identifierFormatter) {
        super("entities", "Amount of currently loaded entities", "world", "group", "type");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            HashMap<Identifier, Integer> currentWorldEntities = new HashMap<>();
            Registries.ENTITY_TYPE.getIds().forEach(id -> currentWorldEntities.put(id, 0));

            world.getEntitiesByType(ENTITY_FILTER, entity -> true).forEach(entity -> {
                Identifier entityTypeId = Registries.ENTITY_TYPE.getId(entity.getType());
                Integer typeCount = currentWorldEntities.getOrDefault(entityTypeId, 0);
                currentWorldEntities.put(entityTypeId, typeCount + 1);
            });

            for (Identifier entityTypeId : currentWorldEntities.keySet()) {
                Integer count = currentWorldEntities.get(entityTypeId);
                EntityType<?> entityType = Registries.ENTITY_TYPE.get(entityTypeId);

                this.getGauge().labels(
                        identifierFormatter.getWorldName(world),
                        entityType.getSpawnGroup().getName(),
                        identifierFormatter.format(entityTypeId)
                ).set(count);
            }
        }
    }
}
