package com.gymapp.backend.model.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {

    private String message;
    private String error; // Ej: "Not Found", "Bad Request"
    private int status;   // Ej: 404, 400
    private LocalDateTime timestamp;
}
