package com.example.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingStudentCreateDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String cin;

    @NotBlank
    private String gender;

    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String academicYear; // e.g., "1ère année"

    @NotBlank
    private String speciality;   // e.g., "Prépa", "Génie Logiciel"

    @NotBlank
    @Pattern(regexp = "J|S", message = "Course type must be 'J' (jour) or 'S' (soir)")
    private String courseType;
}
