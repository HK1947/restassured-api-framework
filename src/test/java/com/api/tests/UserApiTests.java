package com.api.tests;

import com.api.models.User;
import com.api.models.UserResponse;
import com.api.specs.ResponseSpecs;
import com.api.utils.SchemaValidator;
import com.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserApiTests - CRUD tests for User API endpoints.
 *
 * Design Decisions:
 * 1. AAA Pattern - Arrange, Act, Assert structure
 * 2. Allure Annotations - Rich reporting metadata
 * 3. POJO Assertions - Type-safe response validation
 * 4. Schema Validation - Contract testing
 *
 * API Under Test: https://reqres.in/api
 *
 * @author Harsha Kumar
 * @version 1.0
 */
@Epic("User Management API")
@Feature("User CRUD Operations")
public class UserApiTests extends BaseApiTest {

    // ==================== GET TESTS ====================

    @Test(description = "Get list of users", groups = {"smoke", "regression"})
    @Story("Get Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users returns paginated list of users")
    public void testGetUsers() {
        // Arrange
        String endpoint = "/users?page=1";

        // Act
        Response response = apiClient.get(endpoint);

        // Assert - Status and Content Type
        response.then()
                .spec(ResponseSpecs.success200());

        // Assert - Deserialize and validate
        UserResponse userResponse = response.as(UserResponse.class);

        assertThat(userResponse.getPage()).isEqualTo(1);
        assertThat(userResponse.getData()).isNotEmpty();
        assertThat(userResponse.getPerPage()).isGreaterThan(0);
        assertThat(userResponse.getTotal()).isGreaterThan(0);

        logger.info("Retrieved {} users from page {}", userResponse.getUserCount(), userResponse.getPage());
    }

    @Test(description = "Get single user by ID", groups = {"smoke", "regression"})
    @Story("Get Users")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify GET /users/{id} returns user details")
    public void testGetSingleUser() {
        // Arrange
        int userId = 2;
        String endpoint = "/users/" + userId;

        // Act
        Response response = apiClient.get(endpoint);

        // Assert - Status
        response.then().spec(ResponseSpecs.success200());

        // Assert - Response body
        User user = response.jsonPath().getObject("data", User.class);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getEmail()).isNotEmpty();
        assertThat(user.getFirstName()).isNotEmpty();
        assertThat(user.getLastName()).isNotEmpty();

        logger.info("Retrieved user: {} {}", user.getFirstName(), user.getLastName());
    }

    @Test(description = "Get non-existent user returns 404", groups = {"regression", "negative"})
    @Story("Get Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users/{id} returns 404 for non-existent user")
    public void testGetNonExistentUser() {
        // Arrange
        int nonExistentId = 99999;
        String endpoint = "/users/" + nonExistentId;

        // Act
        Response response = apiClient.get(endpoint);

        // Assert
        response.then().spec(ResponseSpecs.notFound404());

        logger.info("Correctly received 404 for non-existent user ID: {}", nonExistentId);
    }

    // ==================== POST TESTS ====================

    @Test(description = "Create new user", groups = {"smoke", "regression"})
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /users creates a new user")
    public void testCreateUser() {
        // Arrange
        User newUser = TestDataGenerator.generateUser();
        String endpoint = "/users";

        // Act
        Response response = apiClient.post(endpoint, newUser);

        // Assert - Status
        response.then().spec(ResponseSpecs.created201());

        // Assert - Response body
        User createdUser = response.as(User.class);

        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(newUser.getName());
        assertThat(createdUser.getJob()).isEqualTo(newUser.getJob());
        assertThat(createdUser.getCreatedAt()).isNotNull();

        logger.info("Created user with ID: {}, Name: {}", createdUser.getId(), createdUser.getName());
    }

    @Test(description = "Create user with specific data", groups = {"regression"})
    @Story("Create User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify POST /users creates user with provided name and job")
    public void testCreateUserWithSpecificData() {
        // Arrange
        String name = "Harsha Kumar";
        String job = "SDET Engineer";
        User newUser = User.createUser(name, job);
        String endpoint = "/users";

        // Act
        Response response = apiClient.post(endpoint, newUser);

        // Assert
        response.then().spec(ResponseSpecs.created201());

        User createdUser = response.as(User.class);

        assertThat(createdUser.getName()).isEqualTo(name);
        assertThat(createdUser.getJob()).isEqualTo(job);

        logger.info("Created user: {} - {}", name, job);
    }

    // ==================== PUT TESTS ====================

    @Test(description = "Update user completely", groups = {"regression"})
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PUT /users/{id} updates user details")
    public void testUpdateUser() {
        // Arrange
        int userId = 2;
        User updatedUser = User.builder()
                .name("Updated Name")
                .job("Updated Job")
                .build();
        String endpoint = "/users/" + userId;

        // Act
        Response response = apiClient.put(endpoint, updatedUser);

        // Assert
        response.then().spec(ResponseSpecs.success200());

        User result = response.as(User.class);

        assertThat(result.getName()).isEqualTo(updatedUser.getName());
        assertThat(result.getJob()).isEqualTo(updatedUser.getJob());
        assertThat(result.getUpdatedAt()).isNotNull();

        logger.info("Updated user {}: {} - {}", userId, result.getName(), result.getJob());
    }

    // ==================== PATCH TESTS ====================

    @Test(description = "Partially update user", groups = {"regression"})
    @Story("Update User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify PATCH /users/{id} partially updates user")
    public void testPatchUser() {
        // Arrange
        int userId = 2;
        User patchData = User.builder()
                .job("Senior SDET")  // Only updating job
                .build();
        String endpoint = "/users/" + userId;

        // Act
        Response response = apiClient.patch(endpoint, patchData);

        // Assert
        response.then().spec(ResponseSpecs.success200());

        User result = response.as(User.class);

        assertThat(result.getJob()).isEqualTo(patchData.getJob());
        assertThat(result.getUpdatedAt()).isNotNull();

        logger.info("Patched user {} job to: {}", userId, result.getJob());
    }

    // ==================== DELETE TESTS ====================

    @Test(description = "Delete user", groups = {"regression"})
    @Story("Delete User")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify DELETE /users/{id} deletes user")
    public void testDeleteUser() {
        // Arrange
        int userId = 2;
        String endpoint = "/users/" + userId;

        // Act
        Response response = apiClient.delete(endpoint);

        // Assert - 204 No Content for successful delete
        response.then().spec(ResponseSpecs.noContent204());

        logger.info("Deleted user with ID: {}", userId);
    }

    // ==================== SCHEMA VALIDATION TESTS ====================

    @Test(description = "Validate user list response schema", groups = {"regression", "schema"})
    @Story("Schema Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify GET /users response matches JSON schema")
    public void testUsersResponseSchema() {
        // Arrange
        String endpoint = "/users?page=1";

        // Act
        Response response = apiClient.get(endpoint);

        // Assert - Status first
        response.then().spec(ResponseSpecs.success200());

        // Assert - Schema validation
        SchemaValidator.validateSchema(response, "users-list-schema.json");

        logger.info("Users list response schema validated successfully");
    }

    // ==================== PAGINATION TESTS ====================

    @Test(description = "Test pagination", groups = {"regression"})
    @Story("Get Users")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify pagination works correctly")
    public void testPagination() {
        // Act - Get page 1
        Response page1Response = apiClient.get("/users?page=1");
        UserResponse page1 = page1Response.as(UserResponse.class);

        // Act - Get page 2
        Response page2Response = apiClient.get("/users?page=2");
        UserResponse page2 = page2Response.as(UserResponse.class);

        // Assert
        assertThat(page1.getPage()).isEqualTo(1);
        assertThat(page2.getPage()).isEqualTo(2);
        assertThat(page1.getData()).isNotEqualTo(page2.getData());
        assertThat(page1.getTotalPages()).isEqualTo(page2.getTotalPages());

        logger.info("Pagination verified: Page 1 has {} users, Page 2 has {} users",
                page1.getUserCount(), page2.getUserCount());
    }
}
