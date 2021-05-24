package ru.ruscalworld.fabricexporter.util;

import net.minecraft.server.world.ServerWorld;

public class TextUtil {
    public static String getWorldName(ServerWorld world) {
        return world.getRegistryKey().getValue().getPath();
    }
}
