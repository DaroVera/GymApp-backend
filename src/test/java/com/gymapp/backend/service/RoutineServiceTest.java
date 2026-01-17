package com.gymapp.backend.service;

import com.gymapp.backend.model.dto.RoutineCreateDTO;
import com.gymapp.backend.model.dto.RoutineExerciseDTO;
import com.gymapp.backend.model.entity.Exercise;
import com.gymapp.backend.model.entity.Routine;
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.repository.ExerciseRepository;
import com.gymapp.backend.repository.RoutineRepository;
import com.gymapp.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// 1. @ExtendWith: Le dice a JUnit 5 que use Mockito para procesar las anotaciones
@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    // 2. @Mock: Crea simulacros (falsos) de las dependencias.
    // No son los repositorios reales, son objetos vacíos que nosotros controlamos.
    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private UserRepository userRepository;

    // Mocks para simular la seguridad (SecurityContext)
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    // 3. @InjectMocks: Crea una instancia REAL de RoutineService e inyecta los @Mock dentro de ella.
    @InjectMocks
    private RoutineService routineService;

    // Datos de prueba reutilizables
    private User mockUser;
    private Exercise mockExercise;

    @BeforeEach
    void setUp() {
        // Preparamos datos básicos antes de cada test
        mockUser = User.builder().id(1L).email("test@gym.com").build();
        mockExercise = Exercise.builder().id(10L).name("Press Banca").build();

        // Simular el Login (SecurityContextHolder es estático y un poco tramposo de testear)
        // Esta configuración simula que hay un usuario logueado con el email "test@gym.com"
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createRoutine_WhenDataIsValid_ShouldSaveAndReturnRoutine() {
        // --- GIVEN (Dado que...) ---
        // Preparamos el escenario.

        // 1. Configuramos el comportamiento de los Mocks
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gym.com");
        when(userRepository.findByEmail("test@gym.com")).thenReturn(Optional.of(mockUser));
        when(exerciseRepository.findById(10L)).thenReturn(Optional.of(mockExercise));

        // Cuando el repo guarde cualquier rutina, devuelve esa misma rutina pero con ID 1
        when(routineRepository.save(any(Routine.class))).thenAnswer(invocation -> {
            Routine r = invocation.getArgument(0);
            r.setId(1L); // Simulamos que la BD le asignó ID 1
            return r;
        });

        // 2. Preparamos el DTO de entrada (lo que enviaría el usuario)
        RoutineExerciseDTO exDto = new RoutineExerciseDTO();
        exDto.setExerciseId(10L);
        exDto.setSets(4);
        exDto.setReps(10);
        exDto.setRestTime(60);

        RoutineCreateDTO request = new RoutineCreateDTO();
        request.setName("Rutina Test");
        request.setDifficultyLevel("BEGINNER");
        request.setExercises(List.of(exDto));

        // --- WHEN (Cuando...) ---
        // Ejecutamos el método real que queremos probar
        Routine result = routineService.createRoutine(request);

        // --- THEN (Entonces...) ---
        // Verificamos que el resultado sea el esperado

        assertNotNull(result); // No debe ser null
        assertEquals(1L, result.getId()); // Debe tener el ID simulado
        assertEquals("Rutina Test", result.getName());
        assertEquals(1, result.getRoutineExercises().size()); // Debe tener 1 ejercicio

        // Verificamos que se haya llamado al método save del repositorio 1 vez
        verify(routineRepository, times(1)).save(any(Routine.class));
    }
}