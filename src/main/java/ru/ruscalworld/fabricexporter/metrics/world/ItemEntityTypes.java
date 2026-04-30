package ru.ruscalworld.fabricexporter.metrics.world;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

public class ItemEntityTypes extends Metric {
    private final IdentifierFormatter identifierFormatter;
    public ItemEntityTypes(IdentifierFormatter identifierFormatter) {
        super("item_entity_by_item", "Item entities by item", "world", "item");
        this.identifierFormatter = identifierFormatter;
    }

    @Override
    public void update(FabricExporter exporter) {
        //This is jank as hell but it should work in theory
        this.getGauge().clear();

        Object2IntOpenHashMap<Holder<Item>> itemCounter = new Object2IntOpenHashMap<>();
        for (ServerLevel world : exporter.getServer().getAllLevels()) {
            itemCounter.clear();
            var worldName = this.identifierFormatter.getWorldName(world);
            world.getAllEntities().forEach(entity->{
                if (entity instanceof ItemEntity item) {
                    var holder = item.getItem().typeHolder();
                    if (holder.unwrapKey().isPresent()) {
                        itemCounter.addTo(holder, 1);
                    }
                }
            });


            for (var entry : itemCounter.object2IntEntrySet()) {
                this.getGauge().labels(worldName, this.identifierFormatter.format(entry.getKey().unwrapKey().get().identifier())).set(entry.getIntValue());
            }
        }
    }
}
