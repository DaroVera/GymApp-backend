package com.gymapp.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDTO {

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String currentPassword;


    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String newPassword;
}
