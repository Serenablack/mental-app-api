package com.mentalapp.service.interfaces;

import com.mentalapp.model.User;
import com.mentalapp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User registerUser(UserDto.UserRegistrationRequest request);

    UserDto.AuthResponse authenticateUser(UserDto.UserLoginRequest request);

    UserDto.UserProfileResponse getUserProfile(Long userId);

    UserDto.UserProfileResponse updateUserProfile(Long userId, UserDto.UserUpdateRequest request);

    void verifyEmail(String token);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);
}