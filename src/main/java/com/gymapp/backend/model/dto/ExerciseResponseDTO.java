package com.gymapp.backend.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String muscleGroup; // Lo devolvemos como String (ej: "CHEST")
    private String videoUrl;
}
