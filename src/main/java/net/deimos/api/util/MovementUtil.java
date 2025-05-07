package net.deimos.api.util;

import net.deimos.api.interfaces.IClient;
import net.minecraft.util.math.Vec3d;

public class MovementUtil implements IClient {

    public static void moveForward(float speed) {
        if (client.player == null) return;
        Vec3d dir = getForwardVector(speed);
        client.player.setVelocity(dir.x, client.player.getVelocity().y, dir.z);
        client.player.velocityModified = true;
    }

    public static void moveBackward(float speed) {
        if (client.player == null) return;
        Vec3d dir = getForwardVector(-speed);
        client.player.setVelocity(dir.x, client.player.getVelocity().y, dir.z);
        client.player.velocityModified = true;
    }

    public static void strafeLeft(float speed) {
        if (client.player == null) return;
        Vec3d dir = getStrafeVector(speed);
        client.player.setVelocity(dir.x, client.player.getVelocity().y, dir.z);
        client.player.velocityModified = true;
    }

    public static void strafeRight(float speed) {
        if (client.player == null) return;
        Vec3d dir = getStrafeVector(-speed);
        client.player.setVelocity(dir.x, client.player.getVelocity().y, dir.z);
        client.player.velocityModified = true;
    }

    public static void moveForwardLeft(float speed) {
        if (client.player == null) return;
        Vec3d forward = getForwardVector(speed);
        Vec3d strafe = getStrafeVector(speed);
        Vec3d result = forward.add(strafe).normalize().multiply(speed);
        client.player.setVelocity(result.x, client.player.getVelocity().y, result.z);
        client.player.velocityModified = true;
    }

    public static void moveForwardRight(float speed) {
        if (client.player == null) return;
        Vec3d forward = getForwardVector(speed);
        Vec3d strafe = getStrafeVector(-speed);
        Vec3d result = forward.add(strafe).normalize().multiply(speed);
        client.player.setVelocity(result.x, client.player.getVelocity().y, result.z);
        client.player.velocityModified = true;
    }

    public static void moveVertical(float speed) {
        if (client.player == null) return;
        Vec3d velocity = client.player.getVelocity();
        client.player.setVelocity(velocity.x, speed, velocity.z);
        client.player.velocityModified = true;
    }

    public static void setPlayerPos(double x,double y,double z) {
        if (client.player == null) return;
        client.player.setPos(x,y,z);
    }


    public static Vec3d getForwardVector(float speed) {
        double yaw = Math.toRadians(client.player.getYaw());
        double x = -Math.sin(yaw) * speed;
        double z = Math.cos(yaw) * speed;
        return new Vec3d(x, 0, z);
    }

    public static Vec3d getStrafeVector(float speed) {
        double yaw = Math.toRadians(client.player.getYaw() - 90);
        double x = -Math.sin(yaw) * speed;
        double z = Math.cos(yaw) * speed;
        return new Vec3d(x, 0, z);
    }

    public static boolean isMoving() {
        return forwardPressed() || backPressed() || leftPressed() || rightPressed();
    }

    public static boolean isMovingForwardLeft() {
        return forwardPressed() && leftPressed();
    }

    public static boolean isMovingForwardRight() {
        return forwardPressed() && rightPressed();
    }

    public static boolean forwardPressed() {
        return client.options.forwardKey.isPressed();
    }


    public static boolean backPressed() {
        return client.options.backKey.isPressed();
    }

    public static boolean leftPressed() {
        return client.options.leftKey.isPressed();
    }

    public static boolean rightPressed() {
        return client.options.rightKey.isPressed();
    }
}
