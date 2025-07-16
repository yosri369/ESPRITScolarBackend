package com.example.user.configuration;

import com.example.user.entity.Role;
import com.example.user.entity.RoleType;
import com.example.user.entity.User;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminSeeder {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner seedAdmin() {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {

                // Fetch or create the ADMIN role
                var adminRole = roleRepo.findByRoleType(RoleType.ADMIN).orElseGet(() -> {
                    Role newRole = new Role();  // Use no-args constructor
                    newRole.setRoleType(RoleType.ADMIN);  // Set the role type
                    return roleRepo.save(newRole);  // Save and return
                });

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@mail.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setActive(true);
                admin.setRole(adminRole);  // Assign the role


                //admin.setRole(
                  //      roleRepo.findByRoleType(RoleType.ADMIN)
                    //            .orElseThrow(() -> new IllegalStateException("Role ADMIN missing"))
                //);

                userRepo.save(admin);
                System.out.println("➡️  Admin user created (username: admin / password: admin123)");
            }
        };
    }
}
