package com.ndv.identity_service.Services;

import com.ndv.identity_service.domain.dtos.request.CreateUserRequest;
import com.ndv.identity_service.domain.dtos.request.UpdateUserRequest;
import com.ndv.identity_service.domain.entities.User;
import com.ndv.identity_service.domain.enums.Role;
import com.ndv.identity_service.exception.AppException;
import com.ndv.identity_service.exception.ErrorCode;
import com.ndv.identity_service.mappers.UserMapper;
import com.ndv.identity_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public User createUser(CreateUserRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") //Check role before running method
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.username == authentication.name") //Get info of this user
    public User getUser(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist with id: " + id));
    }

    public User getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist!"));
    }

    public User updateUser(UUID id, UpdateUserRequest request) {
        User user = getUser(id);
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
