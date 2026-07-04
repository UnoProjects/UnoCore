package me.unoprojects.unocore.modules;

import me.unoprojects.unocore.api.modules.ModuleManager;
import me.unoprojects.unocore.api.modules.UnoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BaseModuleManager implements ModuleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseModuleManager.class);
    private final Map<String, UnoPlugin> registeredModules = new ConcurrentHashMap<>();

    @Override
    public void registerModule(UnoPlugin module) {
        registeredModules.put(module.getName().toLowerCase(), module);
        LOGGER.info("Registered module: {}", module.getName());
    }

    @Override
    public void unregisterModule(UnoPlugin module) {
        registeredModules.remove(module.getName().toLowerCase());
        LOGGER.info("Unregistered module: {}", module.getName());
    }

    @Override
    public Collection<UnoPlugin> getModules() {
        Map<String, UnoPlugin> allModules = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (UnoPlugin module : registeredModules.values())
            allModules.put(module.getName(), module);

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin instanceof UnoPlugin)
                allModules.put(plugin.getName(), (UnoPlugin) plugin);
        }

        return allModules.values();
    }

    @Override
    public Optional<UnoPlugin> getModule(String name) {
        UnoPlugin module = registeredModules.get(name.toLowerCase());
        if (module != null) {
            return Optional.of(module);
        }
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin instanceof UnoPlugin) {
            return Optional.of((UnoPlugin) plugin);
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends UnoPlugin> Optional<T> getModule(Class<T> clazz) {
        for (UnoPlugin module : registeredModules.values()) {
            if (clazz.isInstance(module))
                return Optional.of((T) module);
        }
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (clazz.isInstance(plugin))
                return Optional.of((T) plugin);
        }
        return Optional.empty();
    }
}
