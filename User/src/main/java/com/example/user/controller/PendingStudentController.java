package com.example.user.controller;

import com.example.user.dto.PendingStudentAdminUpdateDTO;
import com.example.user.dto.PendingStudentCreateDTO;
import com.example.user.entity.PendingStudent;
import com.example.user.service.PendingStudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pending-students")
public class PendingStudentController {
    private final PendingStudentService service;

    public PendingStudentController(PendingStudentService service) {
        this.service = service;
    }

    // Create a new PendingStudent
    @PostMapping
    public ResponseEntity<PendingStudent> createPendingStudent(@Valid @RequestBody PendingStudentCreateDTO dto) {
        PendingStudent savedStudent = service.createPendingStudent(dto);
        return ResponseEntity.ok(savedStudent);
    }

    // Admin status update
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingStudent> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody PendingStudentAdminUpdateDTO dto) {

        Optional<PendingStudent> updated = service.updateStatus(id, dto);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
