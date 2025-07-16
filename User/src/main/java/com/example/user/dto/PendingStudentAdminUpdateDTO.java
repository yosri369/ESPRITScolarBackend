package com.example.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PendingStudentAdminUpdateDTO {
    @NotBlank
    private String status;  // e.g. "validated", "rejected", "en attente"
}
