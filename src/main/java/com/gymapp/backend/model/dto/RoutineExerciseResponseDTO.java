package com.gymapp.backend.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoutineExerciseResponseDTO {
    private Long id;
    private String exerciseName; // Solo el nombre, no todo el objeto ejercicio
    private String videoUrl;
    private int sets;
    private int reps;
    private Double weight;
    private int restTime;

}
