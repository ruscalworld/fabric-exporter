package ru.ruscalworld.fabricexporter.metrics;

import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.Metric;

public class OnlinePlayers extends Metric {
    public OnlinePlayers() {
        super("players_online", "Amount of currently online players on the server");
    }

    @Override
    public double getCurrentValue(FabricExporter exporter) {
        return exporter.getServer().getCurrentPlayerCount();
    }
}
