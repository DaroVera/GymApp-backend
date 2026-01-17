package com.gymapp.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymapp.backend.model.dto.RoutineCreateDTO;
import com.gymapp.backend.model.dto.RoutineExerciseDTO;
import com.gymapp.backend.model.entity.Routine;
import com.gymapp.backend.model.enums.DifficultyLevel;
import com.gymapp.backend.security.JwtService;
import com.gymapp.backend.service.RoutineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. @WebMvcTest: Solo levanta el contexto web para este controller.
// Excluye filtros de seguridad complejos por defecto para facilitar el test unitario.
@WebMvcTest(RoutineController.class)
@AutoConfigureMockMvc(addFilters = false) // ⚠️ Importante: Desactiva la seguridad (Login/JWT) para este test
public class RoutineControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula las peticiones HTTP (El "Postman" falso)

    @MockitoBean// Crea un Mock del servicio y lo inyecta en el contexto de Spring
    private RoutineService routineService;

    @Autowired
    private ObjectMapper objectMapper; // Convierte Objetos Java <-> JSON String

    // --- AGREGAR ESTO ---
    // Inyectamos un mock de JwtService para que Spring Security no explote al iniciar.
    // No lo usamos en el test, pero lo necesitamos para que la app "compile" en memoria.
    @MockitoBean
    private JwtService jwtService;
    // --------------------

    private RoutineCreateDTO validRequest;
    private Routine mockRoutineEntity;

    @BeforeEach
    void setUp() {
        // Preparamos datos de prueba
        RoutineExerciseDTO exDto = new RoutineExerciseDTO();
        exDto.setExerciseId(1L);
        exDto.setSets(4);
        exDto.setReps(10);
        exDto.setRestTime(60);

        validRequest = new RoutineCreateDTO();
        validRequest.setName("Rutina Mock");
        validRequest.setDifficultyLevel("BEGINNER");
        validRequest.setExercises(List.of(exDto));

        // Preparamos la entidad que "devolvería" el servicio
        mockRoutineEntity = Routine.builder()
                .id(1L)
                .name("Rutina Mock")
                .description("Desc")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .routineExercises(Collections.emptyList()) // Lista vacía para simplificar
                .build();
    }

    @Test
    void createRoutine_ShouldReturnCreatedStatus_WhenRequestIsValid() throws Exception {
        // --- GIVEN (El escenario) ---
        // Le decimos al Mock del servicio: "Cuando te llamen, devolvé esta entidad"
        // Nota: En el controller real, mapeamos la entidad a DTO, pero el servicio devuelve entidad.
        when(routineService.createRoutine(any(RoutineCreateDTO.class))).thenReturn(mockRoutineEntity);

        // --- WHEN & THEN (Ejecución y Verificación) ---
        mockMvc.perform(post("/api/routines") // 1. Hacemos POST a la URL
                        .contentType(MediaType.APPLICATION_JSON) // 2. Decimos que enviamos JSON
                        .content(objectMapper.writeValueAsString(validRequest))) // 3. Convertimos el objeto a JSON String

                // --- VERIFICACIONES (Assertions) ---
                .andExpect(status().isCreated()) // Esperamos HTTP 201
                .andExpect(jsonPath("$.name").value("Rutina Mock")) // Verificamos campos del JSON de respuesta
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.difficultyLevel").value("BEGINNER"));
    }
}