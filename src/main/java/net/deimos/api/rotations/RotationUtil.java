package net.deimos.api.rotations;

import net.deimos.api.i.IClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements IClient {
    public static float[] calculateRotations(Vec3d from, Vec3d to) {
        double deltaX = to.x - from.x;
        double deltaY = to.y - from.y;
        double deltaZ = to.z - from.z;

        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0);
        float pitch = (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(deltaY, horizontalDistance)));

        return new float[]{yaw, pitch};
    }

    public static float getYaw(Vec3d vec) {
        return (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
    }

    public static float getPitch(Vec3d vec) {
        double horizontalDistance = Math.sqrt(vec.x * vec.x + vec.z * vec.z);
        return (float) MathHelper.wrapDegrees(-Math.toDegrees(Math.atan2(vec.y, horizontalDistance)));
    }

    /**
     * ncp rotation bypass #A,
     * do not use for AutoCrystal or anything that requires an excessive amount of rotations
     *
     */
    public static void rotateInstant(Vec3d point) {
        assert client.player != null;
        float[] rots = RotationUtil.calculateRotations(client.player.getEyePos(), point);
        client.player.networkHandler.sendPacket(
                new PlayerMoveC2SPacket.Full(
                        client.player.getX(),
                        client.player.getY(),
                        client.player.getZ(),
                        rots[0],
                        rots[1],
                        client.player.isOnGround()
                )
        );
    }
}