package ru.ruscalworld.fabricexporter.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

        // IPv6 addresses returned by InetSocketAddress are enclosed in brackets
        // when a port is present, for example: [2001:db8::1]:25565.
        if (ip.startsWith("[")) {
            int closingBracketIndex = ip.indexOf(']');
            if (closingBracketIndex != -1) {
                return ip.substring(1, closingBracketIndex);
            }
        }

        // An unbracketed address with one colon is an IPv4 address with a port.
        // Multiple colons indicate an IPv6 address, whose colons are part of the
        // address and must be preserved.
        if (ip.indexOf(':') != -1 && ip.indexOf(':') == ip.lastIndexOf(':')) {
            ip = ip.substring(0, ip.indexOf(':'));
        }
        return ip;
    }

    public String getPlayerAnonymizedIP(ServerPlayer player) {
        String ip = this.cleanupIP(player.connection.getRemoteAddress().toString());
        int lastDotIndex = ip.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return ip.substring(0, lastDotIndex) + ".0";
        }

        if (ip.indexOf(':') != -1) {
            return this.anonymizeIPv6(ip);
        }

        return ip;
    }

    private String anonymizeIPv6(String ip) {
        try {
            byte[] address = InetAddress.getByName(ip).getAddress();
            if (address.length != 16) {
                return ip;
            }

            // Keep the network prefix and clear the interface identifier (/64).
            for (int i = 8; i < address.length; i++) {
                address[i] = 0;
            }
            return InetAddress.getByAddress(address).getHostAddress();
        } catch (UnknownHostException exception) {
            return ip;
        }
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
