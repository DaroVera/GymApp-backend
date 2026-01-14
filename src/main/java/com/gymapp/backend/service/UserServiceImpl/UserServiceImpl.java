package com.gymapp.backend.service.UserServiceImpl;

import com.gymapp.backend.model.dto.ChangePasswordDTO;
import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.dto.UserResponseDTO;
import com.gymapp.backend.model.dto.UserUpdateDTO;
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.model.enums.Role;
import com.gymapp.backend.repository.UserRepository;
import com.gymapp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder; // <--- INYECCIÓN NUEVA

    @Override
    public UserResponseDTO getUserProfile() {
        // 1. Obtener la autenticación actual del "Contexto de Seguridad"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Extraer el email (o username) del objeto de autenticación
        // Spring Security guarda el principal (usuario) ahí.
        String email = authentication.getName();

        // 3. Buscar al usuario en la BD
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado (Error de integridad)"));

        // 4. Mapear a DTO (Para no devolver la password)
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public UserResponseDTO updateUserProfile(UserUpdateDTO updateData) {
        // 1. Obtener la identidad del Token (Contexto de Seguridad)
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscar al usuario en la BD
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Actualizar SOLO los campos permitidos
        // Aquí es donde ignoramos cualquier otra cosa que manden.
        user.setFirstName(updateData.getFirstName());
        user.setLastName(updateData.getLastName());

        // 4. Guardar cambios
        User updatedUser = userRepository.save(user);

        // 5. Convertir a DTO de respuesta (sin password)
        return mapToUserResponse(updatedUser);
    }

    // TIP: Es buena práctica extraer el mapeo a un método privado para no repetir código
    private UserResponseDTO mapToUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordDTO request) {
        // 1. Obtener usuario autenticado
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. VERIFICACIÓN: ¿La contraseña actual coincide con la de la BD?
        // passwordEncoder.matches(textoPlano, hashEncriptado)
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("La contraseña actual es incorrecta");
        }

        // 3. Validar que la nueva no sea igual a la vieja (Opcional, pero buena práctica)
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalStateException("La nueva contraseña debe ser diferente a la actual");
        }

        // 4. Encriptar la nueva contraseña y guardarla
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

}
