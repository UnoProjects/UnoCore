package me.unoprojects.unocore.api;

import me.unoprojects.unocore.api.database.ConnectionProvider;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class UnoCore extends JavaPlugin {

    public abstract ConnectionProvider getConnectionProvider();
}
