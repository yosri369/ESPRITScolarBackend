package com.example.user.service;

import com.example.user.dto.PendingTeacherAdminUpdateDTO;
import com.example.user.dto.PendingTeacherCreateDTO;
import com.example.user.entity.*;
import com.example.user.repository.PendingTeacherRepository;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.TeacherRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PendingTeacherService {
    private final PendingTeacherRepository pendingTeacherRepo;
    private final UserRepository userRepo;
    private final TeacherRepository teacherRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RoleRepository roleRepo;
    private final UserService userService;

    // Create new pending teacher
    public PendingTeacher create(PendingTeacherCreateDTO dto) {
        PendingTeacher teacher = new PendingTeacher();
        teacher.setCin(dto.cin());
        teacher.setFirstName(dto.firstName());
        teacher.setLastName(dto.lastName());
        teacher.setAddress(dto.address());
        teacher.setEmail(dto.email());
        teacher.setPhone(dto.phone());
        teacher.setDepartment(dto.department());
        teacher.setGrade(dto.grade());
        teacher.setSpeciality(dto.speciality());
        teacher.setFullTime(dto.fullTime());
        teacher.setDateEmbauche(dto.dateEmbauche());
        return pendingTeacherRepo.save(teacher);
    }

    // List all pending teachers
    public List<PendingTeacher> findAll() {
        return pendingTeacherRepo.findAll();
    }

    // Update status and validate teacher
    public Optional<PendingTeacher> updateStatus(Long id, PendingTeacherAdminUpdateDTO dto) {
        Optional<PendingTeacher> optional = pendingTeacherRepo.findById(id);
        if (optional.isEmpty()) return Optional.empty();

        PendingTeacher pending = optional.get();

        if ("VALIDATED".equalsIgnoreCase(dto.status())) {
            // ✅ Delegate to UserService
            userService.createTeacherFromPending(pending);
        } else {
            // REJECTED or PENDING — just update status
            pending.setStatus(dto.status());
            pendingTeacherRepo.save(pending);
        }

        return Optional.of(pending);
    }
}
