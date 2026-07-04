package me.unoprojects.unocore.listeners;

import me.unoprojects.unocore.DefaultUnoCore;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.data.PlayerDataManager;
import me.unoprojects.unocore.api.data.UnoPlayer;
import me.unoprojects.unocore.data.BasePlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PlayerConnectionListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerConnectionListener.class);

    private final UnoCore core;
    private final PlayerDataManager dataManager;

    public PlayerConnectionListener(DefaultUnoCore core, PlayerDataManager dataManager) {
        this.core = core;
        this.dataManager = dataManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        UUID uuid = event.getUniqueId();
        String name = event.getName();

        int id = core.getPlayersTable().getOrCreatePlayer(uuid, name);
        if (id == -1) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    Component.text("An error occurred loading your player data. Please reconnect.", NamedTextColor.RED));
            return;
        }

        UnoPlayer player = new UnoPlayer(id, uuid, name);

        try {
            dataManager.loadAllComponents(player).join();
            dataManager.getPreLoadedPlayers().put(uuid, player);
        } catch (Exception e) {
            LOGGER.error("Failed to load components for player {}", name, e);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    Component.text("An error occurred loading your player components. Please reconnect.", NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            dataManager.getPreLoadedPlayers().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        UnoPlayer player = dataManager.getPreLoadedPlayers().remove(uuid);

        if (player != null) {
            dataManager.getOnlinePlayersMap().put(uuid, player);
        } else {
            LOGGER.warn("Player {} joined but data was not preloaded. Loading synchronously on main thread...", event.getPlayer().getName());
            int id = core.getPlayersTable().getOrCreatePlayer(uuid, event.getPlayer().getName());
            if (id != -1) {
                UnoPlayer fallbackPlayer = new UnoPlayer(id, uuid, event.getPlayer().getName());
                try {
                    dataManager.loadAllComponents(fallbackPlayer).join();
                    dataManager.getOnlinePlayersMap().put(uuid, fallbackPlayer);
                } catch (Exception e) {
                    LOGGER.error("Failed to load components synchronously for {}", event.getPlayer().getName(), e);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        UnoPlayer player = dataManager.getOnlinePlayersMap().remove(uuid);

        dataManager.getPreLoadedPlayers().remove(uuid);

        if (player != null) {
            dataManager.saveAllComponents(player).thenRun(() -> {
                LOGGER.info("Successfully saved data for player {}", player.getName());
            });
        }
    }
}
