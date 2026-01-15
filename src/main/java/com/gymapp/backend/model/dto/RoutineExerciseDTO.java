package com.gymapp.backend.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoutineExerciseDTO {

    @NotNull(message = "El ID del ejercicio es obligatorio")
    private Long exerciseId;

    @Min(value = 1, message = "Debe haber al menos 1 serie")
    private int sets;

    @Min(value = 1, message = "Debe haber al menos 1 repetición")
    private int reps;

    private Double weight; // Opcional

    @Min(value = 0, message = "El tiempo de descanso no puede ser negativo")
    private int restTime;
}

