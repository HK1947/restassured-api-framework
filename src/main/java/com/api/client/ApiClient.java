package com.api.client;

import com.api.specs.RequestSpecs;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * ApiClient - Reusable API client encapsulating REST operations.
 *
 * Design Decisions:
 * 1. Encapsulation - Hides Rest Assured complexity from tests
 * 2. Fluent Interface - Method chaining for clean test code
 * 3. Overloaded Methods - Support various parameter types
 * 4. Logging - Built-in request/response logging
 *
 * Why use an ApiClient?
 * - Single place to modify HTTP logic
 * - Consistent error handling
 * - Easy to add retry/circuit breaker logic
 * - Clean separation from tests
 *
 * Usage:
 *   ApiClient client = new ApiClient();
 *   Response response = client.get("/users/1");
 *   Response response = client.post("/users", userObject);
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public class ApiClient {

    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private RequestSpecification requestSpec;

    /**
     * Default constructor using base request spec.
     */
    public ApiClient() {
        this.requestSpec = RequestSpecs.getBaseSpec();
    }

    /**
     * Constructor with custom request specification.
     *
     * @param requestSpec Custom request specification
     */
    public ApiClient(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    /**
     * Set request specification.
     *
     * @param requestSpec Request specification
     * @return ApiClient for method chaining
     */
    public ApiClient withSpec(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
        return this;
    }

    // ==================== GET METHODS ====================

    /**
     * Perform GET request.
     *
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response get(String endpoint) {
        logger.info("GET request to: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform GET request with path parameters.
     *
     * @param endpoint API endpoint with path param placeholders
     * @param pathParams Path parameters map
     * @return Response object
     */
    public Response get(String endpoint, Map<String, ?> pathParams) {
        logger.info("GET request to: {} with pathParams: {}", endpoint, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform GET request with query parameters.
     *
     * @param endpoint API endpoint
     * @param queryParams Query parameters map
     * @return Response object
     */
    public Response getWithQueryParams(String endpoint, Map<String, ?> queryParams) {
        logger.info("GET request to: {} with queryParams: {}", endpoint, queryParams);
        return given()
                .spec(requestSpec)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    // ==================== POST METHODS ====================

    /**
     * Perform POST request with body.
     *
     * @param endpoint API endpoint
     * @param body Request body (will be serialized to JSON)
     * @return Response object
     */
    public Response post(String endpoint, Object body) {
        logger.info("POST request to: {}", endpoint);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform POST request without body.
     *
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response post(String endpoint) {
        logger.info("POST request to: {} (no body)", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform POST request with body and path parameters.
     *
     * @param endpoint API endpoint
     * @param body Request body
     * @param pathParams Path parameters
     * @return Response object
     */
    public Response post(String endpoint, Object body, Map<String, ?> pathParams) {
        logger.info("POST request to: {} with pathParams: {}", endpoint, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    // ==================== PUT METHODS ====================

    /**
     * Perform PUT request with body.
     *
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public Response put(String endpoint, Object body) {
        logger.info("PUT request to: {}", endpoint);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform PUT request with path parameters.
     *
     * @param endpoint API endpoint
     * @param body Request body
     * @param pathParams Path parameters
     * @return Response object
     */
    public Response put(String endpoint, Object body, Map<String, ?> pathParams) {
        logger.info("PUT request to: {} with pathParams: {}", endpoint, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    // ==================== PATCH METHODS ====================

    /**
     * Perform PATCH request with body.
     *
     * @param endpoint API endpoint
     * @param body Request body (partial update)
     * @return Response object
     */
    public Response patch(String endpoint, Object body) {
        logger.info("PATCH request to: {}", endpoint);
        return given()
                .spec(requestSpec)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform PATCH request with path parameters.
     *
     * @param endpoint API endpoint
     * @param body Request body
     * @param pathParams Path parameters
     * @return Response object
     */
    public Response patch(String endpoint, Object body, Map<String, ?> pathParams) {
        logger.info("PATCH request to: {} with pathParams: {}", endpoint, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    // ==================== DELETE METHODS ====================

    /**
     * Perform DELETE request.
     *
     * @param endpoint API endpoint
     * @return Response object
     */
    public Response delete(String endpoint) {
        logger.info("DELETE request to: {}", endpoint);
        return given()
                .spec(requestSpec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    /**
     * Perform DELETE request with path parameters.
     *
     * @param endpoint API endpoint
     * @param pathParams Path parameters
     * @return Response object
     */
    public Response delete(String endpoint, Map<String, ?> pathParams) {
        logger.info("DELETE request to: {} with pathParams: {}", endpoint, pathParams);
        return given()
                .spec(requestSpec)
                .pathParams(pathParams)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Add header to current request.
     *
     * @param name Header name
     * @param value Header value
     * @return ApiClient for chaining
     */
    public ApiClient withHeader(String name, String value) {
        this.requestSpec = given()
                .spec(requestSpec)
                .header(name, value);
        return this;
    }

    /**
     * Add multiple headers to current request.
     *
     * @param headers Headers map
     * @return ApiClient for chaining
     */
    public ApiClient withHeaders(Map<String, String> headers) {
        this.requestSpec = given()
                .spec(requestSpec)
                .headers(headers);
        return this;
    }

    /**
     * Add query parameter to current request.
     *
     * @param name Parameter name
     * @param value Parameter value
     * @return ApiClient for chaining
     */
    public ApiClient withQueryParam(String name, Object value) {
        this.requestSpec = given()
                .spec(requestSpec)
                .queryParam(name, value);
        return this;
    }

    /**
     * Create new ApiClient with Bearer token authentication.
     *
     * @param token Bearer token
     * @return New ApiClient instance with auth
     */
    public static ApiClient withBearerToken(String token) {
        return new ApiClient(RequestSpecs.getAuthSpec(token));
    }

    /**
     * Create new ApiClient with Basic authentication.
     *
     * @param username Username
     * @param password Password
     * @return New ApiClient instance with basic auth
     */
    public static ApiClient withBasicAuth(String username, String password) {
        return new ApiClient(RequestSpecs.getBasicAuthSpec(username, password));
    }
}
