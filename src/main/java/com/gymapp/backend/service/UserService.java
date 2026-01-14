package com.gymapp.backend.service;

import com.gymapp.backend.model.dto.ChangePasswordDTO;
import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.dto.UserResponseDTO;
import com.gymapp.backend.model.dto.UserUpdateDTO;
import com.gymapp.backend.model.entity.User;

public interface UserService {


        UserResponseDTO getUserProfile();

    UserResponseDTO updateUserProfile(UserUpdateDTO updateData);

    // Método void porque no necesitamos devolver el usuario, solo confirmar que se hizo.
    void changePassword(ChangePasswordDTO request);

}
