package com.mentalapp.mapper;

import com.mentalapp.model.User;
import com.mentalapp.dto.UserDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    User toEntity(UserDto.UserRegistrationRequest request);

    @Mapping(target = "message", ignore = true)
    UserDto.UserProfileResponse toProfileResponse(User user);

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "expiresIn", constant = "900")
    @Mapping(target = "message", ignore = true)
    UserDto.AuthResponse toAuthResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    void updateEntity(@MappingTarget User entity, UserDto.UserUpdateRequest request);
}