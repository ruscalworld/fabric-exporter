package ru.ruscalworld.fabricexporter;

import io.prometheus.client.exporter.HTTPServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ruscalworld.fabricexporter.config.MainConfig;
import ru.ruscalworld.fabricexporter.metrics.OnlinePlayers;

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

        ServerLifecycleEvents.SERVER_STARTED.register(this::setServer);

        try {
            new HTTPServer(this.getConfig().getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MetricUpdater metricUpdater = new MetricUpdater(this);
        metricUpdater.registerMetric(new OnlinePlayers());

        Timer timer = new Timer();
        timer.schedule(metricUpdater, 1000, this.getConfig().getUpdateInterval());
    }

    public static Logger getLogger() {
        return LogManager.getLogger();
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

    public MainConfig getConfig() {
        return config;
    }

    public void setConfig(MainConfig config) {
        this.config = config;
    }
}
