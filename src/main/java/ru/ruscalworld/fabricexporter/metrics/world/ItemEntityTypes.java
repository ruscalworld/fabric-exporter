package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.entity.item.ItemEntity;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

import java.util.HashMap;

public class ItemEntityTypes extends Metric {
    private final IdentifierFormatter identifierFormatter;

    public ItemEntityTypes(IdentifierFormatter identifierFormatter) {
        super("item_entity_by_item", "Item entities by item", "world", "item");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            HashMap<Identifier, Integer> currentWorldItems = new HashMap<>();
            String worldName = this.identifierFormatter.getWorldName(world);

            BuiltInRegistries.ITEM.keySet().forEach(id -> currentWorldItems.put(id, 0));

            world.getEntities(EntityTypeTest.forClass(ItemEntity.class), item -> true).forEach(item -> {
                item.getItem().typeHolder().unwrapKey().ifPresent(itemId -> {
                    Integer typeCount = currentWorldItems.getOrDefault(itemId.identifier(), 0);
                    currentWorldItems.put(itemId.identifier(), typeCount + 1);
                });
            });

            for (Identifier itemTypeId: currentWorldItems.keySet()) {
                Integer count = currentWorldItems.get(itemTypeId);
                if (count > 0) {
                    this.getGauge().labels(
                            worldName,
                            identifierFormatter.format(itemTypeId)
                    ).set(count);
                }
            }
        }
    }
}
