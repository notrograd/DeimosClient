package net.deimos.api.gui.settings;

import net.deimos.api.settings.Setting;
import net.deimos.api.interfaces.IClient;
import net.deimos.api.interfaces.IVisible;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.function.Consumer;

public class ModeSetting<E extends Enum<E>> extends Setting<E> implements IClient {

    private boolean hovered = false;
    private final E[] values;

    public ModeSetting(String name, String description, E defaultValue, Consumer<E> onChanged, Consumer<Setting<E>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.values = defaultValue.getDeclaringClass().getEnumConstants();
    }

    public static class Builder<E extends Enum<E>> {
        private String name;
        private String description;
        private E defaultValue;
        private Consumer<E> onChanged;
        private Consumer<Setting<E>> onModuleActivated;
        private IVisible visible;

        public Builder<E> setName(String name) {
            this.name = name;
            return this;
        }

        public Builder<E> setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder<E> setDefaultValue(E defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder<E> setOnChanged(Consumer<E> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder<E> setOnModuleActivated(Consumer<Setting<E>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public Builder<E> setVisible(IVisible visible) {
            this.visible = visible;
            return this;
        }

        public ModeSetting<E> build() {
            return new ModeSetting<>(name, description, defaultValue, onChanged, onModuleActivated, visible);
        }
    }

    public void cycle() {
        int currentIndex = getCurrentIndex();
        int nextIndex = (currentIndex + 1) % values.length;
        setValue(values[nextIndex]);
    }

    private int getCurrentIndex() {
        E currentValue = getValue();
        for (int i = 0; i < values.length; i++) {
            if (values[i] == currentValue) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void render(DrawContext context, float mouseX, float mouseY, int x, int y, int width, int height) {
        hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int backgroundColor = new Color(12, 121, 232, hovered ? 200 : 150).getRGB();

        context.fill(x, y, x + width, y + height, backgroundColor);
        context.drawText(client.textRenderer, getName() + ": " + getValue().toString(), x + 5, y + 2, Color.WHITE.getRGB(), true);
    }

    @Override
    public void onClicked(float mouseX, float mouseY, int button) {
        if (hovered && button == 0) {
            cycle();
        }
    }
}