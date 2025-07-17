package com.example.user.service;

import com.example.user.dto.PendingStudentAdminUpdateDTO;
import com.example.user.dto.PendingStudentCreateDTO;
import com.example.user.entity.PendingStudent;
import com.example.user.entity.PendingStudentStatus;
import com.example.user.repository.PendingStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PendingStudentService {
    private final PendingStudentRepository repository;
    private final UserService userService;

    public PendingStudent createPendingStudent(PendingStudentCreateDTO dto) {
        PendingStudent student = new PendingStudent();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setCin(dto.getCin());
        student.setGender(dto.getGender());
        student.setBirthdate(dto.getBirthdate());
        student.setPhone(dto.getPhone());
        student.setAddress(dto.getAddress());
        student.setAcademicYear(dto.getAcademicYear());
        student.setSpeciality(dto.getSpeciality());
        student.setCourseType(dto.getCourseType());
        student.setStatus(PendingStudentStatus.EN_ATTENTE);  // default
        return repository.save(student);
    }

    // Admin updates the pending student's status
    public Optional<PendingStudent> updateStatus(Long id, PendingStudentAdminUpdateDTO dto) {
        Optional<PendingStudent> optionalStudent = repository.findById(id);

        if (optionalStudent.isEmpty()) return Optional.empty();

        PendingStudent student = optionalStudent.get();
        PendingStudentStatus newStatus;

        try {
            newStatus = PendingStudentStatus.valueOf(dto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Optional.empty();  // Invalid status
        }

        student.setStatus(newStatus);

        // If accepted → set extra info, create user, and delete from pending
        if (newStatus == PendingStudentStatus.ACCEPTE) {
            student.setEnrollmentDate(LocalDate.now());
            student.setRegistrationNumber(generateRegistrationNumber(student));

            try {
                userService.createStudentFromPending(student);
                repository.delete(student);  // Only delete if account creation succeeded
                return Optional.of(student);
            } catch (Exception e) {
                e.printStackTrace();
                // rollback: maybe log the failure, return without deleting
                return Optional.empty();
            }
        }

        // If not accepted, just update status and save
        return Optional.of(repository.save(student));
    }

    private String generateRegistrationNumber(PendingStudent student) {
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);  // e.g. "25"
        String academicYearDigit = mapAcademicYearToDigit(student.getAcademicYear());
        String courseTypeLetter = student.getCourseType().toUpperCase();  // "J" or "S"
        String genderInitial = student.getGender().substring(0, 1).toUpperCase();  // "M" or "F"
        String nationality = "T";
        String randomDigits = generateUnique4DigitNumber();

        return year + academicYearDigit + courseTypeLetter + genderInitial + nationality + randomDigits;
    }

    private String mapAcademicYearToDigit(String academicYear) {
        switch (academicYear.toLowerCase()) {
            case "1ère année":
            case "1ere année":
            case "first year":
                return "1";
            case "2ème année":
            case "2eme année":
            case "second year":
                return "2";
            case "3ème année":
            case "3eme année":
            case "third year":
                return "3";
            default:
                return "0";
        }
    }

    private String generateUnique4DigitNumber() {
        int number = (int)(Math.random() * 10000);
        return String.format("%04d", number);
    }

    public List<PendingStudent> findAll() {
        return repository.findAll();
    }

    public Optional<PendingStudent> findById(Long id) {
        return repository.findById(id);
    }

    public List<PendingStudent> findByStatus(String status) {
        return repository.findByStatus(PendingStudentStatus.valueOf(status.toUpperCase()));
    }
}
