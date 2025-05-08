package net.deimos.api.mods;

import net.deimos.api.event.EventManager;
import net.deimos.api.rotations.RotationManager;
import net.deimos.api.settings.Setting;
import net.deimos.api.interfaces.Module;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public abstract class ModuleBuilder {
    public RotationManager RotationManager = new RotationManager();
    public boolean enabled = false;
    public MinecraftClient client = MinecraftClient.getInstance();
    public void toggle() {
        this.enabled = !this.enabled;
    }

    ArrayList<Setting<?>> settings = new ArrayList<>();
    public String name;
    public String description;
    public Category category;

    public ModuleBuilder() {
        EventManager.register(this);

        Class<?> clazz = this.getClass();

        if (clazz.isAnnotationPresent(Module.class)) {
            Module moduleAnnotation = clazz.getAnnotation(Module.class);

            this.name = moduleAnnotation.name();
            this.description = moduleAnnotation.description();
            this.category = moduleAnnotation.category();
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Setting<?>> getSettings() {
        return settings;
    }

    public void addSetting(Setting<?> s) {
        this.settings.add(s);
    }

    public Category getCategory() {
        return category;
    }

    public void SetEnabled(boolean e) {
        this.enabled = e;
    }

    public boolean getEnabled() {
        return this.enabled;
    }
}