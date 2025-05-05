package net.deimos.api.cmd;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
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
            MinecraftClient client = MinecraftClient.getInstance();
            client.inGameHud.getChatHud().addMessage(getPrefix().append("Available commands:"));
            commands.keySet().forEach(cmd ->
                    client.inGameHud.getChatHud().addMessage(Text.of(" " + cmd))
            );
        });

        registerCommand("example", (args) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.inGameHud.getChatHud().addMessage(getPrefix().append("Example command executed!"));
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