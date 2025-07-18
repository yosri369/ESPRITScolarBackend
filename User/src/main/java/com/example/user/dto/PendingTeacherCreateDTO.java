package com.example.user.dto;

import java.time.LocalDate;

public record PendingTeacherCreateDTO(String cin,
                                      String firstName,
                                      String lastName,
                                      String email,
                                      String address,
                                      String phone,
                                      String department,
                                      String grade,
                                      String speciality,
                                      boolean fullTime,
                                      LocalDate dateEmbauche) {
}
