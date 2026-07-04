package me.unoprojects.unocore.api.modules;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import me.unoprojects.unocore.api.UnoCore;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class UnoPlugin extends JavaPlugin {

    private final List<String> registeredCommandNames = new ArrayList<>();
    private final List<Class<?>> registeredServiceClasses = new ArrayList<>();

    @Override
    public final void onEnable() {
        UnoCore core = UnoCore.getInstance();
        if (core != null && core.getModuleManager() != null) {
            core.getModuleManager().registerModule(this);
        }
        onModuleEnable();
    }

    @Override
    public final void onDisable() {
        onModuleDisable();

        for (String commandName : registeredCommandNames) {
            CommandAPI.unregister(commandName);
        }
        registeredCommandNames.clear();

        UnoCore core = UnoCore.getInstance();
        if (core != null) {
            if (core.getServiceRegistry() != null) {
                for (Class<?> serviceClass : registeredServiceClasses) {
                    core.getServiceRegistry().unregister(serviceClass);
                }
            }
            registeredServiceClasses.clear();

            if (core.getModuleManager() != null) {
                core.getModuleManager().unregisterModule(this);
            }
        }
    }

    /**
     * Called when the module is enabled.
     */
    protected void onModuleEnable() {}

    /**
     * Called when the module is disabled.
     */
    protected void onModuleDisable() {}

    /**
     * Helper to register an event listener.
     *
     * @param listener the listener to register
     */
    protected void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Helper to register CommandAPI commands.
     *
     * @param commands the commands to register
     */
    protected void registerCommands(CommandAPICommand... commands) {
        for (CommandAPICommand command : commands) {
            command.register();
            registeredCommandNames.add(command.getName());
        }
    }

    /**
     * Helper to register a service in the central registry.
     *
     * @param serviceClass   the service class/interface
     * @param implementation the service implementation
     * @param <S>            the type of the service
     */
    protected <S> void registerService(Class<S> serviceClass, S implementation) {
        UnoCore core = UnoCore.getInstance();
        if (core != null && core.getServiceRegistry() != null) {
            core.getServiceRegistry().register(serviceClass, implementation);
            registeredServiceClasses.add(serviceClass);
        }
    }

    /**
     * Helper to retrieve a service from the central registry.
     *
     * @param serviceClass the service class/interface
     * @param <S>          the type of the service
     * @return an Optional containing the service if registered
     */
    protected <S> Optional<S> getService(Class<S> serviceClass) {
        UnoCore core = UnoCore.getInstance();
        if (core != null && core.getServiceRegistry() != null) {
            return core.getServiceRegistry().get(serviceClass);
        }
        return Optional.empty();
    }
}
