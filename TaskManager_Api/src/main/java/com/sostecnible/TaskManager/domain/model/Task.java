package com.sostecnible.TaskManager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Task {
    private Long idTask;
    private String title;
    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;
    private Priority priority;
    private LocalDate createdAt;
    private String status;
    private Integer isActive; // 1 = activo, 0 = borrado lógico
    private LocalDate fechaVencimiento;
    private Long userId;

    public enum Priority {
        ALTA, MEDIA, BAJA
    }
}