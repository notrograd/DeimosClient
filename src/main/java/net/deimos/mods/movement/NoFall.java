package net.deimos.mods.movement;

import net.deimos.api.event.impl.PacketEvent;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.i.EventHandler;
import net.deimos.api.i.Module;
import net.deimos.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.deimos.api.mods.Category;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@Module(name="NoFall",description="Cancels all fall damage.",category= Category.Movement)
public class NoFall extends ModuleBuilder {
    @EventHandler
    public void run(PacketEvent.Receive inbound){
        if (inbound.getPacket() instanceof PlayerMoveC2SPacket packet) {

            if (isFalling())
                ((AccessorPlayerMoveC2SPacket) packet).setOnGround(true);
        }
    }
    private boolean isFalling() {
        if (!client.player.isOnGround() && client.player.getVelocity().y > 0) {
            return true;
        }
        return false;
    }
}
