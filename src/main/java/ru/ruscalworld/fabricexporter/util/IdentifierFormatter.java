package ru.ruscalworld.fabricexporter.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

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

    public String getPlayerIP(ServerPlayer player, boolean shouldAnonymize) {
        if (!(player.connection.getRemoteAddress() instanceof InetSocketAddress socketAddress)) return "";
        InetAddress address = socketAddress.getAddress();
        if (address == null) return "";

        if (shouldAnonymize) address = this.anonymizeIP(address);
        return address.getHostAddress();
    }

    private InetAddress anonymizeIP(InetAddress address) {
        // Keep only the network prefix: /24 for IPv4 and /48 for IPv6
        byte[] bytes = address.getAddress();
        int prefixLength = address instanceof Inet4Address ? 3 : 6;
        Arrays.fill(bytes, prefixLength, bytes.length, (byte) 0);

        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException exception) {
            // Can't happen as the length of the array is taken from a valid address
            throw new IllegalStateException(exception);
        }
    }

    public String getWorldName(@NotNull ServerLevel world) {
        return this.format(world.dimension().identifier());
    }
}
