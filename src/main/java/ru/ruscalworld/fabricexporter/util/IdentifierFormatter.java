package ru.ruscalworld.fabricexporter.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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

    public String getWorldName(@NotNull ServerWorld world) {
        return this.format(world.getRegistryKey().getValue());
    }
}
