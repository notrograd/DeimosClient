package net.deimos.api.cmd;

public class Command {

    public String body;
    public CommandRegistry.CommandExecutor executor;

    public Command(String body, CommandRegistry.CommandExecutor executor) {
        this.body = body;
        this.executor = executor;
    }
}
