package net.deimos.mods.movement;

import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;

@Module(name="AutoSprint",
        description="Automatically sprint",
        category = Category.Movement)
public class AutoSprint extends ModuleBuilder {

    @EventHandler
    public void onTick(TickEvent.Post event)
    {
      if (!this.enabled) return;
      assert this.client.player != null;
        if (this.client.player.isDead() || this.client.player.isRiding()) return;

        if (!this.client.player.isSprinting())
        {
            if (this.client.options.forwardKey.isPressed())
            {
                this.client.player.setSprinting(true);
            }
            if (this.client.options.leftKey.isPressed())
            {
                this.client.player.setSprinting(true);
            }
            if (this.client.options.rightKey.isPressed())
            {
                this.client.player.setSprinting(true);
            }
            if (this.client.options.backKey.isPressed())
            {
                this.client.player.setSprinting(true);
            }
        }

    }
}
