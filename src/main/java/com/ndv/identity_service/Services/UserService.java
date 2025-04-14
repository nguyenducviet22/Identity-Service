package com.ndv.identity_service.Services;

import com.ndv.identity_service.domain.dtos.request.CreateUserRequest;
import com.ndv.identity_service.domain.dtos.request.UpdateUserRequest;
import com.ndv.identity_service.domain.entities.User;
import com.ndv.identity_service.exception.AppException;
import com.ndv.identity_service.exception.ErrorCode;
import com.ndv.identity_service.mappers.UserMapper;
import com.ndv.identity_service.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(CreateUserRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist with id: " + id));
    }

    public User updateUser(UUID id, UpdateUserRequest request) {
        User user = getUser(id);
        userMapper.updateUser(user, request);
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
