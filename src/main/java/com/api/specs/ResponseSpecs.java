package com.api.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

/**
 * ResponseSpecs - Factory for creating reusable response specifications.
 *
 * Design Decisions:
 * 1. Predefined specs - Common response validations
 * 2. Reusable assertions - Status codes, content types, timing
 * 3. Composable - Can be combined for complex validations
 * 4. Fluent API - Easy to read and maintain
 *
 * Why use ResponseSpecification?
 * - Consistent response validation
 * - Reduces assertion code duplication
 * - Easy to add common checks (timing, content type)
 * - Cleaner test code
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public final class ResponseSpecs {

    private static final Logger logger = LogManager.getLogger(ResponseSpecs.class);

    // Default timeout for response time validation (milliseconds)
    private static final long DEFAULT_TIMEOUT_MS = 5000;

    private ResponseSpecs() {
        throw new UnsupportedOperationException("ResponseSpecs is a utility class");
    }

    // ==================== STATUS CODE SPECS ====================

    /**
     * Response spec for successful GET (200 OK).
     */
    public static ResponseSpecification success200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for successful POST/PUT (201 Created).
     */
    public static ResponseSpecification created201() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for successful DELETE (204 No Content).
     */
    public static ResponseSpecification noContent204() {
        return new ResponseSpecBuilder()
                .expectStatusCode(204)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for bad request (400).
     */
    public static ResponseSpecification badRequest400() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for unauthorized (401).
     */
    public static ResponseSpecification unauthorized401() {
        return new ResponseSpecBuilder()
                .expectStatusCode(401)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for forbidden (403).
     */
    public static ResponseSpecification forbidden403() {
        return new ResponseSpecBuilder()
                .expectStatusCode(403)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for not found (404).
     */
    public static ResponseSpecification notFound404() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for conflict (409).
     */
    public static ResponseSpecification conflict409() {
        return new ResponseSpecBuilder()
                .expectStatusCode(409)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for server error (500).
     */
    public static ResponseSpecification serverError500() {
        return new ResponseSpecBuilder()
                .expectStatusCode(500)
                .log(LogDetail.ALL)
                .build();
    }

    // ==================== CUSTOM STATUS CODE SPEC ====================

    /**
     * Response spec for custom status code.
     *
     * @param statusCode Expected HTTP status code
     * @return ResponseSpecification
     */
    public static ResponseSpecification expectStatus(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(LogDetail.ALL)
                .build();
    }

    // ==================== CONTENT TYPE SPECS ====================

    /**
     * Response spec expecting JSON content type.
     */
    public static ResponseSpecification jsonResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec expecting XML content type.
     */
    public static ResponseSpecification xmlResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.XML)
                .log(LogDetail.ALL)
                .build();
    }

    // ==================== TIMING SPECS ====================

    /**
     * Response spec with default timeout validation.
     */
    public static ResponseSpecification withDefaultTimeout() {
        return new ResponseSpecBuilder()
                .expectResponseTime(lessThan(DEFAULT_TIMEOUT_MS), TimeUnit.MILLISECONDS)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec with custom timeout validation.
     *
     * @param timeoutMs Maximum response time in milliseconds
     * @return ResponseSpecification
     */
    public static ResponseSpecification withTimeout(long timeoutMs) {
        return new ResponseSpecBuilder()
                .expectResponseTime(lessThan(timeoutMs), TimeUnit.MILLISECONDS)
                .log(LogDetail.ALL)
                .build();
    }

    // ==================== COMBINED SPECS ====================

    /**
     * Response spec for successful GET with timeout.
     */
    public static ResponseSpecification success200WithTimeout() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(DEFAULT_TIMEOUT_MS), TimeUnit.MILLISECONDS)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec for successful POST with timeout.
     */
    public static ResponseSpecification created201WithTimeout() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(DEFAULT_TIMEOUT_MS), TimeUnit.MILLISECONDS)
                .log(LogDetail.ALL)
                .build();
    }

    // ==================== HEADER VALIDATION SPECS ====================

    /**
     * Response spec expecting specific header.
     *
     * @param headerName Header name
     * @param headerValue Expected header value
     * @return ResponseSpecification
     */
    public static ResponseSpecification withHeader(String headerName, String headerValue) {
        return new ResponseSpecBuilder()
                .expectHeader(headerName, headerValue)
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec expecting header to exist.
     *
     * @param headerName Header name
     * @return ResponseSpecification
     */
    public static ResponseSpecification hasHeader(String headerName) {
        return new ResponseSpecBuilder()
                .expectHeader(headerName, notNullValue())
                .log(LogDetail.ALL)
                .build();
    }

    // ==================== BODY VALIDATION SPECS ====================

    /**
     * Response spec expecting non-empty body.
     */
    public static ResponseSpecification hasBody() {
        return new ResponseSpecBuilder()
                .expectBody(notNullValue())
                .log(LogDetail.ALL)
                .build();
    }

    /**
     * Response spec expecting specific field in response.
     *
     * @param jsonPath JSON path to field
     * @return ResponseSpecification
     */
    public static ResponseSpecification hasField(String jsonPath) {
        return new ResponseSpecBuilder()
                .expectBody(jsonPath, notNullValue())
                .log(LogDetail.ALL)
                .build();
    }
}
