package net.deimos.api.gui;

import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.i.IClient;
import net.deimos.api.mods.Category;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;

public class CategoryElement implements IClient {

    int x, y;
    int width, height;

    ArrayList<ModuleElement> modules = new ArrayList<>();

    Category category;

    public CategoryElement(Category category, ArrayList<ModuleBuilder> moduleList, int x, int y, int width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = 0;

        for (ModuleBuilder module : moduleList) {
            modules.add(new ModuleElement(module));
        }
    }

    public void onRender(DrawContext context, float mouseX, float mouseY, float delta) {
        int mheight = 0;

        context.fill(x, y, x + width, y + 16,  new Color(232, 12, 12, 240).getRGB());
        context.drawText(client.textRenderer, category.name(), x + 5, y + 5, Color.WHITE.getRGB(), true);

        int currentY = y + 16;
        mheight += 16;

        for (ModuleElement element : modules) {
            element.render(context, x, currentY, width, 12, mouseX, mouseY);
            currentY += element.getHeight();
            mheight += element.getHeight();
        }

        context.drawBorder(x, y, width, mheight, new Color(0, 0, 0, 68).getRGB());
    }

    public void onClick(float mouseX, float mouseY, int button) {
        for (ModuleElement element : modules) {
            element.onClick(mouseX, mouseY, button);
        }
    }
}