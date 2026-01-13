package com.gymapp.backend.service;

import com.gymapp.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;



    // Este es el ÚNICO método que nos obliga a tener la interfaz.
    // Spring Security llamará a este método automáticamente cuando alguien intente loguearse.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // La lógica es simple:
        // "Busca en el repositorio por email. Si no está, lanza error."
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + username + " no existe"));
    }
}
