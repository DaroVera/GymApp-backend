package com.gymapp.backend.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseResponseDTO {

    @Schema(description = "ID único del ejercicio", example = "1")
    private Long id;

    @Schema(description = "Nombre del ejercicio", example = "Press de Banca")
    private String name;

    @Schema(description = "Descripción detallada de la técnica", example = "Recuéstate en el banco y empuja la barra...")
    private String description;

    @Schema(description = "Grupo muscular principal trabajado", example = "CHEST")
    private String muscleGroup;

    @Schema(description = "URL del video demostrativo", example = "https://youtube.com/watch?v=123")
    private String videoUrl;
}
