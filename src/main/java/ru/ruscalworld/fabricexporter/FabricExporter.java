package ru.ruscalworld.fabricexporter;

import io.prometheus.client.exporter.HTTPServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import ru.ruscalworld.fabricexporter.metrics.OnlinePlayers;

import java.io.IOException;
import java.util.Timer;

public class FabricExporter implements ModInitializer {
    private MinecraftServer server;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(this::setServer);

        try {
            HTTPServer httpServer = new HTTPServer(1337);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MetricUpdater metricUpdater = new MetricUpdater(this);
        metricUpdater.registerMetric(new OnlinePlayers());

        Timer timer = new Timer();
        timer.schedule(metricUpdater, 1000, 1000);
    }

    public static String getMetricName(String name) {
        return "mc_" + name;
    }

    public MinecraftServer getServer() {
        return server;
    }

    private void setServer(MinecraftServer server) {
        this.server = server;
    }
}
