package net.deimos.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.deimos.api.EventManager;
import net.deimos.api.event.impl.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {
    @Shadow
    private Channel channel;
    @Shadow @Final
    private NetworkSide side;
    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext chc, Packet<?> packet, CallbackInfo ci) {
        if (this.channel.isOpen() && packet != null) {
            try {
                PacketEvent.Receive event = new PacketEvent.Receive(packet);
                EventManager.post(event);

                if (event.isCancelled) ci.cancel();
            } catch (Exception ignored) {
            }
        }
    }
}
