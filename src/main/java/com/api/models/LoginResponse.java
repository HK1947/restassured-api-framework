package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginResponse - POJO for authentication response.
 *
 * @author Harsha Kumar
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("error")
    private String error;

    /**
     * Check if login was successful.
     */
    public boolean isSuccess() {
        return token != null && !token.isEmpty();
    }

    /**
     * Check if login failed.
     */
    public boolean isFailed() {
        return error != null && !error.isEmpty();
    }
}
