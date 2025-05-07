package net.deimos.api.settings;

import net.deimos.api.interfaces.IVisible;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class Setting<T> {
    public final String name, description;

    public int x, y, width, height;

    protected final T defaultValue;
    public T value;
    public final Consumer<Setting<T>> onModuleActivated;
    private final Consumer<T> onChanged;
    public double lengthValue;

    public Setting(String name, String description, T defaultValue, Consumer<T> onChanged,
                   Consumer<Setting<T>> onModuleActivated, IVisible visible) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.onChanged = onChanged;
        this.onModuleActivated = onModuleActivated;

        this.lengthValue = 1.0;
        this.value = defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        if (onChanged != null) {
            onChanged.accept(value);
        }
    }

    public String getName() {
        return name;
    }


    public boolean isValueTrue() {
        if (this.value instanceof Boolean) {
            return (Boolean) this.value;
        }
        return false;
    }

    public Enum<?> getMode() {
        if (this.value instanceof Enum<?>) {
            return (Enum<?>) this.value;
        }
        return null;
    }

    // Rendering
    public void render(DrawContext context, float mouseX, float mouseY, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void onReleased(float mouseX, float mouseY, int button) {}
    public void onClicked(float mouseX, float mouseY, int button) {}
    public void onDrag(float mouseX, float mouseY, int button, float deltaX, float deltaY) {}
}
