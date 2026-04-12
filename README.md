#  TechCup Football - Backend API

[![Build](https://img.shields.io/badge/build-passing-brightgreen)](#)
[![Coverage](https://img.shields.io/badge/coverage-85%25-success)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Security](https://img.shields.io/badge/Security-JWT%20%2B%20OAuth2-blue)](#)

Backend RESTful para la gestión integral de torneos de fútbol de **TechCup**, desarrollado con **Spring Boot**, **PostgreSQL**, **JWT**, **OAuth2** y despliegue containerizado con **Docker**...

---

#  Tabla de Contenido
- [Arquitectura](#-arquitectura)
- [Diagrama de Contexto](#-diagrama-de-contexto)
- [Ejecución Local](#-ejecución-local)
- [Ejecución con Docker](#-ejecución-con-docker)
- [Seguridad](#-seguridad-jwt--oauth2)
- [Testing](#-testing)
- [Swagger](#-swagger)
- [Ambientes Desplegados](#-ambientes-desplegados)

---

# Arquitectura

El backend sigue una arquitectura basada en capas:

- **Controller Layer** → Exposición de endpoints REST
- **Service Layer** → Reglas de negocio
- **Repository Layer** → Persistencia JPA
- **Security Layer** → JWT + OAuth2
- **Infrastructure Layer** → PostgreSQL + Docker

---

#  Diagrama de Contexto






---

#  Ejecución Local

## 1) Clonar repositorio
```bash
git clone https://github.com/mariajoseperez01/Zeus-Codensa.git
cd Zeus-Codensa
git checkout develop
```

## 2) Configurar base de datos
```sql
CREATE DATABASE techcup_db;
```

## 3) Configurar variables
Editar:

```properties
src/main/resources/application.properties
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/techcup_db
spring.datasource.username=postgres
spring.datasource.password=postgres
server.port=8080
```

## 4) Instalar dependencias
```bash
mvn clean install
```

## 5) Ejecutar proyecto
```bash
mvn spring-boot:run
```

Aplicación disponible en:

```bash
http://localhost:8080
```

---

#  Ejecución con Docker

## Construir imagen
```bash
docker build -t techcup-backend .
```

## Ejecutar contenedor
```bash
docker run -p 8080:8080 techcup-backend
```

## Docker Compose
```bash
docker-compose up --build
```

---

# 🔐 Seguridad JWT + OAuth2

La seguridad del backend implementa doble estrategia:

## JWT Authentication
1. Usuario inicia sesión en `/auth/login`
2. El sistema genera un **JWT firmado**
3. El cliente envía:
```http
Authorization: Bearer <token>
```
4. Spring Security valida firma y expiración

## OAuth2 Flow
Se soporta autenticación externa mediante proveedores OAuth2:

- Google
- GitHub
- Microsoft

### Flujo
1. Usuario autenticado con proveedor externo
2. OAuth2 devuelve access token
3. Backend valida identidad
4. Se genera JWT interno para sesión API

## Roles
- `ADMIN`
- `ORGANIZER`
- `PLAYER`

---

#  Testing

Ejecutar pruebas:

```bash
mvn test
```

Cobertura:

```bash
mvn test jacoco:report
```

Reporte JaCoCo:

```bash
target/site/jacoco/index.html
```

---

#  Swagger

Swagger UI disponible en:

```bash
http://localhost:8080/swagger-ui.html
```

---

#  Ambientes Desplegados

## QA
```bash
https://qa-api.techcup.com
```

## Producción
```bash
https://api.techcup.com
```

---

# 👥 Equipo

Desarrollado por **Zeus-Codensa Team**  
Proyecto académico **TechCup Football Platform**