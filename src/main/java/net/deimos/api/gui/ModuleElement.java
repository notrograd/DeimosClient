package net.deimos.api.gui;

import net.deimos.api.gui.settings.BoolSetting;
import net.deimos.api.gui.settings.ModeSetting;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.settings.Setting;
import net.deimos.api.interfaces.IClient;
import net.deimos.api.util.MouseUtil;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ModuleElement implements IClient {
    int x, y, wid, hei;
    boolean expanded = false;
    ModuleBuilder module;
    private static final DescriptionBox descriptionBox = new DescriptionBox();
    private boolean isHovered = false;

    public ModuleElement(ModuleBuilder module) {
        this.module = module;
    }

    public void render(DrawContext context, int x, int y, int wid, int hei, float mx, float my) {

        this.x = x;
        this.y = y;
        this.wid = wid;
        this.hei = hei;

        boolean currentlyHovered = hovered(x, y, wid, 12, mx, my);

        if (currentlyHovered && !isHovered) {
            descriptionBox.setDescription(module.getDescription());
        } else if (!currentlyHovered && isHovered) {
            descriptionBox.clearDescription();
        }
        isHovered = currentlyHovered;

        if (!module.getEnabled()) {
            if (currentlyHovered) {
                context.fill(x, y, x + wid, y + 12, new Color(147, 27, 27, 111).getRGB());
            } else {
                context.fill(x, y, x + wid, y + 12, new Color(147, 27, 27, 111).getRGB());
            }
        } else {
            context.fill(x, y, x + wid, y + 12, new Color(232, 12, 12, 180).getRGB());
        }

        context.drawText(client.textRenderer, module.getName(), x + 5, y + 2, Color.WHITE.getRGB(), true);

        context.drawText(client.textRenderer, (expanded ? " -" : " +"), x + (wid - 13), y + 2, Color.WHITE.getRGB(), true);

        if (expanded) {
            renderSettings(context, x, y + 12, wid, hei, mx, my);
        }

        if (currentlyHovered) {
            descriptionBox.render(context, mx, my);
        }
    }

    private void renderSettings(DrawContext context, int x, int y, int wid, int hei, float mx, float my) {
        int settingY = y;

        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof SliderSetting s) {
                s.render(context, mx, my, x, settingY, wid, 12);
            }

            if (setting instanceof BoolSetting s) {
                s.render(context, mx, my, x, settingY, wid, 12);
            }

            if (setting instanceof ModeSetting<?> s)
                s.render(context,mx,my,x,settingY,wid,12);
            settingY += 12;
        }
    }

    public void onClick(float mx, float my, int button) {
        if (hovered(x, y, wid, 12, mx, my)) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                module.toggle();
            } else {
                if (module.getSettings().isEmpty()) return;
                expanded = !expanded;
            }
        }

        for (Setting<?> setting : module.getSettings()) {
            setting.onClicked(mx, my, button);
        }
    }

    public boolean hovered(int x, int y, int wid, int hei, float mx, float my) {
        return mx >= x && mx <= x + wid && my >= y && my <= y + hei;
    }

    public int getHeight() {
        if (expanded) {
            return 12 + module.getSettings().size() * 12;
        }
        return 12;
    }

    public static DescriptionBox getDescriptionBox() {
        return descriptionBox;
    }
}