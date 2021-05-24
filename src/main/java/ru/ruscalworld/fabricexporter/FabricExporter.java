package ru.ruscalworld.fabricexporter;

import io.prometheus.client.exporter.HTTPServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ruscalworld.fabricexporter.config.MainConfig;
import ru.ruscalworld.fabricexporter.metrics.OnlinePlayers;
import ru.ruscalworld.fabricexporter.metrics.TicksPerSecond;
import ru.ruscalworld.fabricexporter.metrics.MillisPerTick;

import java.io.IOException;
import java.util.Timer;

public class FabricExporter implements ModInitializer {
    private MinecraftServer server;
    private MainConfig config;

    @Override
    public void onInitialize() {
        try {
            MainConfig config = new MainConfig("exporter.properties");
            config.load();
            this.setConfig(config);
        } catch (IOException e) {
            FabricExporter.getLogger().fatal("Unable to load config");
            e.printStackTrace();
        }

        MetricUpdater metricUpdater = new MetricUpdater(this);
        metricUpdater.registerMetric(new OnlinePlayers());
        metricUpdater.registerMetric(new TicksPerSecond());
        metricUpdater.registerMetric(new MillisPerTick());

        ServerLifecycleEvents.SERVER_STARTING.register(this::setServer);
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                int port = this.getConfig().getPort();
                new HTTPServer(port);
                FabricExporter.getLogger().info("Prometheus exporter server is now listening on port " + port);

                Timer timer = new Timer();
                timer.schedule(metricUpdater, 1000, this.getConfig().getUpdateInterval());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Logger getLogger() {
        return LogManager.getLogger();
    }

    public MinecraftServer getServer() {
        return server;
    }

    private void setServer(MinecraftServer server) {
        this.server = server;
    }

    public MainConfig getConfig() {
        return config;
    }

    public void setConfig(MainConfig config) {
        this.config = config;
    }
}
