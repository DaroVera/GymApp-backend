package com.gymapp.backend.service;

import com.gymapp.backend.exception.ResourceNotFoundException; // <--- 1. IMPORTAR ESTO
import com.gymapp.backend.model.dto.RoutineCreateDTO;
import com.gymapp.backend.model.entity.Exercise;
import com.gymapp.backend.model.entity.Routine;
import com.gymapp.backend.model.entity.RoutineExercise;
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.model.enums.DifficultyLevel;
import com.gymapp.backend.repository.ExerciseRepository;
import com.gymapp.backend.repository.RoutineRepository;
import com.gymapp.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional
    public Routine createRoutine(RoutineCreateDTO request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // CAMBIO 1: Usar excepción personalizada para el Usuario
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")); // <--- CAMBIO AQUÍ

        Routine routine = Routine.builder()
                .name(request.getName())
                .description(request.getDescription())
                .difficultyLevel(DifficultyLevel.valueOf(request.getDifficultyLevel().toUpperCase()))
                .user(user)
                .routineExercises(new ArrayList<>())
                .build();

        request.getExercises().forEach(exDto -> {

            // CAMBIO 2: Usar excepción personalizada para el Ejercicio
            Exercise exercise = exerciseRepository.findById(exDto.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ejercicio no encontrado ID: " + exDto.getExerciseId())); // <--- CAMBIO AQUÍ

            RoutineExercise routineExercise = RoutineExercise.builder()
                    .routine(routine)
                    .exercise(exercise)
                    .sets(exDto.getSets())
                    .repetitions(exDto.getReps())
                    .weight(exDto.getWeight())
                    .restTime(exDto.getRestTime())
                    .build();

            routine.getRoutineExercises().add(routineExercise);
        });

        return routineRepository.save(routine);
    }
}