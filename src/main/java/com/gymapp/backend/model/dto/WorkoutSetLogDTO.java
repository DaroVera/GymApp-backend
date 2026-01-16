package com.gymapp.backend.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class WorkoutSetLogDTO {
    @Min(value = 1, message = "El número de serie es obligatorio")
    private int setNumber;

    @Min(value = 1, message = "Las repeticiones deben ser al menos 1")
    private int reps;

    @Min(value = 0, message = "El peso no puede ser negativo")
    private Double weight;
}
