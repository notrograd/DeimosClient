package net.deimos.mods.render;

import net.deimos.api.gui.settings.BoolSetting;
import net.deimos.api.gui.settings.SliderSetting;
import net.deimos.api.interfaces.Module;
import net.deimos.api.mods.Category;
import net.deimos.api.mods.ModuleBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.awt.*;

@Module(
        name="HUD",
        category=Category.Render,
        description="Shows info on hud."
)
public class HUD extends ModuleBuilder {

    public final BoolSetting fps = new BoolSetting.Builder()
            .setName("FPS")
            .setDescription("Frames per second.")
            .setDefaultValue(true)
            .build();
    public final BoolSetting watermark = new BoolSetting.Builder()
            .setName("Watermark")
            .setDescription("Renders watermark on screen.")
            .setDefaultValue(true)
            .build();
    public final BoolSetting coordinates = new BoolSetting.Builder()
            .setName("Coordinates")
            .setDescription("Shows player coordinates on screen.")
            .setDefaultValue(false)
            .build();
    public final BoolSetting totemCounter = new BoolSetting.Builder()
            .setName("Totem Counter")
            .setDescription("Shows number of totems in inventory.")
            .setDefaultValue(true)
            .build();

    public final SliderSetting hue = new SliderSetting.Builder()
            .setName("Hue")
            .setDescription("The color in which everything is based")
            .setDefaultValue(0)
            .setMax(360)
            .setMin(0)
            .build();

    public HUD() {
        addSetting(fps);
        addSetting(watermark);
        addSetting(coordinates);
        addSetting(totemCounter);
        addSetting(hue);

        HudRenderCallback.EVENT.register(this::render);
    }

    private void render(DrawContext ctx, RenderTickCounter ignored) {
        if (!getEnabled() || client.player == null) return;

        int color = getColorFromHue().getRGB();
        int white = Color.WHITE.getRGB();
        int y = 4;

        if (watermark.getValue()) {
            ctx.drawText(client.textRenderer, "DeimosClient", 4, y, color, true);
            y += 12;
        }

        if (fps.getValue() && client.fpsDebugString != null) {
            String fpsText = "FPS: " + client.getCurrentFps();
            ctx.drawText(client.textRenderer, fpsText, 4, y, color, true);
        }

        if (coordinates.getValue()) {
            String coordText = String.format("XYZ: %.1f, %.1f, %.1f",
                    client.player.getX(),
                    client.player.getY(),
                    client.player.getZ());

            int textWidth = client.textRenderer.getWidth(coordText);
            int screenHeight = client.getWindow().getScaledHeight();

            ctx.drawText(client.textRenderer, coordText, 4, screenHeight - 12, color, true);
        }

        if (totemCounter.getValue()) {
            int totemCount = countTotems();
            String totemText = "Totems: ";
            String countText = String.valueOf(totemCount);

            int textWidth = client.textRenderer.getWidth(totemText);
            int screenHeight = client.getWindow().getScaledHeight();
            int centerY = screenHeight / 2;

            ctx.drawText(client.textRenderer, totemText, 4, centerY, color, true);
            ctx.drawText(client.textRenderer, countText, 4 + textWidth, centerY, white, true);
        }
    }

    private int countTotems() {
        if (client.player == null) return 0;

        int count = 0;

        if (client.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            count++;
        }

        if (client.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            count++;
        }

        for (ItemStack stack : client.player.getInventory().main) {
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count += stack.getCount();
            }
        }

        return count;
    }

    public Color getColorFromHue() {
        return Color.getHSBColor(hue.getValue() / 360f, 1.0f, 1.0f);
    }
}