package net.deimos;

import net.deimos.api.event.EventManager;
import net.deimos.api.event.impl.RenderEvent;
import net.deimos.api.event.impl.TickEvent;
import net.deimos.api.gui.ClickGui;
import net.deimos.mods.ModuleManager;
import net.deimos.api.interfaces.EventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Deimos implements ModInitializer {

    public static ClickGui clickGUI;
    private KeyBinding clickGuiKey;
    public static MinecraftClient mc = MinecraftClient.getInstance();
    public static ModuleManager MOD_MANAGER = new ModuleManager();
    public static String VERSION = "v0.1.0";
    @Override
    public void onInitialize() {

        System.out.println("Deimos version "+VERSION+"loaded");
        MOD_MANAGER.init();

        clickGUI = new ClickGui();
        clickGuiKey = new KeyBinding(
                "ClickGUI",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "deimosclient"
        );
        KeyBindingHelper.registerKeyBinding(clickGuiKey);
        EventManager.register(this);

        ClientTickEvents.END_CLIENT_TICK.register(this::tickchannel0);
        ClientTickEvents.START_CLIENT_TICK.register(this::tickchannel1);
        WorldRenderEvents.LAST.register(ctx -> EventManager.post(new RenderEvent()));
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (mc != null && clickGuiKey.isPressed()) {
            mc.setScreen(clickGUI);
        }
    }


    // $TickEvent.Post
    public void tickchannel0(MinecraftClient client)
    {
        EventManager.post(new TickEvent.Post());
    }
    // $TickEvent.Pre
    public void tickchannel1(MinecraftClient client)
    {
        EventManager.post(new TickEvent.Pre());
    }
}
