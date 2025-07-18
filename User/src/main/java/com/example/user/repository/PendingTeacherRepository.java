package com.example.user.repository;

import com.example.user.entity.PendingTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingTeacherRepository extends JpaRepository<PendingTeacher,Long> {
    List<PendingTeacher> findByStatus(String status);

}
