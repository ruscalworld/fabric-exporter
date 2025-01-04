package ru.ruscalworld.fabricexporter;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.SimpleCollector;
import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.fabricexporter.config.MainConfig;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.metrics.spark.MillisPerTick;
import ru.ruscalworld.fabricexporter.metrics.spark.SparkMetric;
import ru.ruscalworld.fabricexporter.metrics.spark.TicksPerSecond;
import ru.ruscalworld.fabricexporter.metrics.world.Entities;
import ru.ruscalworld.fabricexporter.metrics.world.LoadedChunks;
import ru.ruscalworld.fabricexporter.metrics.world.OnlinePlayers;
import ru.ruscalworld.fabricexporter.metrics.world.TotalLoadedChunks;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class MetricRegistry {
    private final FabricExporter exporter;
    private final List<Metric> metrics = new ArrayList<>();
    private final HashMap<String, Collector> customMetrics = new HashMap<>();
    private final Timer metricUpdaterTimer;
    private final IdentifierFormatter identifierFormatter;

    public MetricRegistry(@NotNull FabricExporter exporter) {
        this.exporter = exporter;
        metricUpdaterTimer = new Timer("Metric Updater Timer");
        identifierFormatter = exporter.getIdentifierFormatter();
    }

    public void runUpdater() {
        MainConfig config = this.getExporter().getConfig();
        MetricUpdater metricUpdater = new MetricUpdater(this.getExporter());
        this.getMetrics().forEach(metricUpdater::registerMetric);
        this.getMetricUpdaterTimer().schedule(metricUpdater, 1000, config.getUpdateInterval());
    }

    public void registerDefault() {
        this.registerMetric(new OnlinePlayers(identifierFormatter));
        this.registerMetric(new Entities(identifierFormatter));
        this.registerMetric(new LoadedChunks(identifierFormatter));
        this.registerMetric(new TotalLoadedChunks(identifierFormatter));

        if (this.getExporter().getConfig().shouldUseSpark()) {
            this.registerMetric(new TicksPerSecond());
            this.registerMetric(new MillisPerTick());
        }

        this.registerCustomMetric("handshakes", new Counter.Builder()
                .name(getMetricName("handshakes_total"))
                .help("Amount of handshake requests")
                .labelNames("type")
        );
    }

    public void registerMetric(Metric metric) {
        MainConfig config = this.getExporter().getConfig();
        if (metric instanceof SparkMetric && !config.shouldUseSpark()) return;
        if (this.isDisabled(metric.getName())) return;

        metric.getGauge().register();
        this.metrics.add(metric);
    }

    public void registerCustomMetric(String name, SimpleCollector.Builder<?, ?> collector) {
        if (this.isDisabled(name)) return;
        this.getCustomMetrics().put(name, collector.register());
    }

    public boolean isDisabled(String name) {
        MainConfig config = this.getExporter().getConfig();
        String property = "enable-" + name.replace("_", "-");
        return !config.getProperties().getProperty(property, "true").equals("true");
    }

    public static String getMetricName(String name) {
        return "minecraft_" + name;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public FabricExporter getExporter() {
        return exporter;
    }

    public Timer getMetricUpdaterTimer() {
        return metricUpdaterTimer;
    }

    public HashMap<String, Collector> getCustomMetrics() {
        return customMetrics;
    }
}
