package com.gymapp.backend.service;

import com.gymapp.backend.model.dto.WorkoutLogCreateDTO;
import com.gymapp.backend.model.entity.*;
import com.gymapp.backend.repository.ExerciseRepository;
import com.gymapp.backend.repository.RoutineRepository;
import com.gymapp.backend.repository.UserRepository;
import com.gymapp.backend.repository.WorkoutLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutLogRepository workoutLogRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;

    @Transactional
    public WorkoutLog logWorkout(WorkoutLogCreateDTO request) {
        // 1. Obtener Usuario
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la cabecera (Log)
        WorkoutLog workoutLog = WorkoutLog.builder()
                .user(user)
                .startTime(LocalDateTime.now()) // Asumimos que lo guarda apenas termina
                .exercises(new ArrayList<>())
                .build();

        // 3. (Opcional) Si viene de una rutina, la vinculamos
        if (request.getRoutineId() != null) {
            Routine routine = routineRepository.findById(request.getRoutineId())
                    .orElse(null); // Si no existe, lo dejamos null o lanzamos error, decisión de negocio
            workoutLog.setRoutine(routine);
        }

        // 4. Mapeo de Ejercicios (Nivel 2)
        request.getExercises().forEach(exDto -> {
            Exercise exercise = exerciseRepository.findById(exDto.getExerciseId())
                    .orElseThrow(() -> new RuntimeException("Ejercicio ID no encontrado: " + exDto.getExerciseId()));

            WorkoutExercise workoutExercise = WorkoutExercise.builder()
                    .workoutLog(workoutLog) // Vinculamos al padre
                    .exercise(exercise)
                    .sets(new ArrayList<>())
                    .build();

            // 5. Mapeo de Series (Nivel 3)
            exDto.getSets().forEach(setDto -> {
                WorkoutSet set = WorkoutSet.builder()
                        .workoutExercise(workoutExercise) // Vinculamos al padre intermedio
                        .setNumber(setDto.getSetNumber())
                        .repetitions(setDto.getReps())
                        .weight(setDto.getWeight())
                        .build();

                workoutExercise.getSets().add(set);
            });

            workoutLog.getExercises().add(workoutExercise);
        });

        // 6. Guardar todo en cascada
        return workoutLogRepository.save(workoutLog);
    }
}
