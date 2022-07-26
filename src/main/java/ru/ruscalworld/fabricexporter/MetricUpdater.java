package ru.ruscalworld.fabricexporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ruscalworld.fabricexporter.metrics.Metric;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MetricUpdater extends TimerTask {
    private final static Logger logger = LoggerFactory.getLogger("MetricUpdater");
    private final List<Metric> metrics = new ArrayList<>();
    private final FabricExporter exporter;

    public MetricUpdater(FabricExporter exporter) {
        this.exporter = exporter;
    }

    @Override
    public void run() {
        for (Metric metric : this.getMetrics()) {
            try {
                metric.update(this.getExporter());
            } catch (Exception exception) {
                logger.error("Error updating metric " + metric.getName(), exception);
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
