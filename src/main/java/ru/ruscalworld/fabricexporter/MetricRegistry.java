package ru.ruscalworld.fabricexporter;

import io.prometheus.client.Collector;
import ru.ruscalworld.fabricexporter.config.MainConfig;
import ru.ruscalworld.fabricexporter.metrics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MetricRegistry {
    private final FabricExporter exporter;
    private final List<Metric> metrics = new ArrayList<>();
    private final Timer timer;

    public MetricRegistry(FabricExporter exporter) {
        this.exporter = exporter;
        timer = new Timer();
    }

    public void runUpdater() {
        MainConfig config = this.getExporter().getConfig();
        MetricUpdater metricUpdater = new MetricUpdater(this.getExporter());
        this.getMetrics().forEach(metricUpdater::registerMetric);
        this.getTimer().schedule(metricUpdater, 1000, config.getUpdateInterval());
    }

    public void registerDefault() {
        this.registerMetric(new OnlinePlayers());
        this.registerMetric(new Entities());
        this.registerMetric(new LoadedChunks());
        this.registerMetric(new TotalLoadedChunks());
        this.registerMetric(new TicksPerSecond());
        this.registerMetric(new MillisPerTick());
    }

    public void registerMetric(Metric metric) {
        MainConfig config = this.getExporter().getConfig();
        if (metric instanceof SparkMetric && !config.shouldUseSpark()) return;
        if (!this.isEnabled(metric.getName())) return;

        metric.getGauge().register();
        this.metrics.add(metric);
    }

    public void registerCustomMetric(String name, Collector collector) {
        if (this.isEnabled(name)) collector.register();
    }

    public boolean isEnabled(String name) {
        MainConfig config = this.getExporter().getConfig();
        String property = "enable-" + name.replace("_", "-");
        return config.getProperties().getProperty(property, "true").equals("true");
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public FabricExporter getExporter() {
        return exporter;
    }

    public Timer getTimer() {
        return timer;
    }
}
