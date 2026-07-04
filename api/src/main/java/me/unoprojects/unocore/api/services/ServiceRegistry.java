package me.unoprojects.unocore.api.services;

import java.util.Optional;

public interface ServiceRegistry {

    /**
     * Registers a service provider.
     *
     * @param serviceClass   the interface/class representing the service
     * @param implementation the implementation of the service
     * @param <T>            the type of the service
     */
    <T> void register(Class<T> serviceClass, T implementation);

    /**
     * Unregisters a service provider.
     *
     * @param serviceClass the interface/class representing the service
     */
    void unregister(Class<?> serviceClass);

    /**
     * Gets a registered service provider.
     *
     * @param serviceClass the interface/class representing the service
     * @param <T>          the type of the service
     * @return an Optional containing the service implementation if registered, or empty
     */
    <T> Optional<T> get(Class<T> serviceClass);
}
