package com.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConfigManager - Thread-safe singleton for API configuration management.
 *
 * Design Decisions:
 * 1. Singleton Pattern - Single source of truth for configuration
 * 2. Environment-based - Supports dev/qa/prod environments
 * 3. YAML format - Human-readable, supports nested configs
 * 4. Thread-safe - Uses ConcurrentHashMap for parallel execution
 *
 * Usage:
 *   ConfigManager.getInstance().getBaseUrl()
 *   ConfigManager.getInstance().get("api.key")
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static volatile ConfigManager instance;

    private final Map<String, Object> configCache = new ConcurrentHashMap<>();
    private final String environment;

    private ConfigManager() {
        this.environment = Optional.ofNullable(System.getProperty("env"))
                .orElse(Optional.ofNullable(System.getenv("ENV"))
                .orElse("dev"));

        loadConfiguration();
        logger.info("ConfigManager initialized for environment: {}", environment);
    }

    /**
     * Get singleton instance with double-checked locking.
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    /**
     * Load configuration from YAML files.
     */
    @SuppressWarnings("unchecked")
    private void loadConfiguration() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        // Load default config
        loadConfigFile(mapper, "config/api-config.yaml");

        // Load environment-specific config (overrides)
        String envConfigPath = String.format("config/api-config-%s.yaml", environment);
        loadConfigFile(mapper, envConfigPath);

        logger.debug("Configuration loaded: {}", configCache.keySet());
    }

    @SuppressWarnings("unchecked")
    private void loadConfigFile(ObjectMapper mapper, String resourcePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                Map<String, Object> config = mapper.readValue(inputStream, Map.class);
                flattenAndCache(config, "");
                logger.info("Loaded config: {}", resourcePath);
            } else {
                logger.warn("Config file not found: {}", resourcePath);
            }
        } catch (IOException e) {
            logger.error("Failed to load config: {}", resourcePath, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void flattenAndCache(Map<String, Object> map, String prefix) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                flattenAndCache((Map<String, Object>) value, key);
            } else {
                configCache.put(key, value);
            }
        }
    }

    // ==================== GETTERS ====================

    /**
     * Get configuration value as String.
     */
    public String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        Object value = configCache.get(key);
        return value != null ? String.valueOf(value) : null;
    }

    /**
     * Get configuration value with default.
     */
    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get integer configuration value.
     */
    public int getInt(String key, int defaultValue) {
        String value = get(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    /**
     * Get boolean configuration value.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    // ==================== API-SPECIFIC GETTERS ====================

    /**
     * Get API base URL.
     */
    public String getBaseUrl() {
        return get("api.baseUrl", "https://reqres.in/api");
    }

    /**
     * Get API timeout in milliseconds.
     */
    public int getTimeout() {
        return getInt("api.timeout", 30000);
    }

    /**
     * Get API key (if required).
     */
    public String getApiKey() {
        return get("api.key");
    }

    /**
     * Get authentication token.
     */
    public String getAuthToken() {
        return get("api.authToken");
    }

    /**
     * Get current environment.
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Check if logging is enabled.
     */
    public boolean isLoggingEnabled() {
        return getBoolean("api.logging.enabled", true);
    }

    /**
     * Reload configuration.
     */
    public void reload() {
        configCache.clear();
        loadConfiguration();
        logger.info("Configuration reloaded");
    }
}
