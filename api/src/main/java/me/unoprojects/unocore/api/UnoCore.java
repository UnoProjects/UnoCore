package me.unoprojects.unocore.api;

import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.api.data.PlayerDataManager;
import me.unoprojects.unocore.api.database.tables.PlayersTable;
import me.unoprojects.unocore.api.modules.ModuleManager;
import me.unoprojects.unocore.api.services.ServiceRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class UnoCore extends JavaPlugin {

    private static UnoCore instance;

    public static UnoCore getInstance() {
        return instance;
    }

    protected static void setInstance(UnoCore inst) {
        instance = inst;
    }

    public abstract ConnectionProvider getConnectionProvider();

    public abstract ModuleManager getModuleManager();

    public abstract ServiceRegistry getServiceRegistry();

    public abstract PlayerDataManager getPlayerDataManager();

    public abstract PlayersTable getPlayersTable();
}

