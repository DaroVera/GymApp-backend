
# 1. IMAGEN BASE: Usamos una versión ligera de Linux (Alpine) que ya tiene Java 21 instalado
FROM eclipse-temurin:21-jdk-alpine

# 2. VOLUMEN: Creamos un punto de montaje temporal (opcional, pero buena práctica en Spring Boot)
VOLUME /tmp

# 3. COPIAR: Agarramos el .jar que creaste en target y lo metemos en la imagen con nombre "app.jar"
# Asegúrate de que el asterisco (*) coincida con tu archivo generado
COPY target/*.jar app.jar

# 4. PUNTO DE ENTRADA: El comando que se ejecutará cuando arranque el contenedor
ENTRYPOINT ["java","-jar","/app.jar"]