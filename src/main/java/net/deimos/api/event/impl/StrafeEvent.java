package net.deimos.api.event.impl;

import net.deimos.api.event.Event;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StrafeEvent extends Event {

    float speed;
    Vec3d movementInput;
    CallbackInfo ci;

    public StrafeEvent(float speed, Vec3d movementInput, CallbackInfo ci) {
        this.speed = speed;
        this.movementInput = movementInput;
        this.ci = ci;
    }

    public CallbackInfo getCallback() {
        return ci;
    }

    public float getSpeed() {
        return speed;
    }

    public Vec3d getMovementInput() {
        return movementInput;
    }
}
