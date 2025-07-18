package com.example.user.controller;

import com.example.user.dto.PendingTeacherAdminUpdateDTO;
import com.example.user.dto.PendingTeacherCreateDTO;
import com.example.user.entity.PendingTeacher;
import com.example.user.service.PendingTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pending-teachers")
@RequiredArgsConstructor
public class PendingTeacherController {
    private final PendingTeacherService pendingTeacherService;

    // 1️⃣ - Submit teacher registration (from frontend form)
    @PostMapping
    public ResponseEntity<PendingTeacher> createPendingTeacher(@RequestBody PendingTeacherCreateDTO dto) {
        PendingTeacher created = pendingTeacherService.create(dto);
        return ResponseEntity.ok(created);
    }

    // 2️⃣ - Admin lists all pending teachers
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PendingTeacher>> getAllPendingTeachers() {
        return ResponseEntity.ok(pendingTeacherService.findAll());
    }

    // 3️⃣ - Admin validates or rejects teacher registration
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingTeacher> updateTeacherStatus(
            @PathVariable Long id,
            @RequestBody PendingTeacherAdminUpdateDTO dto) {
        return pendingTeacherService.updateStatus(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

