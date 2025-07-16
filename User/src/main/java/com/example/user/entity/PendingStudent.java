package com.example.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PendingStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String cin;
    private String phone;
    private LocalDate birthdate;
    private String address;
    private String gender;

    private String academicYear;       // e.g., "1ère année", "2ème année"
    private String speciality;         // e.g., "Prépa", "Génie Logiciel", "Génie Mécanique"
    private String courseType;  // "J" for jour, "S" for soir

    @Enumerated(EnumType.STRING)
    private PendingStudentStatus status = PendingStudentStatus.EN_ATTENTE;
    private String registrationNumber;      // to be generated later
    private LocalDate enrollmentDate;       // to be generated when accepted
}
