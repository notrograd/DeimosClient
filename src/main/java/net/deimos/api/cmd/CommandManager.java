package net.deimos.api.cmd;

import java.util.ArrayList;

public class CommandManager {
    public static ArrayList<Command> commands = new ArrayList<>();

    private static void initList()
    {
    }

    public static void init() {
        initList();

        for (Command command : commands)
        {
            CommandRegistry.registerCommand(command.body, command.executor);
        }
    }
}
