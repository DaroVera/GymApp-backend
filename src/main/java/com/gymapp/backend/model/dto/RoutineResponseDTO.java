package com.gymapp.backend.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoutineResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String difficultyLevel;
    private List<RoutineExerciseResponseDTO> exercises;
}
