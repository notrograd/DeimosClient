package net.deimos.api.gui;

import net.deimos.Deimos;
import net.deimos.api.mods.ModuleBuilder;
import net.deimos.api.settings.Setting;
import net.deimos.api.mods.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClickGui extends Screen {

    int PANEL_WIDTH = 100;

    public ArrayList<CategoryElement> panels = new ArrayList<>();

    public ClickGui() {
        super(Text.of("deimosclient ClickGUI"));

        int x = 10;
        int y = 10;

        for (Category category : Category.values()) {
            if (category == Category.Misc) continue;
            if (category == Category.Player) continue;
            if (category == Category.World) continue;

            ArrayList<ModuleBuilder> mods = new ArrayList<>();
            for (ModuleBuilder m : Deimos.MOD_MANAGER.registry) {
                if (m.category != category) continue;

                String moduleIdentifier = m.getName();
                Set<String> addedModules = new HashSet<>();
                if (addedModules.contains(moduleIdentifier)) continue;

                mods.add(m);
                addedModules.add(moduleIdentifier);
            }

            if (!mods.isEmpty()) {
                panels.add(
                        new CategoryElement(category, mods, x, y, PANEL_WIDTH)
                );

                x += PANEL_WIDTH + 2;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (CategoryElement panel : panels) {
            panel.onRender(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CategoryElement panel : panels) {
            panel.onClick((float) mouseX, (float) mouseY, button);
        }

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryElement panelElement : panels) {
            for (ModuleElement moduleElement : panelElement.modules) {
                for (Setting<?> setting : moduleElement.module.getSettings()) {
                    setting.onReleased((float) mouseX, (float) mouseY, button);
                }
            }
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (CategoryElement panelElement : panels) {
            for (ModuleElement moduleElement : panelElement.modules) {
                for (Setting<?> setting : moduleElement.module.getSettings()) {
                    setting.onDrag((float) mouseX, (float) mouseY, button, (float) deltaX, (float) deltaY);
                }
            }
        }

        return true;
    }
}