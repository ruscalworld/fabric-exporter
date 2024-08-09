package ru.ruscalworld.fabricexporter.metrics.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import org.jetbrains.annotations.NotNull;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.metrics.Metric;
import ru.ruscalworld.fabricexporter.util.TextUtil;

public class OnlinePlayers extends Metric {
    public OnlinePlayers() {
        super(
                "online_players", "Extended info about all the online players",
                "uuid", "name", "world", "game_mode"
        );
    }

    @Override
    public void update(@NotNull FabricExporter exporter) {
        PlayerManager playerManager = exporter.getServer().getPlayerManager();
        this.getGauge().clear();

        playerManager.getPlayerList().forEach(player -> {
            GameProfile profile = player.getGameProfile();

            this.getGauge().labels(
                    profile.getId().toString(),
                    profile.getName(),
                    TextUtil.getWorldName(player.getServerWorld()),
                    player.interactionManager.getGameMode().getName()
            ).set(1);
        });
    }
}
