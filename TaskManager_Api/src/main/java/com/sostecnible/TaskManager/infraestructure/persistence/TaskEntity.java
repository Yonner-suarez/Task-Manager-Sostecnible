package com.sostecnible.TaskManager.infraestructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTask;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false) 
    private Integer isActive; // 1 = activo, 0 = borrado lógico

    @Column(nullable = false)
    private LocalDate fechaVencimiento;


    @PrePersist
protected void onCreate() {
    if (createdAt == null) {
        createdAt = LocalDate.now();  // Se asigna fecha actual antes de insertar
    }
    if (isActive == null) {
        isActive = 1; // cuando se cree activar por defecto
    }
}

    public enum Priority { ALTA, MEDIA, BAJA }
}