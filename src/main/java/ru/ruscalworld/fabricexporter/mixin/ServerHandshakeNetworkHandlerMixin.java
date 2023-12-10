package ru.ruscalworld.fabricexporter.mixin;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.MetricRegistry;

@Mixin(ServerHandshakeNetworkHandler.class)
public class ServerHandshakeNetworkHandlerMixin {
    @Inject(method = "onHandshake", at = @At("HEAD"))
    public void onHandshake(HandshakeC2SPacket packet, CallbackInfo info) {
        MetricRegistry metricRegistry = FabricExporter.getInstance().getMetricRegistry();
        Collector collector = metricRegistry.getCustomMetrics().get("handshakes");
        if (!(collector instanceof Counter counter)) return;
        counter.labels(packet.intendedState().name().toLowerCase()).inc();
    }
}
