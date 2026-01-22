# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen que tiene Maven y Java instalado
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el pom.xml y el código fuente
COPY pom.xml .
COPY src ./src

# Ejecutamos el empaquetado (igual que hacías en tu PC)
# -DskipTests para ahorrar tiempo en el despliegue
RUN mvn clean package -DskipTests

# --- ETAPA 2: EJECUCIÓN (RUN) ---
# Usamos la imagen ligera solo con Java (igual que antes)
FROM eclipse-temurin:21-jdk-alpine

# Creamos el volumen temporal
VOLUME /tmp

# AQUÍ ESTÁ LA MAGIA: Copiamos el .jar generado en la ETAPA 1 (build)
# hacia esta nueva etapa limpia.
COPY --from=build /app/target/*.jar app.jar

# Ejecutamos la app
ENTRYPOINT ["java","-jar","/app.jar"]