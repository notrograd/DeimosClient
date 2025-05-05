package net.deimos.api.mods;

import net.deimos.api.i.Module;
import net.deimos.mods.movement.AutoSprint;
import net.deimos.mods.movement.NoSlow;
import net.deimos.mods.movement.Speed;
import net.deimos.mods.pvp.KillAura;

import java.util.ArrayList;
public class ModuleManager {
    public ArrayList<ModuleBuilder> registry = new ArrayList<>();

    public void register(ModuleBuilder c) {
        if (c.getClass().isAnnotationPresent(Module.class)) {
            Module a = c.getClass().getAnnotation(Module.class);

            c.name = a.name();
            c.description = a.description();
            c.category = a.category();

            registry.add(c);
        }
    }

    public void toggle(String name) {
        for (ModuleBuilder m : registry) {
            if (m.getClass().getName().equalsIgnoreCase(name)) {
                m.enabled = !m.enabled;
                System.out.println("Toggled " + name + " to " + m.enabled);
                return;
            }
        }
    }

    public boolean enabled(String name) {
        for (ModuleBuilder m : registry) {
            if (m.name.equalsIgnoreCase(name)) {
                return m.getEnabled();
            }
        }
        return false;
    }

    public void init()
    {
        // !!!add all modules here!!!
        this.register(new AutoSprint());
        this.register(new KillAura());
        this.register(new Speed());
        this.register(new NoSlow());
    }
}
