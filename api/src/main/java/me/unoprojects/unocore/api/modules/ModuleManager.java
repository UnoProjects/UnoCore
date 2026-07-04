package me.unoprojects.unocore.api.modules;

import java.util.Collection;
import java.util.Optional;

public interface ModuleManager {

    /**
     * Registers a module. Called automatically by UnoPlugin on enable.
     *
     * @param module the module to register
     */
    void registerModule(UnoPlugin module);

    /**
     * Unregisters a module. Called automatically by UnoPlugin on disable.
     *
     * @param module the module to unregister
     */
    void unregisterModule(UnoPlugin module);

    /**
     * Gets all loaded modules (both enabled and disabled).
     *
     * @return a collection of all UnoPlugin modules
     */
    Collection<UnoPlugin> getModules();

    /**
     * Gets a module by its plugin name.
     *
     * @param name the plugin name
     * @return the module if found
     */
    Optional<UnoPlugin> getModule(String name);

    /**
     * Gets a module by its class.
     *
     * @param clazz the module class
     * @param <T> the type of the module
     * @return the module if found
     */
    <T extends UnoPlugin> Optional<T> getModule(Class<T> clazz);
}
