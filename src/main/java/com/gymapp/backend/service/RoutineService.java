package com.gymapp.backend.service;

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
    private final UserRepository userRepository; // Para buscar al usuario actual


    @Transactional // ⚠️ CLAVE: Si algo falla, se hace rollback de todo
    public Routine createRoutine(RoutineCreateDTO request) {
        // 1. Obtener usuario autenticado (del Token)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la entidad Routine (Cabecera)
        Routine routine = Routine.builder()
                .name(request.getName())
                .description(request.getDescription())
                .difficultyLevel(DifficultyLevel.valueOf(request.getDifficultyLevel().toUpperCase()))
                .user(user)
                .routineExercises(new ArrayList<>()) // Inicializamos la lista vacía
                .build();

        // 3. Iterar sobre los ejercicios solicitados y crearlos
        request.getExercises().forEach(exDto -> {
            Exercise exercise = exerciseRepository.findById(exDto.getExerciseId())
                    .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado ID: " + exDto.getExerciseId()));

            RoutineExercise routineExercise = RoutineExercise.builder()
                    .routine(routine) // Enlazamos con la rutina padre
                    .exercise(exercise)
                    .sets(exDto.getSets())
                    .repetitions(exDto.getReps())
                    .weight(exDto.getWeight())
                    .restTime(exDto.getRestTime())
                    .build();

            // Agregamos a la lista de la rutina
            routine.getRoutineExercises().add(routineExercise);
        });

        // 4. Guardar (Gracias al CascadeType.ALL, guarda Rutina Y sus Ejercicios)
        return routineRepository.save(routine);
    }
}
