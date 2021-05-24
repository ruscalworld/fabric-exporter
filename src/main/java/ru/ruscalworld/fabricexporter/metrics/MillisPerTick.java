package ru.ruscalworld.fabricexporter.metrics;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import ru.ruscalworld.fabricexporter.FabricExporter;

public class MillisPerTick extends SparkMetric {
    public MillisPerTick() {
        super("mspt", "Milliseconds per tick (MSPT)");
    }

    @Override
    public double getCurrentValue(FabricExporter exporter) {
        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = this.getSpark().mspt();
        if (mspt == null) return 0;
        return mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1).mean();
    }
}
