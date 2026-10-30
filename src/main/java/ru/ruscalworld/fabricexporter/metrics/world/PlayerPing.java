package ru.ruscalworld.fabricexporter.metrics.world;

import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.IdentifierFormatter;

public class PlayerPing extends Metric {
    private final IdentifierFormatter identifierFormatter;
    private final boolean shouldCollectIP;
    private final boolean shouldAnonymizeIP;

    public PlayerPing(IdentifierFormatter identifierFormatter, boolean shouldCollectIP, boolean shouldAnonymizeIP) {
        super("player_ping", "Current ping of online players on your server", "name", "world", "ip");
        this.identifierFormatter = identifierFormatter;
        this.shouldCollectIP = shouldCollectIP;
        this.shouldAnonymizeIP = shouldAnonymizeIP;
    }

    @Override
    public void update(FabricExporter exporter) {
        this.getGauge().clear();
        exporter.getServer().getAllLevels().forEach(world -> world.players().forEach(player -> {
            this.getGauge().labels(
                    identifierFormatter.getPlayerName(player),
                    identifierFormatter.getWorldName(world),
                    shouldCollectIP ? identifierFormatter.getPlayerIP(player, shouldAnonymizeIP) : ""
                    ).set(player.connection.latency());
        }));
    }
}
