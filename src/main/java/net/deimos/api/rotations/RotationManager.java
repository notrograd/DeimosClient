package net.deimos.api.rotations;

import net.deimos.api.event.impl.MovementEvent;
import net.deimos.api.event.impl.StrafeEvent;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.IClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * --CLASS PORTED FROM OMNIHACK/OMNI CLIENT--
 * Credit to github.com/misten1
 * ------------------------------------------
 */
// Simple rotation manager
public class RotationManager implements IClient {

    // null = vanilla rotation takes over
    Rotation target_rotation = null;
    float prevPitch, prevYaw;

    // todo can be improved™
    public void rotate(Vec3d point) {
        target_rotation = new Rotation(point);
    }

    // Set rotations
    @EventHandler
    public void preSync(MovementEvent.Pre event) {
        if (mc.player == null) return;

        prevPitch = mc.player.getPitch();
        prevYaw = mc.player.getYaw();

        if (target_rotation == null) return;
        mc.player.setYaw(target_rotation.yaw);
        mc.player.setPitch(target_rotation.pitch);

        // Rotated
        target_rotation = null;
    }

    // Set variables back
    @EventHandler
    public void postSync(MovementEvent.Post event) {
        if (mc.player == null) return;
        mc.player.setYaw(prevYaw);
        mc.player.setPitch(prevPitch);
    }


    // 1njects code
    // might become a module
//    @EventHandler
//    public void Bussin(MovementEvent.Post event) {
//        if (mc.player == null) return;
//        if (mc.player.isSprinting()) {
//            mc.player.setYaw(-90);
//            mc.player.setPitch(90);
//        }
//        if (mc.player.isHoldingOntoLadder()) {
//            mc.player.setYaw(-50);
//            mc.player.setPitch(50);
//        }
//    }

    // The strafe fix
    // Grim has many other strafe things but this is the most important one
    @EventHandler
    public void onStrafe(StrafeEvent event) {
        if (target_rotation == null || mc.player.isRiding())
            return;

        event.getCallback().cancel();
        mc.player.setVelocity(mc.player.getVelocity().add(
                fix(target_rotation.yaw, event.getMovementInput(), event.getSpeed())
        ));
    }

    // the magic function
    // todo keyboard helpers to make it seem less invasive and more easy to use
    public static Vec3d fix(float yaw, Vec3d movementInput, float speed) {
        double d = movementInput.lengthSquared();
        if (d < 1.0E-7)
            return Vec3d.ZERO;

        Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply(speed);
        float f = MathHelper.sin(yaw * MathHelper.RADIANS_PER_DEGREE);
        float g = MathHelper.cos(yaw * MathHelper.RADIANS_PER_DEGREE);
        return new Vec3d(vec3d.x * (double) g - vec3d.z * (double) f, vec3d.y, vec3d.z * (double) g + vec3d.x * (double) f);
    }

    public static class Rotation {
        public Vec3d point;
        public float yaw;
        public float pitch;

        public Rotation(Vec3d point) {
            this.point = point;
            float[] rots = RotationUtil.calculateRotations(mc.player.getEyePos(), point);
            this.yaw = rots[0];
            this.pitch = rots[1];
        }
    }
}