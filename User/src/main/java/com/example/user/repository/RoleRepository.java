package com.example.user.repository;

import com.example.user.entity.Role;
import com.example.user.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleType(RoleType roleType);

}
