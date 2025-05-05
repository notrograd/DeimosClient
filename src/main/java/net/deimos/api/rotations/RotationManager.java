package net.deimos.api.rotations;

import net.deimos.api.EventManager;
import net.deimos.api.event.impl.MovementEvent;
import net.deimos.api.event.impl.StrafeEvent;
import net.deimos.api.util.NoCheatPlus;
import net.deimos.api.i.EventHandler;
import net.deimos.api.i.IClient;
import net.deimos.mixin.accessor.AccessorPlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * CREDITS: github.com/misten1
 */
public class RotationManager implements IClient {
    Rotation target_rotation = null;
    float prevYaw,prevPitch;
    boolean shouldRotate = true;

    public void ncp(Vec3d point)
    {
        RotationUtil.rotateInstant(point);
    }

    // make the rotation work
    @EventHandler
    public void ncpstrict(NoCheatPlus.StrictRotation event){

        // not exactly sure this works
        if (event.getPacket() instanceof PlayerMoveC2SPacket.LookAndOnGround p_)
        {
            ((AccessorPlayerMoveC2SPacket) p_).setYaw(event.yaw);
            ((AccessorPlayerMoveC2SPacket) p_).setPitch(event.pitch);
        }
    }

    public void grim(Vec3d point) {
        if (this.target_rotation == null)
            this.target_rotation = new Rotation(point);
    }
    @EventHandler
    public void preSync(MovementEvent.Pre event) {
        if (client.player == null) return;

        prevPitch = client.player.getPitch();
        prevYaw = client.player.getYaw();

        if (target_rotation == null) return;
        client.player.setYaw(target_rotation.yaw);
        client.player.setPitch(target_rotation.pitch);

        // has rotated
        target_rotation = null;
    }

    @EventHandler
    public void postSync(MovementEvent.Post event) {
        if (client.player == null) return;
        client.player.setYaw(prevYaw);
        client.player.setPitch(prevPitch);
    }

    public void reset()
    {
        this.target_rotation = null;
    }

    @EventHandler
    public void onStrafe(StrafeEvent event) {
        if (target_rotation == null || client.player.isRiding())
            return;

        event.getCallback().cancel();
        client.player.setVelocity(client.player.getVelocity().add(
                fix(target_rotation.yaw, event.getMovementInput(), event.getSpeed())
        ));
    }

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
            assert client.player != null;
            float[] rots = RotationUtil.calculateRotations(client.player.getEyePos(), point);
            this.yaw = rots[0];
            this.pitch = rots[1];
        }
    }

    /*--*/
    {
        EventManager.register(this);
    }
}
