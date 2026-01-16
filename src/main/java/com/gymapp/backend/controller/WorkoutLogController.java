package com.gymapp.backend.controller;

import com.gymapp.backend.model.dto.WorkoutLogCreateDTO;
import com.gymapp.backend.model.entity.WorkoutLog;
import com.gymapp.backend.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutLogController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<String> logWorkout(@Valid @RequestBody WorkoutLogCreateDTO request) {
        WorkoutLog log = workoutService.logWorkout(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Entrenamiento registrado con éxito. ID: " + log.getId());
    }
}
