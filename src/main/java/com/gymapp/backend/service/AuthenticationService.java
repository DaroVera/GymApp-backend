package com.gymapp.backend.service;

import com.gymapp.backend.model.dto.AuthResponse;
import com.gymapp.backend.model.dto.LoginRequest;
import com.gymapp.backend.model.dto.UserRegisterDTO;
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

    // REGISTRO (Devuelve Token directo)
    public AuthResponse register(UserRegisterDTO request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // ¡Encriptamos la password!
                .role(Role.USER) // Por defecto todos son USER
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    // LOGIN
    public AuthResponse login(LoginRequest request) {
        // 1. Este método hace el trabajo sucio: verifica usuario y contraseña.
        // Si falla, lanza una excepción automáticamente.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Si llegamos aquí, las credenciales son correctas. Buscamos al usuario para generar el token.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
