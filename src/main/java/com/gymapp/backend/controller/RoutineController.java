package com.gymapp.backend.controller;

import com.gymapp.backend.model.dto.RoutineCreateDTO;
import com.gymapp.backend.model.dto.RoutineExerciseResponseDTO;
import com.gymapp.backend.model.dto.RoutineResponseDTO;
import com.gymapp.backend.model.entity.Routine;
import com.gymapp.backend.service.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ResponseEntity<RoutineResponseDTO> createRoutine(@Valid @RequestBody RoutineCreateDTO request) {
        // 1. Llamamos al servicio (Transaccional)
        Routine newRoutine = routineService.createRoutine(request);

        // 2. Convertimos la Entidad a DTO de respuesta
        RoutineResponseDTO response = mapToDto(newRoutine);

        // 3. Devolvemos 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // --- MÉTODOS DE MAPEO (Manual Mapper) ---

    private RoutineResponseDTO mapToDto(Routine routine) {
        return RoutineResponseDTO.builder()
                .id(routine.getId())
                .name(routine.getName())
                .description(routine.getDescription())
                .difficultyLevel(routine.getDifficultyLevel().name())
                .exercises(routine.getRoutineExercises().stream()
                        .map(routineExercise -> RoutineExerciseResponseDTO.builder()
                                .id(routineExercise.getId())
                                .exerciseName(routineExercise.getExercise().getName()) // Sacamos el nombre del ejercicio
                                .videoUrl(routineExercise.getExercise().getVideoUrl())
                                .sets(routineExercise.getSets())
                                .reps(routineExercise.getRepetitions())
                                .weight(routineExercise.getWeight())
                                .restTime(routineExercise.getRestTime())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
