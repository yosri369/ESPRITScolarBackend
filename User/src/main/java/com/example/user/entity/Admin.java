package com.example.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String department;
    private String position;
    private LocalDate hireDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
