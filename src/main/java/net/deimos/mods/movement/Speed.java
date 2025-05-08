package net.deimos.mods.movement;

import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.gui.settings.ModeSetting;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.util.MovementUtil;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;

@Module(
        name="Speed",
        description="Speeds the player up.",
        category=Category.Movement
)
public class Speed extends ModuleBuilder {

    private enum MovementModes {
        Bhop,
        LowHop,
        Ground,
        Quake
    }

    public SliderSetting speed = new SliderSetting.Builder()
            .setName("Speed")
            .setDescription("The speed the player should go.")
            .setMin(1)
            .setMax(10)
            .setDefaultValue(1)
            .build();

    public ModeSetting<MovementModes> mode = new ModeSetting.Builder<MovementModes>()
            .setName("Mode")
            .setDescription("Movement mode")
            .setDefaultValue(MovementModes.Ground)
            .build();

    private float speedValue = (float)(speed.value - 0.7);
    private float lastYaw = 0f;
    private boolean wasOnGround = false;

    @EventHandler
    public void onTick(TickEvent.Post post) {
        if (!enabled) return;
        assert client.player != null;

        if (MovementUtil.isMoving()) {
            switch ((MovementModes) mode.getMode()) {
                case Bhop -> handleBHop();
                case LowHop -> handleLowHop();
                case Quake -> handleQuake();
            }
            applyMovementSpeed();
        } else {
            if (mode.getMode() == MovementModes.Bhop || mode.getMode() == MovementModes.LowHop) {
                client.options.jumpKey.setPressed(false);
            }
        }

        lastYaw = client.player.getYaw();
        wasOnGround = client.player.isOnGround();
    }

    private void handleBHop() {
        assert client.player != null;
        client.options.jumpKey.setPressed(client.player.isOnGround());
    }

    private void handleLowHop() {
        assert client.player != null;
        if (client.player.isOnGround()) {
            client.player.jump();
            client.player.setVelocity(client.player.getVelocity().x, 0.45f, client.player.getVelocity().z);
        } else if (client.player.getVelocity().y > 0.1) {
            client.player.setVelocity(
                    client.player.getVelocity().x * 1.02f,
                    client.player.getVelocity().y * 0.6f,
                    client.player.getVelocity().z * 1.02f
            );
        }
    }

    private void handleQuake() {
        assert client.player != null;

        if (client.player.isOnGround()) {
            if (!wasOnGround) {
                client.player.setVelocity(
                        client.player.getVelocity().x,
                        0.0,
                        client.player.getVelocity().z
                );
            }

            if (MovementUtil.isMoving() && client.options.jumpKey.isPressed()) {
                client.player.jump();
                client.player.setVelocity(
                        client.player.getVelocity().x * 1.01f,
                        0.42f,
                        client.player.getVelocity().z * 1.01f
                );
            }
        } else {
            float yawDiff = client.player.getYaw() - lastYaw;
            float baseSpeed = (float) (speedValue * 0.8);
            float airAccelFactor = 0.5f;
            float airControlFactor = 0.3f;

            if (Math.abs(yawDiff) > 0.1f) {
                float wishdir = (float) Math.toRadians(-client.player.getYaw());
                float currentSpeed = (float) Math.sqrt(
                        client.player.getVelocity().x * client.player.getVelocity().x +
                                client.player.getVelocity().z * client.player.getVelocity().z
                );

                float addspeed = 0;
                float accelspeed = airAccelFactor * baseSpeed * 0.05f;

                if (MovementUtil.forwardPressed()) {
                    addspeed = baseSpeed - currentSpeed;
                    if (addspeed < 0) {
                        addspeed = 0;
                    }
                    if (addspeed > accelspeed) {
                        addspeed = accelspeed;
                    }
                }

                if (MovementUtil.isMoving() && !client.player.isOnGround()) {
                    float wishspeed = baseSpeed * airControlFactor;

                    float velx = (float) client.player.getVelocity().x;
                    float velz = (float) client.player.getVelocity().z;

                    float wishx = (float) Math.sin(wishdir) * wishspeed;
                    float wishz = (float) Math.cos(wishdir) * wishspeed;

                    client.player.setVelocity(
                            velx + wishx * addspeed,
                            client.player.getVelocity().y * 0.99f,
                            velz + wishz * addspeed
                    );
                }
            }

            if (client.player.getVelocity().y < 0) {
                client.player.setVelocity(
                        client.player.getVelocity().x,
                        client.player.getVelocity().y * 1.03f,
                        client.player.getVelocity().z
                );
            }
        }
    }

    private void applyMovementSpeed() {
        if (mode.getMode() == MovementModes.Quake) {
            return;
        }

        if (MovementUtil.forwardPressed()) {
            MovementUtil.moveForward(speedValue);
        }
        if (MovementUtil.backPressed()) {
            MovementUtil.moveBackward(speedValue);
        }
        if (MovementUtil.leftPressed()) {
            MovementUtil.strafeLeft(speedValue);
        }
        if (MovementUtil.rightPressed()) {
            MovementUtil.strafeRight(speedValue);
        }
        if (MovementUtil.forwardPressed() && MovementUtil.rightPressed()) {
            MovementUtil.moveForwardRight(speedValue);
        }
        if (MovementUtil.forwardPressed() && MovementUtil.leftPressed()) {
            MovementUtil.moveForwardLeft(speedValue);
        }
    }

    public Speed() {
        addSetting(speed);
        addSetting(mode);
    }
}