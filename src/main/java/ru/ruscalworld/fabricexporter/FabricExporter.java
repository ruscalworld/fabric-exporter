package ru.ruscalworld.fabricexporter;

import io.prometheus.client.exporter.HTTPServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ruscalworld.fabricexporter.config.MainConfig;
import ru.ruscalworld.fabricexporter.metrics.*;

import java.io.IOException;
import java.util.Timer;

public class FabricExporter implements ModInitializer {
    private MinecraftServer server;
    private MainConfig config;
    private HTTPServer httpServer;
    private MetricRegistry metricRegistry;

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

        MetricRegistry metricRegistry = new MetricRegistry(this);
        metricRegistry.registerDefault();
        this.setMetricRegistry(metricRegistry);

        ServerLifecycleEvents.SERVER_STARTING.register(this::setServer);
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                int port = this.getConfig().getPort();
                this.setHttpServer(new HTTPServer(port));
                FabricExporter.getLogger().info("Prometheus exporter server is now listening on port " + port);

                this.getMetricRegistry().runUpdater();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            this.getHttpServer().stop();
            this.getMetricRegistry().getTimer().cancel();
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

    public HTTPServer getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(HTTPServer httpServer) {
        this.httpServer = httpServer;
    }

    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    private void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }
}
