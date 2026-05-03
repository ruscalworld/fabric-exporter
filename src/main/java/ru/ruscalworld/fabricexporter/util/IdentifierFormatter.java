package ru.ruscalworld.fabricexporter.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class IdentifierFormatter {
    private final boolean stripNamespaces;

    public IdentifierFormatter(boolean stripNamespaces) {
        this.stripNamespaces = stripNamespaces;
    }

    public String format(Identifier identifier) {
        if (this.stripNamespaces) return identifier.getPath();
        return identifier.toString();
    }

    public String getPlayerName(ServerPlayer player) {
        return player.getName().getString();
    }

    private String cleanupIP(String ip) {
        // Remove leading slash if present
        if (ip.startsWith("/")) {
            ip = ip.substring(1);
        }
        // Remove port if present
        int colonIndex = ip.indexOf(':');
        if (colonIndex != -1) {
            ip = ip.substring(0, colonIndex);
        }
        return ip;
    }

    public String getPlayerAnonymizedIP(ServerPlayer player) {
        String ip = this.cleanupIP(player.connection.getRemoteAddress().toString());
        int lastDotIndex = ip.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return ip.substring(0, lastDotIndex) + ".0";
        }
        return ip;
    }

    public String getPlayerIP(ServerPlayer player, boolean shouldAnonymize) {
        if (shouldAnonymize) {
            return this.getPlayerAnonymizedIP(player);
        }
        return this.cleanupIP(player.connection.getRemoteAddress().toString());
    }

    public String getWorldName(@NotNull ServerLevel world) {
        return this.format(world.dimension().identifier());
    }
}
