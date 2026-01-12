package com.gymapp.backend.service.UserServiceImpl;

import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.model.enums.Role;
import com.gymapp.backend.repository.UserRepository;
import com.gymapp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserRegisterDTO registerDTO) {

        // 3. Validación de Negocio: ¿El email ya existe?
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // 4. Mapeo: Convertir DTO a Entidad
        // (Nota: Más adelante usaremos librerías como MapStruct, pero hacerlo a mano es bueno para aprender)
        User user = User.builder()
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword()) // OJO: Aquí va la contraseña en texto plano POR AHORA.
                .role(Role.USER) // Por defecto, todos son USER
                .build();

        // 5. Guardar en BD
        return userRepository.save(user);
    }
}
