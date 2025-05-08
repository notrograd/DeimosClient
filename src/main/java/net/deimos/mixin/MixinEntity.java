package net.deimos.mixin;

import net.deimos.api.event.EventManager;
import net.deimos.api.event.impl.StrafeEvent;
import net.deimos.mods.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    // grimstrafe; RotationManager.java
    @Inject(at = @At("HEAD"), method="updateVelocity", cancellable = true)
    public void updateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
        EventManager.post(new StrafeEvent(speed, movementInput, ci));
    }

    // Step.java
    @Inject(at = @At("HEAD"),method="getStepHeight",cancellable = true)
    public void getStepHeight(CallbackInfoReturnable<Float> cir){
        if (ModuleManager.STEP.getEnabled())
        {
            cir.setReturnValue((float) ModuleManager.STEP.height.getValue());
        }
    }
}
