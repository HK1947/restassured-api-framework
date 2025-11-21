package com.api.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;

import java.io.InputStream;

/**
 * SchemaValidator - Utility for JSON schema validation.
 *
 * Design Decisions:
 * 1. Static Methods - Utility pattern, no state needed
 * 2. Resource Loading - Schemas loaded from classpath
 * 3. Hamcrest Integration - Works with Rest Assured assertions
 * 4. Flexible Paths - Support both classpath and absolute paths
 *
 * Why Schema Validation?
 * - Validates response structure, not just values
 * - Catches breaking API changes early
 * - Documents expected response format
 * - Contract testing support
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public final class SchemaValidator {

    private static final Logger logger = LogManager.getLogger(SchemaValidator.class);
    private static final String SCHEMA_BASE_PATH = "schemas/";

    private SchemaValidator() {
        throw new UnsupportedOperationException("SchemaValidator is a utility class");
    }

    /**
     * Get schema matcher from classpath resource.
     *
     * @param schemaFileName Schema file name (without path)
     * @return JsonSchemaValidator matcher
     */
    public static Matcher<?> matchesSchema(String schemaFileName) {
        String schemaPath = SCHEMA_BASE_PATH + schemaFileName;
        logger.info("Loading schema: {}", schemaPath);
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath);
    }

    /**
     * Validate response against schema file.
     *
     * @param response Response to validate
     * @param schemaFileName Schema file name
     * @throws AssertionError if validation fails
     */
    public static void validateSchema(Response response, String schemaFileName) {
        logger.info("Validating response against schema: {}", schemaFileName);
        response.then().assertThat().body(matchesSchema(schemaFileName));
        logger.info("Schema validation passed");
    }

    /**
     * Validate JSON string against schema.
     *
     * @param jsonBody JSON string to validate
     * @param schemaFileName Schema file name
     * @return true if valid
     */
    public static boolean isValidSchema(String jsonBody, String schemaFileName) {
        try {
            io.restassured.RestAssured.given()
                    .body(jsonBody)
                    .when()
                    .then()
                    .assertThat()
                    .body(matchesSchema(schemaFileName));
            return true;
        } catch (AssertionError e) {
            logger.warn("Schema validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get schema from input stream.
     *
     * @param schemaFileName Schema file name
     * @return InputStream of schema file
     */
    public static InputStream getSchemaAsStream(String schemaFileName) {
        String schemaPath = SCHEMA_BASE_PATH + schemaFileName;
        return SchemaValidator.class.getClassLoader().getResourceAsStream(schemaPath);
    }

    /**
     * Check if schema file exists.
     *
     * @param schemaFileName Schema file name
     * @return true if schema exists
     */
    public static boolean schemaExists(String schemaFileName) {
        return getSchemaAsStream(schemaFileName) != null;
    }
}
