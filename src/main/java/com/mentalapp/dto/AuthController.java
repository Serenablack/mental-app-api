package com.mentalapp.controller;

import com.mentalapp.dto.UserDto;
import com.mentalapp.model.User;
import com.mentalapp.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<User.AuthResponse> registerUser(
            @Valid @RequestBody User.UserRegistrationRequest request) {
        log.info("Registration request received for username: {}", request.getUsername());

        try {
            User user = userService.registerUser(request);
            log.info("User registered successfully: {}", request.getUsername());

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Registration failed for username: {}", request.getUsername(), e);

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<User.AuthResponse> loginUser(@Valid @RequestBody User.UserLoginRequest request) {
        log.info("Login request received for user: {}", request.getUsernameOrEmail());

        try {
            User.AuthResponse response = userService.authenticateUser(request);
            log.info("User logged in successfully: {}", request.getUsernameOrEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsernameOrEmail(), e);

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Google OAuth callback endpoint
     */
    @PostMapping("/google/callback")
    public ResponseEntity<User.AuthResponse> googleAuthCallback(
            @Valid @RequestBody User.GoogleAuthRequest request) {
        log.info("Google OAuth callback request received");

        try {
            // TODO: Implement Google OAuth verification and user creation/login
            // For now, return a placeholder response
            log.info("Google OAuth callback processed");

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("Google OAuth callback received. Implementation pending.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Google OAuth callback failed", e);

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("Google OAuth failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Email verification endpoint
     */
    @PostMapping("/verify-email")
    public ResponseEntity<User.AuthResponse> verifyEmail(
            @Valid @RequestBody User.EmailVerificationRequest request) {
        log.info("Email verification request received for token: {}", request.getToken());

        try {
            userService.verifyEmail(request.getToken());
            log.info("Email verified successfully");

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("Email verified successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Email verification failed", e);

            User.AuthResponse response = new User.AuthResponse();
            response.setMessage("Email verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Request password reset endpoint
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<UserDto.AuthResponse> requestPasswordReset(
            @Valid @RequestBody UserDto.PasswordResetRequest request) {
        log.info("Password reset request received for email: {}", request.getEmail());

        try {
            userService.requestPasswordReset(request.getEmail());
            log.info("Password reset email sent successfully");

            UserDto.AuthResponse response = new UserDto.AuthResponse();
            response.setMessage("Password reset email sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password reset request failed", e);

            UserDto.AuthResponse response = new UserDto.AuthResponse();
            response.setMessage("Password reset failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Confirm password reset endpoint
     */
    @PostMapping("/reset-password")
    public ResponseEntity<UserDto.AuthResponse> confirmPasswordReset(
            @Valid @RequestBody UserDto.PasswordResetConfirmRequest request) {
        log.info("Password reset confirmation request received");

        try {
            userService.resetPassword(request.getToken(), request.getNewPassword());
            log.info("Password reset successfully");

            UserDto.AuthResponse response = new UserDto.AuthResponse();
            response.setMessage("Password reset successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password reset confirmation failed", e);

            UserDto.AuthResponse response = new UserDto.AuthResponse();
            response.setMessage("Password reset failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<UserDto.AuthResponse> logout() {
        log.info("Logout request received");

        try {
            // TODO: Implement token blacklisting or invalidation
            // For now, just return success response
            log.info("User logged out successfully");

            UserDto.AuthResponse response = new UserDto.AuthResponse();
            response.setMessage("Logged out successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Logout failed", e);

            UserDto.AuthResponse response = new UserDto.AuthResponse();
            response.setMessage("Logout failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Health check endpoint for authentication service
     */
    @GetMapping("/health")
    public ResponseEntity<UserDto.AuthResponse> healthCheck() {
        log.debug("Auth service health check");

        UserDto.AuthResponse response = new UserDto.AuthResponse();
        response.setMessage("Authentication service is running");
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user profile (requires authentication)
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDto.UserProfileResponse> getCurrentUserProfile() {
        // TODO: Implement getting current user from security context
        log.info("Profile request received");

        UserDto.UserProfileResponse response = new UserDto.UserProfileResponse();
        response.setMessage("Profile endpoint - implementation pending");
        return ResponseEntity.ok(response);
    }

    /**
     * Update current user profile (requires authentication)
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDto.UserProfileResponse> updateCurrentUserProfile(
            @Valid @RequestBody UserDto.UserUpdateRequest request) {
        // TODO: Implement updating current user profile
        log.info("Profile update request received");

        UserDto.UserProfileResponse response = new UserDto.UserProfileResponse();
        response.setMessage("Profile update endpoint - implementation pending");
        return ResponseEntity.ok(response);
    }
}