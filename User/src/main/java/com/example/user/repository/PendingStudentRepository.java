package com.example.user.repository;

import com.example.user.entity.PendingStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingStudentRepository extends JpaRepository<PendingStudent,Long> {
    boolean existsByCin(String cin);
}
