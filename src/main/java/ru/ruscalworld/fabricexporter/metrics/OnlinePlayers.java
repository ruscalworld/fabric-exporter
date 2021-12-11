package ru.ruscalworld.fabricexporter.metrics;

import net.minecraft.server.world.ServerWorld;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.util.TextUtil;

public class OnlinePlayers extends Metric {
    public OnlinePlayers() {
        super("players_online", "Amount of currently online players on the server", "world");
    }

    @Override
    public void onShouldUpdate(FabricExporter exporter) {
        for (ServerWorld world : exporter.getServer().getWorlds()) {
            this.update(TextUtil.getWorldName(world)).set(world.getPlayers().size());
        }
    }
}
