package ru.ruscalworld.fabricexporter.metrics;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import ru.ruscalworld.fabricexporter.FabricExporter;

public class TicksPerSecond extends SparkMetric {
    public TicksPerSecond() {
        super("tps", "Current TPS on server");
    }

    @Override
    public void onShouldUpdate(FabricExporter exporter) {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = this.getSpark().tps();
        if (tps == null) this.update().set(20);
        else this.update().set(tps.poll(StatisticWindow.TicksPerSecond.MINUTES_1));
    }
}
