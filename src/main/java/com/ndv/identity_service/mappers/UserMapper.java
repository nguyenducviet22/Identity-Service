package com.ndv.identity_service.mappers;

import com.ndv.identity_service.domain.dtos.request.CreateUserRequest;
import com.ndv.identity_service.domain.dtos.request.UpdateUserRequest;
import com.ndv.identity_service.domain.dtos.response.UserResponse;
import com.ndv.identity_service.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", source = "password")
    User toUser(CreateUserRequest request);
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UpdateUserRequest request);
}
