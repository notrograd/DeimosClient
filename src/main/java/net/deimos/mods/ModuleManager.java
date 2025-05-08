package net.deimos.mods;

import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.mods.movement.*;
import net.deimos.mods.pvp.KillAura;
import net.deimos.mods.render.HUD;
import net.deimos.mods.render.HoleESP;

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

//
//    public boolean enabled(String name) {
//        for (ModuleBuilder m : registry) {
//            if (m.name.equalsIgnoreCase(name)) {
//                return m.getEnabled();
//            }
//        }
//        return false;
//    }

    public static NoSlow NO_SLOW = new NoSlow();
    public static Step STEP = new Step();
    public void init()
    {
        // !!!add all modules here!!!
        this.register(new AutoSprint());
        this.register(new KillAura());
        this.register(new Speed());
        this.register(new HUD());
        this.register(new HoleESP());
        this.register(new Flight());
        // mixin mods
        this.register(NO_SLOW);
        this.register(STEP);
    }
}
