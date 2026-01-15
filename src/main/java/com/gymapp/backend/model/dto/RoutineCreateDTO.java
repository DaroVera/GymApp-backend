package com.gymapp.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RoutineCreateDTO {
    @NotBlank(message = "El nombre de la rutina es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "La dificultad es obligatoria (BEGINNER, INTERMEDIATE, ADVANCED)")
    private String difficultyLevel; // Lo recibimos como String y validamos después

    @NotNull(message = "La lista de ejercicios no puede ser nula")
    private List<RoutineExerciseDTO> exercises;
}
