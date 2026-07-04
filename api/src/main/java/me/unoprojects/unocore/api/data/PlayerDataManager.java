package me.unoprojects.unocore.api.data;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerDataManager {

    /**
     * Registers a component provider.
     *
     * @param provider the provider to register
     * @param <T>      the type of the component
     */
    <T extends PlayerComponent> void registerProvider(PlayerComponentProvider<T> provider);

    /**
     * Gets the online player's data by UUID.
     *
     * @param uuid the player's UUID
     * @return an Optional containing the UnoPlayer if online
     */
    Optional<UnoPlayer> getPlayer(UUID uuid);

    /**
     * Gets the online player's data.
     *
     * @param player the Bukkit player
     * @return an Optional containing the UnoPlayer if online
     */
    Optional<UnoPlayer> getPlayer(Player player);

    /**
     * Gets all currently online players' data.
     *
     * @return a collection of online UnoPlayers
     */
    Collection<UnoPlayer> getOnlinePlayers();

    Map<UUID, UnoPlayer> getPreLoadedPlayers();

    Map<UUID, UnoPlayer> getOnlinePlayersMap();

    CompletableFuture<UnoPlayer> loadAllComponents(UnoPlayer player);

    CompletableFuture<Void> saveAllComponents(UnoPlayer player);
}
