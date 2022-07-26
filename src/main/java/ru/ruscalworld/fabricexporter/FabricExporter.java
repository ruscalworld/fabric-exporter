package ru.ruscalworld.fabricexporter;

import io.prometheus.client.exporter.HTTPServer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ruscalworld.fabricexporter.config.MainConfig;

import java.io.IOException;
import java.util.Optional;

public class FabricExporter implements ModInitializer {
    private static final Logger logger = LogManager.getLogger();
    private static FabricExporter instance;

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

        Optional<ModContainer> spark = FabricLoader.getInstance().getModContainer("spark");
        if (spark.isEmpty() && config.shouldUseSpark()) {
            config.setShouldUseSpark(false);
            logger.warn("Spark mod is not installed, but \"use-spark\" property is enabled! TPS and MSPT metrics will be disabled.");
            logger.warn("To fix this, you should either set \"use-spark\" in exporter.properties to false or install Spark mod (https://spark.lucko.me).");
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
            this.getHttpServer().close();
            this.getMetricRegistry().getMetricUpdaterTimer().cancel();
        });

        instance = this;
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

    public static FabricExporter getInstance() {
        return instance;
    }
}
