package com.ndv.identity_service.services;

import com.ndv.identity_service.domain.dtos.request.CreateUserRequest;
import com.ndv.identity_service.domain.dtos.request.UpdateUserRequest;
import com.ndv.identity_service.domain.dtos.response.UserResponse;
import com.ndv.identity_service.domain.entities.Role;
import com.ndv.identity_service.domain.entities.User;
import com.ndv.identity_service.mappers.UserMapper;
import com.ndv.identity_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserResponse createUser(CreateUserRequest request) throws Exception {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("Username existed!");
        }
        User newUser = userMapper.toUser(request);

        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<UUID> roleIds = request.getRoleIds();
        List<Role> roles = roleService.getRoleByIds(roleIds);
        newUser.setRoles(new HashSet<>(roles));

        User savedUser = userRepository.save(newUser);
        return userMapper.toUserResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')") //Access by Role
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> userMapper.toUserResponse(user))
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name") //Get info of this user
    public UserResponse getUser(UUID id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist with id: " + id)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return userMapper.toUserResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist!")));
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')") //Access by Permission
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist with id: " + id));
        userMapper.updateUser(existingUser, request);
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<UUID> roleIds = request.getRoleIds();
        List<Role> roles = roleService.getRoleByIds(roleIds);
        existingUser.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(existingUser));
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
