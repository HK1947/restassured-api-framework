package com.api.specs;

import com.api.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * RequestSpecs - Factory for creating reusable request specifications.
 *
 * Design Decisions:
 * 1. Builder Pattern - Fluent API for spec creation
 * 2. Centralized Configuration - All specs share common config
 * 3. Separation of Concerns - Different specs for different auth types
 * 4. Allure Integration - Built-in reporting support
 *
 * Why use RequestSpecification?
 * - Reduces code duplication
 * - Consistent headers/auth across requests
 * - Easy to maintain base configuration
 * - Cleaner test code
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public final class RequestSpecs {

    private static final Logger logger = LogManager.getLogger(RequestSpecs.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    private RequestSpecs() {
        throw new UnsupportedOperationException("RequestSpecs is a utility class");
    }

    /**
     * Get base request specification.
     * Includes: base URL, content type, logging, Allure filter.
     */
    public static RequestSpecification getBaseSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured());

        // Add logging if enabled
        if (config.isLoggingEnabled()) {
            builder.log(LogDetail.ALL);
        }

        logger.debug("Created base request spec for: {}", config.getBaseUrl());
        return builder.build();
    }

    /**
     * Get request specification with Bearer token authentication.
     *
     * @param token Bearer token
     * @return RequestSpecification with auth header
     */
    public static RequestSpecification getAuthSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    /**
     * Get request specification with Basic authentication.
     *
     * @param username Username
     * @param password Password
     * @return RequestSpecification with basic auth
     */
    public static RequestSpecification getBasicAuthSpec(String username, String password) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .setAuth(io.restassured.RestAssured.preemptive().basic(username, password))
                .build();
    }

    /**
     * Get request specification with API key.
     *
     * @param apiKey API key
     * @param headerName Header name for API key (e.g., "X-API-Key")
     * @return RequestSpecification with API key header
     */
    public static RequestSpecification getApiKeySpec(String apiKey, String headerName) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec())
                .addHeader(headerName, apiKey)
                .build();
    }

    /**
     * Get request specification for form data.
     */
    public static RequestSpecification getFormDataSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.URLENC)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Get request specification for multipart/file upload.
     */
    public static RequestSpecification getMultipartSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.MULTIPART)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Get request specification with custom headers.
     *
     * @param headers Map of header name-value pairs
     * @return RequestSpecification with custom headers
     */
    public static RequestSpecification withHeaders(java.util.Map<String, String> headers) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec());

        headers.forEach(builder::addHeader);
        return builder.build();
    }

    /**
     * Get request specification with query parameters.
     *
     * @param params Map of query parameter name-value pairs
     * @return RequestSpecification with query params
     */
    public static RequestSpecification withQueryParams(java.util.Map<String, String> params) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpec());

        params.forEach(builder::addQueryParam);
        return builder.build();
    }

    /**
     * Get request specification without logging (for sensitive data).
     */
    public static RequestSpecification getNoLogSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
