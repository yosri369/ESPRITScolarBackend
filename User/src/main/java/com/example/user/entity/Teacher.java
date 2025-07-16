package com.example.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateEmbauche;
    private String grade;
    private String speciality;
    private boolean isFullTime;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
