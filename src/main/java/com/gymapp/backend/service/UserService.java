package com.gymapp.backend.service;

import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.entity.User;

public interface UserService {

    // Definimos el contrato: "Quiero registrar un usuario recibiendo un DTO y devolviendo la Entidad creada"
    User createUser(UserRegisterDTO userRegisterDTO);
}
