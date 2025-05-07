package net.deimos.api.cmd;

import net.deimos.Deimos;
import net.deimos.api.interfaces.IClient;
import net.deimos.mods.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry implements IClient {
    private static final Map<String, CommandExecutor> commands = new HashMap<>();

    public static MutableText getPrefix() {
        return Text.literal("[")
                .formatted(Formatting.WHITE)
                .append(Text.literal("deimos")
                        .formatted(Formatting.BLUE))
                .append(Text.literal("]")
                        .formatted(Formatting.WHITE))
                .append(Text.literal(" "));
    }

    static {
        registerCommand("help", (args) -> {
            client.inGameHud.getChatHud().addMessage(getPrefix().append("Available commands:"));
            commands.keySet().forEach(cmd ->
                    client.inGameHud.getChatHud().addMessage(Text.of(" " + cmd))
            );
        });

        registerCommand("example", (args) -> {
            client.inGameHud.getChatHud().addMessage(getPrefix().append("Example command executed!"));
        });

        registerCommand("gui", (args) ->
        {
            client.setScreen(Deimos.clickGUI);
        });

        registerCommand("module", (args) ->
        {
            Deimos.MOD_MANAGER.toggle(args[0]);
        });
    }

    public static void registerCommand(String name, CommandExecutor executor) {
        commands.put(name, executor);
    }

    public static boolean executeCommand(String name, String[] args) {
        CommandExecutor executor = commands.get(name);
        if (executor != null) {
            executor.execute(args);
            return true;
        }
        return false;
    }

    @FunctionalInterface
    public interface CommandExecutor {
        void execute(String[] args);
    }
}