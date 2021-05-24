package ru.ruscalworld.fabricexporter.metrics;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import ru.ruscalworld.fabricexporter.FabricExporter;

public class TicksPerSecond extends SparkMetric {
    public TicksPerSecond() {
        super("tps", "Current TPS on server");
    }

    @Override
    public double getCurrentValue(FabricExporter exporter) {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = this.getSpark().tps();
        if (tps == null) return 20;
        return tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1);
    }
}
