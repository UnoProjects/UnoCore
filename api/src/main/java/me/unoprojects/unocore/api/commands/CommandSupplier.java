package me.unoprojects.unocore.api.commands;

import dev.jorel.commandapi.CommandAPICommand;

import java.util.function.Supplier;

public abstract class CommandSupplier implements Supplier<CommandAPICommand[]> {

    protected final String name;

    public CommandSupplier(String name) {
        this.name = name;
    }

    protected CommandAPICommand create(String command) {
        return new CommandAPICommand(command);
    }

    public String getName() {
        return this.name;
    }
}
