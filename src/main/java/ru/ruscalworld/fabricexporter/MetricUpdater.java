package ru.ruscalworld.fabricexporter;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MetricUpdater extends TimerTask {
    private final List<Metric> metrics = new ArrayList<>();
    private final FabricExporter exporter;

    public MetricUpdater(FabricExporter exporter) {
        this.exporter = exporter;
    }

    @Override
    public void run() {
        for (Metric metric : this.getMetrics()) {
            try {
                metric.getGauge().set(metric.getCurrentValue(this.getExporter()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerMetric(Metric metric) {
        this.metrics.add(metric);
    }

    public FabricExporter getExporter() {
        return exporter;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }
}
