package net.deimos.mods.movement;
import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.gui.settings.BoolSetting;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.util.MovementUtil;
import net.deimos.api.i.EventHandler;
import net.deimos.api.i.Module;
import net.deimos.api.mods.Category;

@Module(
        name="Speed",
        description="Speeds the player up.",
        category=Category.Movement
)
public class Speed extends ModuleBuilder {

    public SliderSetting speed = new SliderSetting.Builder()
            .setName("Speed")
            .setDescription("The speed the player should go.")
            .setMin(1)
            .setMax(10)
            .setDefaultValue(1)
            .build();
    public BoolSetting strafe = new BoolSetting.Builder()
            .setName("Strafe")
            .setDescription("Should the player strafe")
            .setDefaultValue(false)
            .build();
    @EventHandler
    public void onTick(TickEvent.Post post)
    {

        if (!enabled) return;
        assert client.player != null;

        if (MovementUtil.isMoving())
        {
            if (client.player.isOnGround())
            {
                client.options.jumpKey.setPressed(true);
            }

            if (MovementUtil.forwardPressed())
            {
                MovementUtil.moveForward((float) (speed.value - 0.8));
            }
            if (MovementUtil.backPressed())
            {
                MovementUtil.moveBackward((float) (speed.value - 0.8));
            }
            if (MovementUtil.leftPressed())
            {
                MovementUtil.strafeLeft((float) (speed.value - 0.8));
            }
            if(MovementUtil.rightPressed())
            {
                MovementUtil.strafeRight((float) (speed.value - 0.8));
            }
            if(MovementUtil.forwardPressed() && MovementUtil.rightPressed())
            {
                MovementUtil.moveForwardRight((float) (speed.value - 0.8));
            }
            if(MovementUtil.forwardPressed() && MovementUtil.leftPressed())
            {
                MovementUtil.moveForwardLeft((float) (speed.value - 0.8));
            }
        }
    }

    /*ClassInitializer*/{
        addSetting(speed);
        addSetting(strafe);
    }/*End ClassInitializer*/

}
