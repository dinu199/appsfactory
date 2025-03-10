package com.appsfactory.helpers;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScenarioContext {
    private final Map<String, String> contextData = new ConcurrentHashMap<>();

    /**
     * Stores a key-value pair in the context.
     *
     * @param key The key under which the value is stored.
     */
    public void setValue(String key, String value) {
        contextData.put(key, value);
    }

    /**
     * Retrieves a value from the context.
     * @param key The key whose value needs to be retrieved.
     * @return The stored value, or null if not found.
     */
    public String getValue(String key) {
        return contextData.get(key);
    }

    /**
     * Clears the stored data (useful for scenario cleanup).
     */
    public void clear() {
        contextData.clear();
    }
}