package com.gymapp.backend.repository;

import com.gymapp.backend.model.entity.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    // Buscar historial de un usuario ordenado por fecha (el más reciente primero)
    List<WorkoutLog> findByUserIdOrderByStartTimeDesc(Long userId);
}
