package com.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UserResponse - POJO for paginated user list response.
 *
 * Maps the reqres.in API response structure:
 * {
 *   "page": 1,
 *   "per_page": 6,
 *   "total": 12,
 *   "total_pages": 2,
 *   "data": [...],
 *   "support": {...}
 * }
 *
 * @author Harsha Kumar
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("per_page")
    private Integer perPage;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("data")
    private List<User> data;

    @JsonProperty("support")
    private Support support;

    /**
     * Get user by index from data list.
     */
    public User getUser(int index) {
        if (data != null && index < data.size()) {
            return data.get(index);
        }
        return null;
    }

    /**
     * Get first user from list.
     */
    public User getFirstUser() {
        return getUser(0);
    }

    /**
     * Get count of users in response.
     */
    public int getUserCount() {
        return data != null ? data.size() : 0;
    }

    /**
     * Check if response has more pages.
     */
    public boolean hasNextPage() {
        return page != null && totalPages != null && page < totalPages;
    }

    /**
     * Inner class for support object.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Support {

        @JsonProperty("url")
        private String url;

        @JsonProperty("text")
        private String text;
    }
}
