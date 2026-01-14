package com.gymapp.backend.controller;

import com.gymapp.backend.model.dto.UserResponseDTO;
import com.gymapp.backend.model.dto.UserUpdateDTO;
import com.gymapp.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // Inyectamos el servicio

    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("¡Hola! 👋 Si estás viendo esto, es porque tu Token es válido y seguro.");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> myProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateProfile(@Valid @RequestBody UserUpdateDTO request) {
        return ResponseEntity.ok(userService.updateUserProfile(request));
    }
}
