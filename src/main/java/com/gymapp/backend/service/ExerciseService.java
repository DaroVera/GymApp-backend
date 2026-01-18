package com.gymapp.backend.service;


import com.gymapp.backend.model.dto.ExerciseResponseDTO;
import com.gymapp.backend.model.entity.Exercise;
import com.gymapp.backend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    // Método para obtener ejercicios paginados
    public Page<ExerciseResponseDTO> getAllExercises(Pageable pageable) {
        // 1. Buscamos la página de Entidades en la BD
        Page<Exercise> exercisesPage = exerciseRepository.findAll(pageable);

        // 2. Transformamos (mapeamos) cada Entidad a DTO
        return exercisesPage.map(this::mapToDto);
    }

    // Método auxiliar para convertir Entidad -> DTO
    private ExerciseResponseDTO mapToDto(Exercise exercise) {
        return ExerciseResponseDTO.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                // .name() convierte el ENUM a String ("CHEST")
                .muscleGroup(exercise.getMuscleGroup().name())
                .videoUrl(exercise.getVideoUrl())
                .build();
    }
}
