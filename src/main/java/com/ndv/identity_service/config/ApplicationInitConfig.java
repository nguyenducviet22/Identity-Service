package com.ndv.identity_service.config;

import com.ndv.identity_service.domain.entities.User;
import com.ndv.identity_service.domain.enums.Role;
import com.ndv.identity_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("SecurityPass123!"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Admin account has been created with username is admin and password is SecurityPass123!");
            }
        };
    }
}
