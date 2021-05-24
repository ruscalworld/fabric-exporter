package ru.ruscalworld.fabricexporter.metrics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.util.TextUtil;

import java.util.HashMap;

public class Entities extends Metric {
    public Entities() {
        super("entities", "Amount of entities in the world", "world", "group", "type");
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            HashMap<String, Integer> currentWorldEntities = new HashMap<>();

            for (Entity entity : world.getEntitiesByType(null, entity -> true)) {
                String name = Registry.ENTITY_TYPE.getId(entity.getType()).getPath();
                Integer typeCount = currentWorldEntities.getOrDefault(name, 0);
                currentWorldEntities.put(name, typeCount + 1);
            }

            for (String type : currentWorldEntities.keySet()) {
                Integer count = currentWorldEntities.get(type);
                EntityType<?> entityType = Registry.ENTITY_TYPE.get(new Identifier(type));
                this.getGauge().labels(TextUtil.getWorldName(world), entityType.getSpawnGroup().getName(), type).set(count);
            }
        }
    }
}
