package net.deimos.api.gui.settings;

import net.deimos.api.settings.Setting;
import net.deimos.api.interfaces.IClient;
import net.deimos.api.interfaces.IVisible;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.function.Consumer;

public class SliderSetting extends Setting<Integer> implements IClient {
    private final int min;
    private final int max;
    private boolean hovered = false;

    public SliderSetting(String name, String description, Integer defaultValue, int min, int max,
                         Consumer<Integer> onChanged, Consumer<Setting<Integer>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.min = min;
        this.max = max;
    }

    public static class Builder {
        private String name;
        private String description;
        private Integer defaultValue;
        private int min;
        private int max;
        private Consumer<Integer> onChanged;
        private Consumer<Setting<Integer>> onModuleActivated;
        private IVisible visible;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setDefaultValue(Integer defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder setMin(int min) {
            this.min = min;
            return this;
        }

        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        public Builder setOnChanged(Consumer<Integer> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder setOnModuleActivated(Consumer<Setting<Integer>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public Builder setVisible(IVisible visible) {
            this.visible = visible;
            return this;
        }

        public SliderSetting build() {
            return new SliderSetting(name, description, defaultValue, min, max, onChanged, onModuleActivated, visible);
        }
    }

    @Override
    public void render(DrawContext context, float mouseX, float mouseY, int x, int y, int width, int height) {
        super.render(context, mouseX, mouseY, x, y, width, height);
        hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int sliderColor = new Color(147, 27, 27, 210).getRGB();

        String v =  getValue().toString();
        int textWidth = client.textRenderer.getWidth(v);

        context.drawTextWithShadow(client.textRenderer, getName(), x + 5, y + 2, Color.WHITE.getRGB());
        context.drawTextWithShadow(client.textRenderer, getValue().toString(), x + ( width - (textWidth + 4) ), y + 2, Color.WHITE.getRGB());

        int sliderWidth = (int) ((getValue() - min) / (float) (max - min) * width);
        context.fillGradient(x, y, x + sliderWidth, y + height, sliderColor, sliderColor);
    }

    @Override
    public void onDrag(float mouseX, float mouseY, int button, float deltaX, float deltaY) {
        if (hovered) updateValue(mouseX);
        super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateValue(float mouseX) {
        int newValue = (int) (((mouseX - x) / width) * (max - min) + min);
        newValue = Math.max(min, Math.min(max, newValue));
        setValue(newValue);
    }
}
