package com.gymapp.backend.service;

import com.gymapp.backend.model.dto.AuthResponse;
import com.gymapp.backend.model.dto.LoginRequest;
import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.dto.UserResponseDTO; // Importamos el DTO de Usuario
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.model.enums.Role;
import com.gymapp.backend.repository.UserRepository;
import com.gymapp.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // REGISTRO
    public AuthResponse register(UserRegisterDTO request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .user(mapToUserDto(user)) // <--- Agregamos los datos del usuario
                .build();
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .user(mapToUserDto(user)) // <--- Agregamos los datos del usuario
                .build();
    }

    // Método auxiliar para convertir Entidad -> DTO
    // Esto evita repetir código y mantiene limpio el servicio
    private UserResponseDTO mapToUserDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().name()) // Convertimos el Enum a String
                .build();
    }
}