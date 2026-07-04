package me.unoprojects.unocore.api.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SubCommand<T extends JavaPlugin> extends CommandAPICommand {

    protected T plugin;

    protected SubCommand(T plugin, String name) {
        super(name);
        this.plugin = plugin;

        setup();
    }

    protected abstract void setup();
}
