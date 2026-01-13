package com.gymapp.backend.service.UserServiceImpl;

import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.dto.UserResponseDTO;
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.model.enums.Role;
import com.gymapp.backend.repository.UserRepository;
import com.gymapp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

}
