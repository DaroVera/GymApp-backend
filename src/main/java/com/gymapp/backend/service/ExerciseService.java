package com.gymapp.backend.service;


import com.gymapp.backend.model.dto.ExerciseResponseDTO;
import com.gymapp.backend.model.entity.Exercise;
import com.gymapp.backend.model.enums.MuscleGroup;
import com.gymapp.backend.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public Page<ExerciseResponseDTO> getAllExercises(Pageable pageable, String muscleGroup) {

        Page<Exercise> exercisesPage;

        if (muscleGroup != null && !muscleGroup.isEmpty()) {
            // Convertimos el String "CHEST" al Enum MuscleGroup.CHEST
            // (Podría fallar si el string es inválido, idealmente validamos antes)
            try {
                MuscleGroup group = MuscleGroup.valueOf(muscleGroup.toUpperCase());
                exercisesPage = exerciseRepository.findByMuscleGroup(group, pageable);
            } catch (IllegalArgumentException e) {
                // Si mandan cualquier cosa ("BICEPS_FAKE"), devolvemos lista vacía o error.
                // Por simplicidad ahora, devolvemos todo o vacía. Usemos Page.empty()
                return Page.empty();
            }
        } else {
            // Sin filtro, traemos todo
            exercisesPage = exerciseRepository.findAll(pageable);
        }

        return exercisesPage.map(this::mapToDto);
    }


    private ExerciseResponseDTO mapToDto(Exercise exercise) {
        return ExerciseResponseDTO.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .muscleGroup(exercise.getMuscleGroup().name())
                .videoUrl(exercise.getVideoUrl())
                .build();
    }
}
