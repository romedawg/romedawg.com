package com.curiousape.blog;

import com.curiousape.blog.model.UserEntity;
import com.curiousape.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:roman}")
    private String adminUsername;

    @Value("${app.admin.password:admin}")
    private String adminPassword;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seed() {
        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }
        UserEntity admin = new UserEntity(
                adminUsername,
                passwordEncoder.encode(adminPassword),
                "ADMIN"
        );
        userRepository.save(admin);
        System.out.println("UserSeeder: created admin user '" + adminUsername + "'");
    }
}
