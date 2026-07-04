package me.unoprojects.unocore.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.database.ConnectionProvider;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseConnectionProvider implements ConnectionProvider {

    private final UnoCore plugin;
    protected HikariDataSource dataSource;

    public BaseConnectionProvider(UnoCore plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        plugin.getLogger().info("Connecting to database...");

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("sql");
        if (section == null) {
            plugin.getLogger().severe("No sql section found in config.yml");
            return;
        }

        String hostname = section.getString("hostname");
        String database = section.getString("database");
        String username = section.getString("auth.username");
        String password = section.getString("auth.password");
        boolean useSSL = section.getBoolean("useSSL");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + "/" + database + "?useSSL=" + useSSL);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setMaxLifetime(1800000L);
        config.setConnectionTimeout(5000L);
        config.addDataSourceProperty("allowPublicKeyRetrieval", "true");

        dataSource = new HikariDataSource(config);
        plugin.getLogger().info("Connected to database.");
    }

    public void disconnect() {
        if (dataSource == null) return;

        dataSource.close();
        plugin.getLogger().info("Disconnected from database.");
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
