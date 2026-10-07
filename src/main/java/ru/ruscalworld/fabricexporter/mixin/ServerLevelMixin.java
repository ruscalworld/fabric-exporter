package ru.ruscalworld.fabricexporter.mixin;

import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ru.ruscalworld.fabricexporter.ducks.IChunkStatusByCountArrayGetter;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements IChunkStatusByCountArrayGetter {
    @Unique
    private final int[] fabricexporter$loadedChunkCountByStatus = new int[FullChunkStatus.values().length];

    @Override
    public int[] fabricexporter$getChunkStatusCounter() {
        return this.fabricexporter$loadedChunkCountByStatus;
    }
}
