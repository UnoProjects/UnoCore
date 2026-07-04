package me.unoprojects.unocore;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.database.BaseConnectionProvider;

public class DefaultUnoCore extends UnoCore {

    private ConnectionProvider connectionProvider;

    @Override
    public void onEnable() {
        this.connectionProvider = new BaseConnectionProvider(this);
        this.connectionProvider.connect();
    }

    @Override
    public void onDisable() {
        if (connectionProvider != null) connectionProvider.disconnect();
    }

    @Override
    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }
}
