package com.sostecnible.TaskManager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Task {
    private Long idTask;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate createdAt;
    private String status;
    private Integer isActive; // 1 = activo, 0 = borrado lógico
    private LocalDate fechaVencimiento;

    public enum Priority {
        ALTA, MEDIA, BAJA
    }
}