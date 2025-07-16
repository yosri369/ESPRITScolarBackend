package com.example.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String phone;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private LocalDateTime lastLogin;
    private String address;
    private String gender;
    private String cin;
    private boolean isActive;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;




    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student studentProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacherProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin adminProfile;
}
