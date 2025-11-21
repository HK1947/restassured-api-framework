package com.api.tests;

import com.api.client.ApiClient;
import com.api.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

/**
 * BaseApiTest - Abstract base class for all API test classes.
 *
 * Design Decisions:
 * 1. Template Method Pattern - Common setup/teardown for all tests
 * 2. RestAssured Configuration - Centralized base settings
 * 3. Allure Integration - Automatic request/response logging
 * 4. Reusable ApiClient - Available for all tests
 *
 * Why extend this class?
 * - Consistent RestAssured configuration
 * - Automatic logging and reporting
 * - Access to ApiClient and ConfigManager
 * - Clean test code
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public abstract class BaseApiTest {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected ConfigManager config;
    protected ApiClient apiClient;

    /**
     * Suite-level setup - Configure RestAssured defaults.
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("========== API TEST SUITE STARTED ==========");

        // Initialize configuration
        config = ConfigManager.getInstance();

        // Configure RestAssured defaults
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

        // Add Allure filter globally
        RestAssured.filters(new AllureRestAssured());

        // Add logging filters if enabled
        if (config.isLoggingEnabled()) {
            RestAssured.filters(
                    new RequestLoggingFilter(LogDetail.ALL),
                    new ResponseLoggingFilter(LogDetail.ALL)
            );
        }

        logger.info("RestAssured configured - Base URL: {}", config.getBaseUrl());
    }

    /**
     * Test-level setup - Initialize ApiClient for each test.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("---------- Test Setup Started ----------");

        // Create fresh ApiClient for each test
        apiClient = new ApiClient();

        logger.info("---------- Test Setup Completed ----------");
    }

    /**
     * Test-level teardown.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("---------- Test Teardown Completed ----------");
    }

    /**
     * Suite-level teardown.
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        // Reset RestAssured
        RestAssured.reset();
        logger.info("========== API TEST SUITE COMPLETED ==========");
    }

    // ==================== HELPER METHODS ====================

    /**
     * Get ApiClient with Bearer token authentication.
     *
     * @param token Bearer token
     * @return Authenticated ApiClient
     */
    protected ApiClient getAuthenticatedClient(String token) {
        return ApiClient.withBearerToken(token);
    }

    /**
     * Get configuration value.
     *
     * @param key Config key
     * @return Config value
     */
    protected String getConfig(String key) {
        return config.get(key);
    }

    /**
     * Get configuration value with default.
     *
     * @param key Config key
     * @param defaultValue Default value
     * @return Config value or default
     */
    protected String getConfig(String key, String defaultValue) {
        return config.get(key, defaultValue);
    }
}
