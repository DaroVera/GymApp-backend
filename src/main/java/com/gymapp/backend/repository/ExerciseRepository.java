package com.gymapp.backend.repository;


import com.gymapp.backend.model.entity.Exercise;
import com.gymapp.backend.model.enums.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    // Método derivado para buscar por nombre (útil para validaciones)
    Optional<Exercise> findByName (String name);

    // Método derivado para filtrar por grupo muscular (útil para el frontend)
    List<Exercise> findByMuscleGroup (MuscleGroup muscleGroup);
}
