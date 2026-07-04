package me.unoprojects.unocore.api.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UnoPlayer {

    private final int id;
    private final UUID uuid;
    private final String name;
    private final Map<Class<? extends PlayerComponent>, PlayerComponent> components = new ConcurrentHashMap<>();

    public UnoPlayer(int id, UUID uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @SuppressWarnings("unchecked")
    public <T extends PlayerComponent> T getComponent(Class<T> clazz) {
        if (clazz == null) return null;
        PlayerComponent component = components.get(clazz);
        if (component == null) return null;
        return (T) component;
    }

    public <T extends PlayerComponent> void addComponent(Class<T> clazz, T component) {
        if (clazz == null || component == null) return;
        components.put(clazz, component);
    }
}
