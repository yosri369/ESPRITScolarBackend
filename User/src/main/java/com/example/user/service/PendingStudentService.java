package com.example.user.service;

import com.example.user.dto.PendingStudentAdminUpdateDTO;
import com.example.user.dto.PendingStudentCreateDTO;
import com.example.user.entity.PendingStudent;
import com.example.user.entity.PendingStudentStatus;
import com.example.user.repository.PendingStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PendingStudentService {
    private final PendingStudentRepository repository;

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
        student.setStatus(PendingStudentStatus.EN_ATTENTE);  // default on creation
        return repository.save(student);
    }

    // Status update method for admin
    public Optional<PendingStudent> updateStatus(Long id, PendingStudentAdminUpdateDTO dto) {
        Optional<PendingStudent> optionalStudent = repository.findById(id);
        if (optionalStudent.isPresent()) {
            PendingStudent student = optionalStudent.get();
            PendingStudentStatus newStatus;
            try {
                newStatus = PendingStudentStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // invalid status string
                return Optional.empty();
            }

            student.setStatus(newStatus);

            // When status changes to ACCEPTE, set enrollment date and registration number
            if (newStatus == PendingStudentStatus.ACCEPTE) {
                student.setEnrollmentDate(LocalDate.now());
                student.setRegistrationNumber(generateRegistrationNumber(student));
            }

            repository.save(student);
            return Optional.of(student);
        }
        return Optional.empty();
    }
    private String generateRegistrationNumber(PendingStudent student) {
        // Year (last two digits)
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);

        // Academic year as a single digit
        String academicYearDigit = mapAcademicYearToDigit(student.getAcademicYear());

        // Course type letter: "J" or "S"
        String courseTypeLetter = student.getCourseType().toUpperCase();  // Should be "J" or "S"

        // Gender initial (M or F)
        String genderInitial = student.getGender().substring(0,1).toUpperCase();

        // Fixed nationality 'T'
        String nationality = "T";

        // Random unique 4-digit number
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

}
