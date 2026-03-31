package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.entity.EntityTypeTest;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

import java.util.HashMap;

public class Entities extends Metric {
    private static final EntityTypeTest<Entity, ?> ENTITY_FILTER = new EntityTypeTest<>() {
        @Override
        public Entity tryCast(Entity entity) {
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
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            HashMap<Identifier, Integer> currentWorldEntities = new HashMap<>();
            BuiltInRegistries.ENTITY_TYPE.keySet().forEach(id -> currentWorldEntities.put(id, 0));

            world.getEntities(ENTITY_FILTER, entity -> true).forEach(entity -> {
                Identifier entityTypeId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                Integer typeCount = currentWorldEntities.getOrDefault(entityTypeId, 0);
                currentWorldEntities.put(entityTypeId, typeCount + 1);
            });

            for (Identifier entityTypeId : currentWorldEntities.keySet()) {
                Integer count = currentWorldEntities.get(entityTypeId);
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(entityTypeId);

                this.getGauge().labels(
                        identifierFormatter.getWorldName(world),
                        entityType.getCategory().getName(),
                        identifierFormatter.format(entityTypeId)
                ).set(count);
            }
        }
    }
}
