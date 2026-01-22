# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen que tiene Maven y Java instalados para compilar
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Establecemos el directorio de trabajo dentro de Linux
WORKDIR /app

# Copiamos primero el archivo de dependencias (para aprovechar la caché de Docker)
COPY pom.xml .
COPY src ./src

# Ejecutamos el comando de Maven para crear el .jar (saltando tests para ir rápido)
RUN mvn clean package -DskipTests

# --- ETAPA 2: EJECUCIÓN (RUN) ---
# Ahora usamos una imagen limpia y ligera solo con Java
FROM eclipse-temurin:21-jdk-alpine

# Creamos un volumen temporal
VOLUME /tmp

# ¡AQUÍ ESTÁ EL TRUCO!: Copiamos el .jar generado en la ETAPA 1 hacia esta etapa final
COPY --from=build /app/target/*.jar app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]