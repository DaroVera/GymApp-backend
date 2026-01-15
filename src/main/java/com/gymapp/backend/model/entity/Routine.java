package com.gymapp.backend.model.entity;


import com.gymapp.backend.model.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    // FECHA DE CREACIÓN (Simple, sin auditoría compleja por ahora)
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    // RELACIÓN: Muchas rutinas pertenecen a UN usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Hook para guardar la fecha automáticamente antes de crear
    @PrePersist
    protected void onCreate(){
        createdDate = LocalDateTime.now();
    }
}
