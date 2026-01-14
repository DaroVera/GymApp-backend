package com.gymapp.backend.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivar CSRF (no necesario en APIs REST stateless con JWT)
                .csrf(csrf -> csrf.disable())

                // 2. Configurar permisos de rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas PÚBLICAS (Login, Registro) - Ajusta esto según tus controllers
                        .requestMatchers("/api/auth/**").permitAll()
                        //.requestMatchers("/api/users/**").permitAll() // Descomenta si usas esta ruta temporalmente

                        // Rutas SOLO para ADMIN (Nueva Regla)
                        // Spring asume el prefijo "ROLE_", así que si en la BD es "ADMIN", aquí ponemos "ADMIN"
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                        //  Zona TRAINER (Pueden entrar TRAINERS y ADMINS)
                        .requestMatchers("/api/trainer/**").hasAnyAuthority("TRAINER", "ADMIN")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )

                // 3. Configurar gestión de sesiones (Stateless)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Agregar nuestro proveedor de autenticación
                .authenticationProvider(authenticationProvider)

                // 5. Agregar nuestro filtro JWT antes del filtro de usuario/password por defecto
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
