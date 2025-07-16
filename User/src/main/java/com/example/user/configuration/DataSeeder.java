package com.example.user.configuration;

import com.example.user.entity.Role;
import com.example.user.entity.RoleType;
import com.example.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private final RoleRepository roleRepo;

    @Bean
    CommandLineRunner seedRoles() {
        return args -> {
            for (RoleType rt : RoleType.values()) {
                roleRepo.findByRoleType(rt)
                        .orElseGet(() -> roleRepo.save(new Role(null, rt)));
            }
        };
    }
}
