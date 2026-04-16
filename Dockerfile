# ETAPA 1: Build (Maven)
# Usamos una imagen de Maven para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Runtime (JRE)
# Usamos una imagen ligera solo con el JRE para que pese < 300MB
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Parametrización de variables críticas
ENV SPRING_PROFILES_ACTIVE=dev
ENV DB_HOST=db
ENV DB_PORT=5432
ENV SERVER_PORT=8080

# Solo copiamos el jar que acaba en .jar, si hay muchos Docker creará una carpeta, 
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]