package net.deimos.api.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TickDelayHandler {
    private static final List<DelayedTask> delayedTasks = new ArrayList<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(TickDelayHandler::onClientTick);
    }

    public static void runAfterTicks(Runnable action, int ticks) {
        delayedTasks.add(new DelayedTask(action, ticks));
    }

    private static void onClientTick(MinecraftClient client) {
        ListIterator<DelayedTask> iterator = delayedTasks.listIterator();
        while (iterator.hasNext()) {
            DelayedTask task = iterator.next();
            task.ticksRemaining--;
            if (task.ticksRemaining <= 0) {
                task.action.run();
                iterator.remove();
            }
        }
    }

    private static class DelayedTask {
        private final Runnable action;
        private int ticksRemaining;

        public DelayedTask(Runnable action, int ticksRemaining) {
            this.action = action;
            this.ticksRemaining = ticksRemaining;
        }
    }
}