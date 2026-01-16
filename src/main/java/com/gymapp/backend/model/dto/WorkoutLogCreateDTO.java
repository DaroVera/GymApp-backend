package com.gymapp.backend.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutLogCreateDTO {

    // Opcional: si vino de una rutina predefinida
    private Long routineId;

    @NotEmpty(message = "El entrenamiento debe tener al menos un ejercicio")
    private List<WorkoutExerciseLogDTO> exercises;
}
