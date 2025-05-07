package net.deimos.api.gui.settings;

import net.deimos.api.settings.Setting;
import net.deimos.api.interfaces.IClient;
import net.deimos.api.interfaces.IVisible;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.function.Consumer;

public class BoolSetting extends Setting<Boolean> implements IClient {

    private boolean hovered = false;

    public BoolSetting(String name, String description, Boolean defaultValue, Consumer<Boolean> onChanged, Consumer<Setting<Boolean>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
    }

    public static class Builder {
        private String name;
        private String description;
        private Boolean defaultValue;
        private Consumer<Boolean> onChanged;
        private Consumer<Setting<Boolean>> onModuleActivated;
        private IVisible visible;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDefaultValue(Boolean defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder setOnChanged(Consumer<Boolean> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder setOnModuleActivated(Consumer<Setting<Boolean>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public Builder setVisible(IVisible visible) {
            this.visible = visible;
            return this;
        }

        public BoolSetting build() {
            return new BoolSetting(name, description, defaultValue, onChanged, onModuleActivated, visible);
        }
    }

    @Override
    public void render(DrawContext context, float mouseX, float mouseY, int x, int y, int width, int height) {
        hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int backgroundColor = getValue() ? new Color(232, 12, 12, hovered ? 200: 150).getRGB()
                : new Color(147, 27, 27, hovered ? 180 : 140).getRGB();

        context.fill(x, y, x + width, y + height, backgroundColor);
        context.drawText(client.textRenderer, getName(), x + 5, y + 2, Color.WHITE.getRGB(), true);
    }

    @Override
    public void onClicked(float mouseX, float mouseY, int button) {
        if (hovered && button == 0) {
            setValue(!getValue());
        }
    }
}
