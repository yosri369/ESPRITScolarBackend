package com.example.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PendingTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cin;
    private String firstName;
    private String lastName;
    private String email;      // Personal email (used to send confirmation)
    private String address;
    private String phone;
    private String department; // Or subject
    private String grade;
    private String speciality;
    private boolean fullTime;
    private LocalDate dateEmbauche;

    private String status = "EN_ATTENTE"; // or PENDING
}
