package com.mentalapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    @NoArgsConstructor
    public static class UserRegistrationRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private String firstName;
        private String lastName;
        private String profilePictureUrl;
    }

    @Data
    @NoArgsConstructor
    public static class UserLoginRequest {
        @NotBlank(message = "Username/Email is required")
        private String usernameOrEmail;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    @NoArgsConstructor
    public static class GoogleAuthRequest {
        @NotBlank(message = "Google token is required")
        private String token;
    }

    @Data
    @NoArgsConstructor
    public static class AuthResponse {
        private String accessToken;
        private String tokenType;
        private int expiresIn;
        private String message;

        public AuthResponse(String message) {
            this.message = message;
        }

        public AuthResponse(String accessToken, String tokenType, int expiresIn, String message) {
            this.accessToken = accessToken;
            this.tokenType = tokenType;
            this.expiresIn = expiresIn;
            this.message = message;
        }
    }

    @Data
    @NoArgsConstructor
    public static class UserProfileResponse {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String profilePictureUrl;
        private boolean isEmailVerified;
        private LocalDateTime lastLoginAt;
        private String message;

        public UserProfileResponse(String message) {
            this.message = message;
        }
    }

    @Data
    @NoArgsConstructor
    public static class UserUpdateRequest {
        private String firstName;
        private String lastName;
        private String profilePictureUrl;
    }

    @Data
    @NoArgsConstructor
    public static class PasswordResetRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
    }

    @Data
    @NoArgsConstructor
    public static class PasswordResetConfirmRequest {
        @NotBlank(message = "Token is required")
        private String token;

        @NotBlank(message = "New password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String newPassword;
    }

    @Data
    @NoArgsConstructor
    public static class EmailVerificationRequest {
        @NotBlank(message = "Token is required")
        private String token;
    }
}
