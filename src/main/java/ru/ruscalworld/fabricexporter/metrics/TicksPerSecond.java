package ru.ruscalworld.fabricexporter.metrics;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import ru.ruscalworld.fabricexporter.FabricExporter;

public class TicksPerSecond extends SparkMetric {
    public TicksPerSecond() {
        super("tps", "Current TPS on server");
    }

    @Override
    public void update(FabricExporter exporter) {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = this.getSpark().tps();
        if (tps == null) this.getGauge().set(20);
        else this.getGauge().set(tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1));
    }
}
