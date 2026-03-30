package ru.ruscalworld.fabricexporter.mixin;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.server.network.ServerHandshakePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.MetricRegistry;

@Mixin(ServerHandshakePacketListenerImpl.class)
public class ServerHandshakePacketListenerImplMixin {
    @Inject(method = "handleIntention", at = @At("HEAD"))
    public void onHandshake(ClientIntentionPacket packet, CallbackInfo info) {
        MetricRegistry metricRegistry = FabricExporter.getInstance().getMetricRegistry();
        Collector collector = metricRegistry.getCustomMetrics().get("handshakes");
        if (!(collector instanceof Counter counter)) return;
        counter.labels(packet.intention().name().toLowerCase()).inc();
    }
}
