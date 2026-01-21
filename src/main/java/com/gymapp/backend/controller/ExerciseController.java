package com.gymapp.backend.controller;

import com.gymapp.backend.model.dto.ExerciseResponseDTO;
import com.gymapp.backend.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Tag(name = "Ejercicios", description = "Endpoints para consultar el catálogo de ejercicios") // Título de la sección
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Operation(
            summary = "Listar todos los ejercicios",
            description = "Obtiene una lista paginada de ejercicios. Permite filtrar por grupo muscular (opcional)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ejercicios recuperada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<ExerciseResponseDTO>> getAllExercises(
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable,
            @RequestParam(required = false) String muscle
    ) {
        Page<ExerciseResponseDTO> result = exerciseService.getAllExercises(pageable, muscle);
        return ResponseEntity.ok(result);
    }
}
