package com.gymapp.backend.model.entity;


import com.gymapp.backend.model.enums.Role;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    //auditoría simple: para saber cuando se creó el usuario
    private LocalDateTime createdAt;

    @PrePersist //Se ejecuta antes de guardar en la BD
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
