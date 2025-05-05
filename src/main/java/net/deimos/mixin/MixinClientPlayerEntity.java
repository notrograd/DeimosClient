package net.deimos.mixin;

import net.deimos.Deimos;
import net.deimos.api.EventManager;
import net.deimos.api.event.impl.MovementEvent;
import net.deimos.api.mods.ModuleManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(at = @At("HEAD"), method = "sendMovementPackets")
    private void update(CallbackInfo ci) {
        EventManager.post(new MovementEvent.Pre());
    }

    @Inject(at = @At("TAIL"), method = "sendMovementPackets")
    private void reset(CallbackInfo ci) {
        EventManager.post(new MovementEvent.Post());
    }

    @Redirect(method = "tickMovement",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"),
            require = 0)
    private boolean tickMovement(ClientPlayerEntity player) {
        if (Deimos.MOD_MANAGER.enabled("NoSlow")) {
            return false;
        }
        return player.isUsingItem();
    }
}
