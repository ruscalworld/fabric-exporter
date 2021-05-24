package ru.ruscalworld.fabricexporter.metrics;

import io.prometheus.client.Gauge;
import ru.ruscalworld.fabricexporter.FabricExporter;

public abstract class Metric {
    private final Gauge gauge;

    public Metric(String name, String help, String... labels) {
        this.gauge = new Gauge.Builder()
                .name("minecraft_" + name)
                .help(help)
                .labelNames(labels)
                .create().register();
    }

    public abstract void update(FabricExporter exporter);

    public Gauge getGauge() {
        return gauge;
    }
}
