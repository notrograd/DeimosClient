package net.deimos.mods.movement;

import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;
import net.deimos.api.mods.ModuleBuilder;

@Module(
        name="Step",
        description="Step over blocks.",
        category=Category.Movement
)
public class Step extends ModuleBuilder {
    public SliderSetting height = new SliderSetting.Builder()
            .setName("Step Height")
            .setDescription("How tall you can step.")
            .setDefaultValue(1)
            .setMax(4)
            .setMin(0)
            .build();
}
