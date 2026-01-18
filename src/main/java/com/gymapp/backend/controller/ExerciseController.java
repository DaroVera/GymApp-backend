package com.gymapp.backend.controller;

import com.gymapp.backend.model.dto.ExerciseResponseDTO;
import com.gymapp.backend.service.ExerciseService;
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
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<Page<ExerciseResponseDTO>> getAllExercises(
            @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable,
            // required = false significa que es opcional
            @RequestParam(required = false) String muscle
    ) {
        // Pasamos el filtro al servicio
        Page<ExerciseResponseDTO> result = exerciseService.getAllExercises(pageable, muscle);
        return ResponseEntity.ok(result);
    }
}
