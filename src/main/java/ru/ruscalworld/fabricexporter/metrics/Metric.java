package ru.ruscalworld.fabricexporter.metrics;

import io.prometheus.client.Gauge;
import org.apache.commons.lang3.ArrayUtils;
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
                .labelNames(ArrayUtils.addAll(labels, MetricRegistry.getGlobalLabelNames()))
                .create();
    }

    public abstract void onShouldUpdate(FabricExporter exporter);

    public Gauge getGauge() {
        return gauge;
    }

    public String getName() {
        return name;
    }

    public Gauge.Child update(String... labelValues) {
        MetricRegistry metricRegistry = FabricExporter.getInstance().getMetricRegistry();
        return this.getGauge().labels(ArrayUtils.addAll(labelValues, metricRegistry.getGlobalLabelValues()));
    }
}
