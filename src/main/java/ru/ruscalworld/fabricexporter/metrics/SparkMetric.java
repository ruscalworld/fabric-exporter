package ru.ruscalworld.fabricexporter.metrics;

import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;

public abstract class SparkMetric extends Metric {
    public SparkMetric(String name, String help, String... labels) {
        super(name, help, labels);
    }

    public Spark getSpark() {
        return SparkProvider.get();
    }
}
