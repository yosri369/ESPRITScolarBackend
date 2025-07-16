package com.example.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String registrationNumber;
    private LocalDate enrollmentDate;
    private String status;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
