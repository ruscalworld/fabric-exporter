package ru.ruscalworld.fabricexporter.metrics;

import io.prometheus.client.Gauge;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.MetricRegistry;

public abstract class Metric {
    private final Gauge gauge;
    private final String name;

    public Metric(String name, String help, String... labels) {
        this.name = name;
        this.gauge = new Gauge.Builder()
                .name(MetricRegistry.getMetricName(name))
                .help(help)
                .labelNames(labels)
                .create();
    }

    public abstract void update(FabricExporter exporter);

    public Gauge getGauge() {
        return gauge;
    }

    public String getName() {
        return name;
    }
}
