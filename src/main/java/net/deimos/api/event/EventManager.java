package net.deimos.api.event;

import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.interfaces.EventHandler;
import net.deimos.api.interfaces.IClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class EventManager implements IClient {

    public static HashMap<Class<?>, ArrayList<EventListener>> listeners = new HashMap<>();

    public static void register(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class) && method.getParameters().length != 0) {
                Class<?> referringClass = method.getParameters()[0].getType();
                listeners.computeIfAbsent(referringClass, l -> new ArrayList<>()).add(
                        new EventListener(obj, method, referringClass, method.getAnnotation(EventHandler.class).Priority())
                );
            }
        }
    }

    public static void post(Event e) {
        try {
            if (listeners.containsKey(e.getClass())) {
                ArrayList<EventListener> list = listeners.get(e.getClass());
                ArrayList<EventListener> s =  ((ArrayList<EventListener>)
                        list.clone()
                );

                s.sort(Comparator.comparingInt(EventListener::getPrio));
                for (EventListener listener : s) {
                    if (listener.getOrigin() instanceof ModuleBuilder tmp) {
                        if (tmp.enabled && client.world != null) {
                            listener.call(listener.getOrigin(), e);
                        }
                    } else {
                        listener.call(listener.getOrigin(), e);
                    }
                }
            }
        } catch (Exception ed) {
            throw new RuntimeException(ed);
        }
    }
}
