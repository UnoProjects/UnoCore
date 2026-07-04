package me.unoprojects.unocore.api.data;

import java.util.concurrent.CompletableFuture;

public interface PlayerComponentProvider<T extends PlayerComponent> {

    /**
     * Gets the Class of the component this provider manages.
     *
     * @return the class of the component
     */
    Class<T> getComponentClass();

    /**
     * Creates a default instance of the component for a player.
     *
     * @param player the player
     * @return a default component instance
     */
    T createDefault(UnoPlayer player);

    /**
     * Loads the component data asynchronously from database or storage.
     *
     * @param player the player
     * @return a CompletableFuture yielding the loaded component
     */
    CompletableFuture<T> load(UnoPlayer player);

    /**
     * Saves the component data asynchronously to database or storage.
     *
     * @param player    the player
     * @param component the component instance to save
     * @return a CompletableFuture representing the save task completion
     */
    CompletableFuture<Void> save(UnoPlayer player, T component);
}
