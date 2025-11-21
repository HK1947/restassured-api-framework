package com.api.utils;

import com.api.models.LoginRequest;
import com.api.models.User;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.UUID;

/**
 * TestDataGenerator - Utility for generating random test data.
 *
 * Design Decisions:
 * 1. JavaFaker - Industry-standard test data generation
 * 2. Static Methods - Easy to use without instantiation
 * 3. Locale Support - Can generate data for different regions
 * 4. Reproducible - Optional seed support for debugging
 *
 * Why use test data generators?
 * - Unique data for each test run
 * - Avoids hardcoded test data
 * - Realistic-looking data
 * - Reduces test data conflicts
 *
 * @author Harsha Kumar
 * @version 1.0
 */
public final class TestDataGenerator {

    private static final Logger logger = LogManager.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker(Locale.US);

    private TestDataGenerator() {
        throw new UnsupportedOperationException("TestDataGenerator is a utility class");
    }

    // ==================== USER DATA ====================

    /**
     * Generate random user for creation.
     */
    public static User generateUser() {
        return User.builder()
                .name(faker.name().fullName())
                .job(faker.job().title())
                .build();
    }

    /**
     * Generate user with specific details.
     */
    public static User generateUser(String name, String job) {
        return User.builder()
                .name(name)
                .job(job)
                .build();
    }

    /**
     * Generate full user with all fields.
     */
    public static User generateFullUser() {
        return User.builder()
                .email(generateEmail())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .name(faker.name().fullName())
                .job(faker.job().title())
                .build();
    }

    // ==================== LOGIN DATA ====================

    /**
     * Generate valid login request (for reqres.in).
     * Uses known working credentials.
     */
    public static LoginRequest generateValidLogin() {
        return LoginRequest.of("eve.holt@reqres.in", "cityslicka");
    }

    /**
     * Generate invalid login request.
     */
    public static LoginRequest generateInvalidLogin() {
        return LoginRequest.of(generateEmail(), generatePassword());
    }

    /**
     * Generate login with missing password.
     */
    public static LoginRequest generateLoginWithoutPassword() {
        return LoginRequest.builder()
                .email("eve.holt@reqres.in")
                .build();
    }

    // ==================== PRIMITIVE DATA ====================

    /**
     * Generate random email.
     */
    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Generate unique email using UUID.
     */
    public static String generateUniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    }

    /**
     * Generate random password.
     */
    public static String generatePassword() {
        return faker.internet().password(8, 20, true, true, true);
    }

    /**
     * Generate random first name.
     */
    public static String generateFirstName() {
        return faker.name().firstName();
    }

    /**
     * Generate random last name.
     */
    public static String generateLastName() {
        return faker.name().lastName();
    }

    /**
     * Generate random full name.
     */
    public static String generateFullName() {
        return faker.name().fullName();
    }

    /**
     * Generate random phone number.
     */
    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    /**
     * Generate random job title.
     */
    public static String generateJobTitle() {
        return faker.job().title();
    }

    /**
     * Generate random company name.
     */
    public static String generateCompanyName() {
        return faker.company().name();
    }

    // ==================== ADDRESS DATA ====================

    /**
     * Generate random street address.
     */
    public static String generateStreetAddress() {
        return faker.address().streetAddress();
    }

    /**
     * Generate random city.
     */
    public static String generateCity() {
        return faker.address().city();
    }

    /**
     * Generate random country.
     */
    public static String generateCountry() {
        return faker.address().country();
    }

    /**
     * Generate random zip code.
     */
    public static String generateZipCode() {
        return faker.address().zipCode();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Generate random integer in range.
     */
    public static int generateInt(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    /**
     * Generate random long in range.
     */
    public static long generateLong(long min, long max) {
        return faker.number().numberBetween(min, max);
    }

    /**
     * Generate random UUID string.
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate random alphanumeric string.
     */
    public static String generateAlphanumeric(int length) {
        return faker.regexify("[A-Za-z0-9]{" + length + "}");
    }

    /**
     * Generate random sentence.
     */
    public static String generateSentence() {
        return faker.lorem().sentence();
    }

    /**
     * Generate random paragraph.
     */
    public static String generateParagraph() {
        return faker.lorem().paragraph();
    }

    /**
     * Get Faker instance for custom data generation.
     */
    public static Faker getFaker() {
        return faker;
    }
}
