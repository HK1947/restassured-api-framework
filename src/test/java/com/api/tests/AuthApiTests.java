package com.api.tests;

import com.api.models.LoginRequest;
import com.api.models.LoginResponse;
import com.api.specs.ResponseSpecs;
import com.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AuthApiTests - Authentication API tests.
 *
 * Tests login and registration endpoints using reqres.in API.
 *
 * @author Harsha Kumar
 * @version 1.0
 */
@Epic("Authentication API")
@Feature("User Authentication")
public class AuthApiTests extends BaseApiTest {

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String REGISTER_ENDPOINT = "/register";

    // ==================== LOGIN TESTS ====================

    @Test(description = "Successful login with valid credentials", groups = {"smoke", "regression"})
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify POST /login returns token for valid credentials")
    public void testSuccessfulLogin() {
        // Arrange
        LoginRequest loginRequest = TestDataGenerator.generateValidLogin();

        // Act
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);

        // Assert
        response.then().spec(ResponseSpecs.success200());

        LoginResponse loginResponse = response.as(LoginResponse.class);

        assertThat(loginResponse.isSuccess()).isTrue();
        assertThat(loginResponse.getToken()).isNotEmpty();
        assertThat(loginResponse.getError()).isNull();

        logger.info("Login successful, token received: {}", loginResponse.getToken());
    }

    @Test(description = "Login fails without password", groups = {"regression", "negative"})
    @Story("User Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /login returns error when password is missing")
    public void testLoginWithoutPassword() {
        // Arrange
        LoginRequest loginRequest = TestDataGenerator.generateLoginWithoutPassword();

        // Act
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);

        // Assert
        response.then().spec(ResponseSpecs.badRequest400());

        LoginResponse loginResponse = response.as(LoginResponse.class);

        assertThat(loginResponse.isFailed()).isTrue();
        assertThat(loginResponse.getError()).isEqualTo("Missing password");

        logger.info("Login correctly rejected without password");
    }

    @Test(description = "Login fails with invalid email", groups = {"regression", "negative"})
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify POST /login returns error for non-registered email")
    public void testLoginWithInvalidEmail() {
        // Arrange
        LoginRequest loginRequest = TestDataGenerator.generateInvalidLogin();

        // Act
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);

        // Assert
        response.then().spec(ResponseSpecs.badRequest400());

        LoginResponse loginResponse = response.as(LoginResponse.class);

        assertThat(loginResponse.isFailed()).isTrue();
        assertThat(loginResponse.getError()).contains("user not found");

        logger.info("Login correctly rejected for invalid email");
    }

    @Test(description = "Login fails with empty body", groups = {"regression", "negative"})
    @Story("User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify POST /login returns error for empty request body")
    public void testLoginWithEmptyBody() {
        // Arrange
        LoginRequest emptyRequest = new LoginRequest();

        // Act
        Response response = apiClient.post(LOGIN_ENDPOINT, emptyRequest);

        // Assert
        response.then().spec(ResponseSpecs.badRequest400());

        logger.info("Login correctly rejected for empty body");
    }

    // ==================== REGISTRATION TESTS ====================

    @Test(description = "Successful registration", groups = {"smoke", "regression"})
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /register creates new user and returns token")
    public void testSuccessfulRegistration() {
        // Arrange - reqres.in only accepts predefined emails
        LoginRequest registerRequest = LoginRequest.of("eve.holt@reqres.in", "pistol");

        // Act
        Response response = apiClient.post(REGISTER_ENDPOINT, registerRequest);

        // Assert
        response.then().spec(ResponseSpecs.success200());

        // Verify response has id and token
        assertThat(response.jsonPath().getInt("id")).isGreaterThan(0);
        assertThat(response.jsonPath().getString("token")).isNotEmpty();

        logger.info("Registration successful, ID: {}", response.jsonPath().getInt("id"));
    }

    @Test(description = "Registration fails without password", groups = {"regression", "negative"})
    @Story("User Registration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify POST /register returns error when password is missing")
    public void testRegistrationWithoutPassword() {
        // Arrange
        LoginRequest registerRequest = LoginRequest.builder()
                .email("eve.holt@reqres.in")
                .build();

        // Act
        Response response = apiClient.post(REGISTER_ENDPOINT, registerRequest);

        // Assert
        response.then().spec(ResponseSpecs.badRequest400());

        String error = response.jsonPath().getString("error");
        assertThat(error).isEqualTo("Missing password");

        logger.info("Registration correctly rejected without password");
    }

    @Test(description = "Registration fails without email", groups = {"regression", "negative"})
    @Story("User Registration")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify POST /register returns error when email is missing")
    public void testRegistrationWithoutEmail() {
        // Arrange
        LoginRequest registerRequest = LoginRequest.builder()
                .password("somepassword")
                .build();

        // Act
        Response response = apiClient.post(REGISTER_ENDPOINT, registerRequest);

        // Assert
        response.then().spec(ResponseSpecs.badRequest400());

        String error = response.jsonPath().getString("error");
        assertThat(error).isEqualTo("Missing email or username");

        logger.info("Registration correctly rejected without email");
    }

    // ==================== TOKEN VALIDATION TESTS ====================

    @Test(description = "Verify token format", groups = {"regression"})
    @Story("User Login")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify login token has expected format")
    public void testTokenFormat() {
        // Arrange
        LoginRequest loginRequest = TestDataGenerator.generateValidLogin();

        // Act
        Response response = apiClient.post(LOGIN_ENDPOINT, loginRequest);

        // Assert
        response.then().spec(ResponseSpecs.success200());

        String token = response.jsonPath().getString("token");

        // Token should be alphanumeric (reqres.in format)
        assertThat(token).matches("[a-zA-Z0-9]+");
        assertThat(token.length()).isGreaterThan(10);

        logger.info("Token format validated: {} characters", token.length());
    }
}
