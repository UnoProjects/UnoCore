package me.unoprojects.unocore.database.tables;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.database.ConnectionProvider;
import me.unoprojects.unocore.api.database.tables.PlayersTable;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BasePlayersTable extends PlayersTable {

    @Language("SQL")
    private static final String PLAYERS_TABLE = """
            CREATE TABLE IF NOT EXISTS unocore_players (
                id          INT             AUTO_INCREMENT PRIMARY KEY,
                uuid        VARCHAR(36)     UNIQUE KEY NOT NULL,
                name        VARCHAR(16)     NOT NULL,
                first_join  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
                last_join   TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
            """;

    @Language("SQL")
    private static final String SELECT = """
            SELECT id
            FROM unocore_players
            WHERE uuid = ?;
            """;

    @Language("SQL")
    private static final String UPDATE = """
            UPDATE unocore_players
            SET name = ?, last_join = CURRENT_TIMESTAMP
            WHERE id = ?;
            """;

    @Language("SQL")
    private static final String INSERT = """
            INSERT INTO unocore_players (uuid, name)
            VALUES (?, ?);
            """;

    public BasePlayersTable(ConnectionProvider provider, UnoCore plugin) {
        super(provider, plugin, PLAYERS_TABLE);
    }

    /**
     * Retrieves the numeric primary key ID of a player by their UUID,
     * creating a new record if they do not exist.
     * Updates their username and last_join time.
     *
     * @param uuid the player's UUID
     * @param name the player's current username
     * @return the player's numeric database ID, or -1 if an error occurs
     */
    @Override
    public int getOrCreatePlayer(UUID uuid, String name) {
        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(SELECT)) {
            selectStmt.setString(1, uuid.toString());
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    try (PreparedStatement updateStmt = conn.prepareStatement(UPDATE)) {
                        updateStmt.setString(1, name);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                    }
                    return id;
                }
            }
        } catch (SQLException e) {
            logError(e, "Error checking/updating player: " + uuid);
        }

        try (Connection conn = getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, uuid.toString());
            insertStmt.setString(2, name);
            insertStmt.executeUpdate();
            try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logError(e, "Error inserting player: " + uuid);
        }
        return -1;
    }
}
