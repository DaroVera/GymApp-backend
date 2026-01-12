package com.gymapp.backend.controller;

import com.gymapp.backend.model.dto.UserRegisterDTO;
import com.gymapp.backend.model.entity.User;
import com.gymapp.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Endpoint: POST /api/users/register
     @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
         // @Valid: Activa las validaciones del DTO (@NotBlank, @Email)
         // @RequestBody: Convierte el JSON que envías en el objeto Java

         User newUser = userService.createUser(userRegisterDTO);

         //Devolvemos el usuario creado y el código 201 (CREATED)
         return new ResponseEntity<>(newUser, HttpStatus.CREATED);
     }
}
