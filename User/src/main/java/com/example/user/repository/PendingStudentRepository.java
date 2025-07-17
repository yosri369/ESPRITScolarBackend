package com.example.user.repository;

import com.example.user.entity.PendingStudent;
import com.example.user.entity.PendingStudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingStudentRepository extends JpaRepository<PendingStudent,Long> {
    boolean existsByCin(String cin);
    List<PendingStudent> findByStatus(PendingStudentStatus status);

}
