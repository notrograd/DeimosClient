package net.deimos.api.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventListener {

    Method target;
    Class<?> targetEventClass;
    Object origin;
    int prio;

    public EventListener(Object origin, Method target, Class<?> targetEventClass, int priority) {
        this.target = target;
        this.targetEventClass = targetEventClass;
        this.origin = origin;
        this.prio = priority;
    }

    public Object getOrigin() {
        return origin;
    }

    public void call(Object obj, Event e) {
        try {
            target.invoke(obj, e);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Class<?> getTargetEventClass() {
        return targetEventClass;
    }

    public Method getTargetMethod() {
        return target;
    }

    public int getPrio() {
        return prio;
    }
}
