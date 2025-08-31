package com.mentalapp.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentalapp.dto.UserDto;
import com.mentalapp.service.JwtService;
import com.mentalapp.service.interfaces.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // Skip authentication for public endpoints
            if (isPublicEndpoint(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;

            // Check if Authorization header exists and has Bearer token
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                log.debug("No valid Authorization header found for URI: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7); // Remove "Bearer " prefix
            log.debug("Processing JWT token for request: {}", request.getRequestURI());

            // Validate token structure first
            if (!jwtService.isTokenValidStructure(jwt)) {
                log.warn("Invalid JWT token structure for request: {}", request.getRequestURI());
                handleAuthenticationError(response, "Invalid token format", 401);
                return;
            }

            // Check if token is expired
            if (jwtService.isTokenExpired(jwt)) {
                log.warn("Expired JWT token for request: {}", request.getRequestURI());
                handleAuthenticationError(response, "Token expired", 401);
                return;
            }

            // Extract username from token
            username = jwtService.extractUsername(jwt);
            if (!StringUtils.hasText(username)) {
                log.warn("Could not extract username from JWT token");
                handleAuthenticationError(response, "Invalid token content", 401);
                return;
            }

            // Check if user is already authenticated
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Setting up authentication for user: {}", username);

                // Load user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate token against user details
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Create authentication token with authorities
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    // Set additional details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("Authentication successful for user: {} with roles: {}",
                            username, userDetails.getAuthorities());

                    // Log token information for debugging
                    if (log.isDebugEnabled()) {
                        jwtService.logTokenInfo(jwt);
                    }

                    // Check if token is expiring soon and log warning
                    if (jwtService.isTokenExpiringSoon(jwt, 5)) { // 5 minutes threshold
                        log.warn("JWT token for user {} will expire soon", username);
                    }

                } else {
                    log.warn("Invalid JWT token for user: {}", username);
                    handleAuthenticationError(response, "Invalid token", 401);
                    return;
                }
            }

            // Continue with the filter chain
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error processing JWT authentication for request: {}", request.getRequestURI(), e);
            handleAuthenticationError(response, "Authentication error", 500);
        }
    }

    /**
     * Check if the endpoint is public (doesn't require authentication)
     */
    private boolean isPublicEndpoint(String requestURI) {
        String[] publicEndpoints = {
                "/api/auth/login",
                "/api/auth/register",
                "/api/auth/refresh",
                "/api/auth/google/callback",
                "/api/auth/forgot-password",
                "/api/auth/reset-password",
                "/api/auth/verify-email",
                "/api/health",
                "/api/public",
                "/error",
                "/swagger-ui",
                "/v3/api-docs"
        };

        for (String endpoint : publicEndpoints) {
            if (requestURI.startsWith(endpoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handle authentication errors with proper HTTP response
     */
    private void handleAuthenticationError(HttpServletResponse response, String message, int status)
            throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        UserDto.AuthResponse errorResponse = UserDto.AuthResponse.builder()
                .message(message)
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

    /**
     * Get current authenticated user from security context
     */
    public static String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Get current authenticated user ID from security context
     */
    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                // You might need to implement a custom UserDetails that includes user ID
                // For now, return null and implement as needed
                return null;
            }
        }
        return null;
    }

    /**
     * Check if current user has specific role
     */
    public static boolean hasRole(String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
        }
        return false;
    }

    /**
     * Check if current user has any of the specified roles
     */
    public static boolean hasAnyRole(String... roles) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Set<String> userRoles = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toSet());

            for (String role : roles) {
                if (userRoles.contains(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get current user's roles
     */
    public static Set<String> getCurrentUserRoles() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * Log authentication details for debugging
     */
    private void logAuthenticationDetails(String username, String requestURI) {
        if (log.isDebugEnabled()) {
            log.debug("Authentication successful - User: {}, URI: {}, Method: {}",
                    username, requestURI, "GET"); // You can get actual method from request
        }
    }
}
