package ru.ruscalworld.fabricexporter.metrics.spark;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import ru.ruscalworld.fabricexporter.metrics.Metric;

public abstract class SparkMetric extends Metric {
    public SparkMetric(String name, String help, String... labels) {
        super(name, help, labels);
    }

    public Spark getSpark() {
        return SparkProvider.get();
    }
}
