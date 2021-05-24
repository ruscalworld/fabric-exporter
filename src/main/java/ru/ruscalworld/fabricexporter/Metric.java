package ru.ruscalworld.fabricexporter;

import io.prometheus.client.Gauge;

public abstract class Metric {
    private final Gauge gauge;

    public Metric(String name, String help) {
        this.gauge = new Gauge.Builder().name("minecraft_" + name).help(help).create().register();
    }

    public abstract double getCurrentValue(FabricExporter exporter);

    public Gauge getGauge() {
        return gauge;
    }
}
