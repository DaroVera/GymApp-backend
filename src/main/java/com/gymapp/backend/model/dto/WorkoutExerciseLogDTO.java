package com.gymapp.backend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutExerciseLogDTO {

    @NotNull(message = "Debes especificar qué ejercicio hiciste (ID)")
    private Long exerciseId;

    @NotNull(message = "Faltan las series del ejercicio")
    private List<WorkoutSetLogDTO> sets;
}
