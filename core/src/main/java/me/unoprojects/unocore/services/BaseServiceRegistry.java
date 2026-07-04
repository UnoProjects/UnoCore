package me.unoprojects.unocore.services;

import me.unoprojects.unocore.api.services.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BaseServiceRegistry implements ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceRegistry.class);
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    @Override
    public <T> void register(Class<T> serviceClass, T implementation) {
        if (serviceClass == null || implementation == null)
            throw new IllegalArgumentException("Service class and implementation cannot be null");
        services.put(serviceClass, implementation);
        LOGGER.info("Service registered: {}", serviceClass.getName());
    }

    @Override
    public void unregister(Class<?> serviceClass) {
        if (serviceClass == null) return;
        services.remove(serviceClass);
        LOGGER.info("Service unregistered: {}", serviceClass.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> get(Class<T> serviceClass) {
        if (serviceClass == null) return Optional.empty();
        Object implementation = services.get(serviceClass);
        if (implementation == null) return Optional.empty();
        return Optional.of((T) implementation);
    }
}
