package com.sostecnible.TaskManager.domain.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  private Long idUser;
    @NotBlank(message = "Debe especificar un user name")
    private String userName;
    @NotBlank(message = "Debe especificar un email")
    private String email;
    @NotBlank(message = "Debe especificar una password")
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}

