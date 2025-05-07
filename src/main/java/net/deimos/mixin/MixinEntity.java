package net.deimos.mixin;

import net.deimos.api.event.EventManager;
import net.deimos.api.event.impl.StrafeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(at = @At("HEAD"), method="updateVelocity", cancellable = true)
    public void grimStrafe(float speed, Vec3d movementInput, CallbackInfo ci) {
        EventManager.post(new StrafeEvent(speed, movementInput, ci));
    }

}
