package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * User - POJO model for User entity.
 *
 * Design Decisions:
 * 1. Jackson Annotations - Control JSON serialization/deserialization
 * 2. Lombok - Reduce boilerplate (getters, setters, constructors)
 * 3. Builder Pattern - Fluent object creation
 * 4. Null handling - Ignore nulls during serialization
 *
 * Why use POJOs?
 * - Type safety for request/response bodies
 * - IDE autocompletion and refactoring support
 * - Reusable across tests
 * - Easy to validate and compare
 *
 * @author Harsha Kumar
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore extra fields in response
@JsonInclude(JsonInclude.Include.NON_NULL)    // Don't serialize null fields
public class User {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("avatar")
    private String avatar;

    // For create/update requests (reqres.in API)
    @JsonProperty("name")
    private String name;

    @JsonProperty("job")
    private String job;

    // Response fields from create/update
    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    /**
     * Get full name (first + last).
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return name;
    }

    /**
     * Static factory method for creating user for POST request.
     */
    public static User createUser(String name, String job) {
        return User.builder()
                .name(name)
                .job(job)
                .build();
    }

    /**
     * Static factory method with all fields.
     */
    public static User of(String email, String firstName, String lastName) {
        return User.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
