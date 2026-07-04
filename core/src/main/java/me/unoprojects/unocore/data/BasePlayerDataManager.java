package me.unoprojects.unocore.data;

import me.unoprojects.unocore.api.data.PlayerComponent;
import me.unoprojects.unocore.api.data.PlayerComponentProvider;
import me.unoprojects.unocore.api.data.PlayerDataManager;
import me.unoprojects.unocore.api.data.UnoPlayer;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BasePlayerDataManager implements PlayerDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePlayerDataManager.class);

    private final Map<UUID, UnoPlayer> onlinePlayers = new ConcurrentHashMap<>();
    private final Map<UUID, UnoPlayer> preLoadedPlayers = new ConcurrentHashMap<>();
    private final List<PlayerComponentProvider<?>> providers = new CopyOnWriteArrayList<>();

    @Override
    public <T extends PlayerComponent> void registerProvider(PlayerComponentProvider<T> provider) {
        if (provider == null) return;
        providers.add(provider);
        LOGGER.info("Registered player data component provider: {}", provider.getComponentClass().getSimpleName());
    }

    @Override
    public Optional<UnoPlayer> getPlayer(UUID uuid) {
        if (uuid == null) return Optional.empty();
        return Optional.ofNullable(onlinePlayers.get(uuid));
    }

    @Override
    public Optional<UnoPlayer> getPlayer(Player player) {
        if (player == null) return Optional.empty();
        return getPlayer(player.getUniqueId());
    }

    @Override
    public Collection<UnoPlayer> getOnlinePlayers() {
        return Collections.unmodifiableCollection(onlinePlayers.values());
    }

    // -- Internal methods for load/save hooks --

    @Override
    public Map<UUID, UnoPlayer> getPreLoadedPlayers() {
        return preLoadedPlayers;
    }

    @Override
    public Map<UUID, UnoPlayer> getOnlinePlayersMap() {
        return onlinePlayers;
    }

    public List<PlayerComponentProvider<?>> getProviders() {
        return providers;
    }

    /**
     * Orchestrates loading all components asynchronously for a player.
     *
     * @param player the initial UnoPlayer base details
     * @return a CompletableFuture yielding the fully loaded UnoPlayer
     */
    @Override
    public CompletableFuture<UnoPlayer> loadAllComponents(UnoPlayer player) {
        if (providers.isEmpty()) {
            return CompletableFuture.completedFuture(player);
        }

        List<CompletableFuture<?>> loadFutures = new ArrayList<>();
        for (PlayerComponentProvider<?> provider : providers) {
            CompletableFuture<?> future = provider.load(player)
                    .thenAccept(component -> {
                        if (component != null) {
                            addComponentHelper(player, provider.getComponentClass(), component);
                        } else {
                            // Fallback to default if load returned null
                            addComponentHelper(player, provider.getComponentClass(), provider.createDefault(player));
                        }
                    })
                    .exceptionally(throwable -> {
                        LOGGER.error("Failed to load component {} for player {}", provider.getComponentClass().getSimpleName(), player.getName(), throwable);
                        // Safe fallback to default component on load error
                        addComponentHelper(player, provider.getComponentClass(), provider.createDefault(player));
                        return null;
                    });
            loadFutures.add(future);
        }

        return CompletableFuture.allOf(loadFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> player);
    }

    @SuppressWarnings("unchecked")
    private <C extends PlayerComponent> void addComponentHelper(UnoPlayer player, Class<?> componentClass, Object component) {
        player.addComponent((Class<C>) componentClass, (C) component);
    }

    /**
     * Orchestrates saving all components asynchronously for a player.
     *
     * @param player the UnoPlayer instance
     * @return a CompletableFuture representing all save tasks completed
     */
    @SuppressWarnings("unchecked")
    @Override
    public CompletableFuture<Void> saveAllComponents(UnoPlayer player) {
        if (providers.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        List<CompletableFuture<Void>> saveFutures = new ArrayList<>();
        for (PlayerComponentProvider<?> provider : providers) {
            PlayerComponent component = player.getComponent(provider.getComponentClass());
            if (component != null) {
                CompletableFuture<Void> future = ((PlayerComponentProvider<PlayerComponent>) provider)
                        .save(player, component)
                        .exceptionally(throwable -> {
                            LOGGER.error("Failed to save component {} for player {}", provider.getComponentClass().getSimpleName(), player.getName(), throwable);
                            return null;
                        });
                saveFutures.add(future);
            }
        }

        return CompletableFuture.allOf(saveFutures.toArray(new CompletableFuture[0]));
    }
}
