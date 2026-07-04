package me.unoprojects.unocore.api.database;

import org.bukkit.plugin.java.JavaPlugin;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.logging.Level;

public abstract class DatabaseTable<T extends JavaPlugin> {

    private static final ExecutorService DATABASE_EXECUTOR = Executors.newFixedThreadPool(5);

    protected final ConnectionProvider provider;
    protected final T plugin;

    protected DatabaseTable(ConnectionProvider provider, T plugin, @Language("SQL") String... tableQueries) {
        this.provider = provider;
        this.plugin = plugin;

        for (String query : tableQueries) {
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.execute();
            } catch (SQLException e) {
                logError(e, "Unable to create table: " + query);
            }
        }
    }

    public static <V> CompletableFuture<V> supplyAsync(Supplier<V> supplier) {
        return CompletableFuture.supplyAsync(supplier, DATABASE_EXECUTOR);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, DATABASE_EXECUTOR);
    }

    public static void shutdown() {
        if (!DATABASE_EXECUTOR.isShutdown()) DATABASE_EXECUTOR.shutdown();
    }

    public void logError(Exception throwable, String message) {
        plugin.getLogger().log(Level.SEVERE, message, throwable);
    }

    protected Connection getConnection() throws SQLException {
        return this.provider.getConnection();
    }
}