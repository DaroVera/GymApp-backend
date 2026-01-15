package com.gymapp.backend.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "routine_exercises")
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CONFIGURACIÓN DEL EJERCICIO
    @Column(nullable = false)
    private int sets; // Cantidad de series (ej: 4)

    @Column(nullable = false)
    private int repetitions; // Repeticiones objetivo (ej: 12)

    @Column(name = "weight_kg")
    private Double weight; // Peso sugerido (opcional)

    @Column(name = "rest_time_seconds")
    private int restTime; // Descanso entre series (ej: 60 seg)

    // ORDEN: Para que los ejercicios salgan ordenados (1, 2, 3...)
    @Column(name = "display_order")
    private int order;

    // RELACIÓN 1: Pertenece a una Rutina
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    // RELACIÓN 2: Apunta a un Ejercicio del catálogo
    @ManyToOne(fetch = FetchType.EAGER) // Eager porque casi siempre queremos ver el nombre del ejercicio
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
}
