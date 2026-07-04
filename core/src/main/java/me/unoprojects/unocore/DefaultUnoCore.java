package me.unoprojects.unocore;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.data.PlayerDataManager;
import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.api.database.DatabaseTable;
import me.unoprojects.unocore.api.database.tables.PlayersTable;
import me.unoprojects.unocore.api.modules.ModuleManager;
import me.unoprojects.unocore.api.services.ServiceRegistry;
import me.unoprojects.unocore.commands.UnoCoreCommand;
import me.unoprojects.unocore.data.BasePlayerDataManager;
import me.unoprojects.unocore.database.BaseConnectionProvider;
import me.unoprojects.unocore.database.tables.BasePlayersTable;
import me.unoprojects.unocore.listeners.PlayerConnectionListener;
import me.unoprojects.unocore.modules.BaseModuleManager;
import me.unoprojects.unocore.services.BaseServiceRegistry;
import org.bukkit.Bukkit;

public class DefaultUnoCore extends UnoCore {

    private ConnectionProvider connectionProvider;
    private ModuleManager moduleManager;
    private ServiceRegistry serviceRegistry;
    private PlayerDataManager playerDataManager;
    private PlayersTable playersTable;

    @Override
    public void onLoad() {
        setInstance(this);
    }

    @Override
    public void onEnable() {
        this.moduleManager = new BaseModuleManager();
        this.serviceRegistry = new BaseServiceRegistry();
        this.playerDataManager = new BasePlayerDataManager();

        this.connectionProvider = new BaseConnectionProvider(this);
        this.connectionProvider.connect();

        this.playersTable = new BasePlayersTable(connectionProvider, this);

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this, playerDataManager), this);

        for (CommandAPICommand command : new UnoCoreCommand().get()) command.register();
    }

    @Override
    public void onDisable() {
        if (connectionProvider != null) connectionProvider.disconnect();
        DatabaseTable.shutdown();
        CommandAPI.unregister("unocore");
    }

    @Override
    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    @Override
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    @Override
    public PlayersTable getPlayersTable() {
        return playersTable;
    }
}

