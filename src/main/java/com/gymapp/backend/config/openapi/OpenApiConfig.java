package com.gymapp.backend.config.openapi;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GymApp Backend API",
                version = "1.0",
                description = "Documentación oficial de la API para gestión de gimnasio.",
                contact = @Contact(
                        name = "Tu Nombre",
                        email = "tuemail@ejemplo.com"
                ),
                license = @License(
                        name = "Standard License",
                        url = "http://gymapp.com"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor Local",
                        url = "http://localhost:8080"
                )
        },
        // Esto aplica la seguridad (el candado) a TODOS los endpoints por defecto
        security = @SecurityRequirement(name = "bearerAuth")
)
// Esta anotación define CÓMO es la seguridad (Token JWT)
@SecurityScheme(
        name = "bearerAuth",
        description = "Autenticación JWT. Ingrese el token en el formato: Bearer {token}",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
