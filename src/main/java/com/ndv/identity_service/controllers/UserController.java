package com.ndv.identity_service.controllers;

import com.ndv.identity_service.Services.UserService;
import com.ndv.identity_service.domain.dtos.request.ApiResponse;
import com.ndv.identity_service.domain.dtos.request.CreateUserRequest;
import com.ndv.identity_service.domain.dtos.request.UpdateUserRequest;
import com.ndv.identity_service.domain.dtos.response.UserResponse;
import com.ndv.identity_service.domain.entities.User;
import com.ndv.identity_service.mappers.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        User user = userService.createUser(request);
        UserResponse userResponse = userMapper.toUserResponse(user);
        apiResponse.setResult(userResponse);
        return apiResponse;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userService.getAllUsers().stream()
                .map(user -> userMapper.toUserResponse(user)).toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id){
        User user = userService.getUser(id);
        return userMapper.toUserResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request){
        User updatedUser = userService.updateUser(id, request);
        return userMapper.toUserResponse(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
    }
}
