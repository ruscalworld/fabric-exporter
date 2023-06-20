package ru.ruscalworld.fabricexporter.metrics.spark;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import ru.ruscalworld.fabricexporter.FabricExporter;

public class MillisPerTick extends SparkMetric {
    public MillisPerTick() {
        super("mspt", "Milliseconds per tick (MSPT)", "type");
    }

    @Override
    public void update(FabricExporter exporter) {
        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = this.getSpark().mspt();
        if (mspt == null) this.setValue(0, 0, 0);
        else this.setValue(
                mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1).min(),
                mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1).mean(),
                mspt.poll(StatisticWindow.MillisPerTick.MINUTES_1).max()
        );
    }

    private void setValue(double min, double mean, double max) {
        this.getGauge().labels("min").set(min);
        this.getGauge().labels("mean").set(mean);
        this.getGauge().labels("max").set(max);
    }
}
