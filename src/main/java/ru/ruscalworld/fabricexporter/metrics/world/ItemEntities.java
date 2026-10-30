package ru.ruscalworld.fabricexporter.metrics.world;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.entity.item.ItemEntity;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

import java.util.HashMap;

public class ItemEntities extends Metric {
    private final IdentifierFormatter identifierFormatter;

    public ItemEntities(IdentifierFormatter identifierFormatter) {
        super("item_entities", "Item entities per item type", "world", "item");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        // Only item types that are currently present are exported, so series of
        // despawned or picked up items must be dropped, otherwise they linger forever.
        this.getGauge().clear();
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            HashMap<Identifier, Integer> currentWorldItems = new HashMap<>();
            String worldName = this.identifierFormatter.getWorldName(world);

            world.getEntities(EntityTypeTest.forClass(ItemEntity.class), item -> true).forEach(item -> {
                item.getItem().typeHolder().unwrapKey().ifPresent(itemId -> {
                    currentWorldItems.merge(itemId.identifier(), 1, Integer::sum);
                });
            });

            for (Identifier itemTypeId : currentWorldItems.keySet()) {
                this.getGauge().labels(
                        worldName,
                        identifierFormatter.format(itemTypeId)
                ).set(currentWorldItems.get(itemTypeId));
            }
        }
    }
}
