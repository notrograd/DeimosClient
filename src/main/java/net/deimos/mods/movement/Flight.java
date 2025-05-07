package net.deimos.mods.movement;

import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.util.MovementUtil;

@Module(name="Flight",
        category=Category.Movement,
        description="Fly around and wreak havoc.")
public class Flight extends ModuleBuilder {

    @EventHandler
    public void tick(TickEvent.Post event) {
        if (client.player == null) return;

        client.player.setOnGround(true);
        client.player.setVelocity(client.player.getVelocity().x, 0, client.player.getVelocity().z);
        client.player.velocityModified = true;

        float speed = 0.6f;

        if (MovementUtil.forwardPressed()) MovementUtil.moveForward(speed);
        if (MovementUtil.backPressed()) MovementUtil.moveBackward(speed);
        if (MovementUtil.leftPressed()) MovementUtil.strafeLeft(speed);
        if (MovementUtil.rightPressed()) MovementUtil.strafeRight(speed);

        if (client.options.jumpKey.isPressed()) MovementUtil.moveVertical(speed);
        if (client.options.sneakKey.isPressed()) MovementUtil.moveVertical(-speed);
    }
}
