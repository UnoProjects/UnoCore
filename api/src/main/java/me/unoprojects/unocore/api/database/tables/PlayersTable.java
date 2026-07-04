package me.unoprojects.unocore.api.database.tables;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.api.database.DatabaseTable;
import org.intellij.lang.annotations.Language;

import java.util.UUID;

public abstract class PlayersTable extends DatabaseTable<UnoCore> {

    protected PlayersTable(ConnectionProvider provider, UnoCore plugin, @Language("SQL") String... tableQueries) {
        super(provider, plugin, tableQueries);
    }

    public abstract int getOrCreatePlayer(UUID uuid, String name);
}
