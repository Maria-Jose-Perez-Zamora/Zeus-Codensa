# TechCup Football - Backend API

[![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-336791)](https://www.postgresql.org/)
[![JWT Authentication](https://img.shields.io/badge/Security-JWT-blue)](https://jwt.io/)

Backend robusto y escalable para la plataforma de gestión de torneos de fútbol **TechCup**, desarrollado con Spring Boot bajo arquitectura MVC y patrones de diseño profesionales.

---

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Características Principales](#características-principales)
- [Tecnologías y Dependencias](#tecnologías-y-dependencias)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
- [API Endpoints](#api-endpoints)
- [Diagramas UML](#diagramas-uml)
- [Diagramas de Secuencia](#diagramas-de-secuencia)
- [Requerimientos Funcionales](#requerimientos-funcionales)
- [Base de Datos](#base-de-datos)
- [Seguridad](#seguridad)
- [Testing](#testing)
- [Documentación](#documentación)
- [Licencia](#licencia)

---

## Descripción General

**TechCup Football Backend** es una API REST que gestiona la lógica de negocio completa para la organización y ejecución de torneos de fútbol. Este repositorio contiene la lógica de negocio y la infraestructura del lado del servidor para la plataforma TechCup. En Sprint #1, se ha establecido la arquitectura base, los modelos de dominio y la API funcional para la gestión de usuarios y equipos.

Implementa funcionalidades de:

- Autenticación y autorización basada en roles (JWT)
- Gestión integral de usuarios y equipos
- Administración completa de torneos
- Inscripciones y control de participantes
- Registro y actualización de partidos
- Alineaciones y control disciplinario
- Documentación automática de API (Swagger/OpenAPI)

---

## Características Principales

### Sprint #1 - Funcionalidades Core Implementadas

#### Gestión de Usuarios
- Registro de nuevos usuarios con validación de datos
- Autenticación segura con JWT
- Perfiles de usuario (Jugador, Organizador, Admin)
- Control de duplicados por correo electrónico
- Atributos: Nombre, correo, contraseña (hash simulado), posición, número de dorsal y foto

#### Gestión de Equipos
- Creación de equipos con información (nombre, escudo, colores)
- Asociación de jugadores a equipos
- Consulta y listado de equipos
- Un equipo puede contener múltiples jugadores asociados
- Atributos: Nombre del equipo, escudo, colores representativos y lista de jugadores

#### Gestión de Torneos
- Creación de torneos con estado BORRADOR
- Configuración operativa (reglamento, fechas, horarios, canchas)
- Estados del torneo controlados (flujo validado)
- Consulta de torneos activos e históricos

#### Inscripciones
- Registro de inscripción de equipos
- Seguimiento de pagos/comprobantes
- Cambio de estado de inscripción (pendiente → aprobada/rechazada)
- Auditoría completa del proceso

#### Gestión de Partidos
- Creación de partidos entre equipos
- Actualización de marcadores en tiempo real
- Registro de alineaciones (local y visitante)
- Control disciplinario (tarjetas amarillas y rojas)
- Cierre automático de partidos

---

## Tecnologías y Dependencias

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| **Core** | Spring Boot | 3.3.4 |
| **Lenguaje** | Java | 21 |
| **Seguridad** | Spring Security + JWT | 0.12.3 |
| **Base de Datos** | PostgreSQL + JPA | Latest |
| **Documentación API** | Springdoc OpenAPI | 2.5.0 |
| **Testing** | JUnit 5 + Mockito | Incluido |
| **Cobertura** | JaCoCo | 0.8.12 |
| **Validación** | Jakarta Validation API | Latest |
| **Build Tool** | Maven | 3.9+ |

### Dependencias Principales

```xml
<!-- Spring Boot Starters -->
spring-boot-starter-web              <!-- REST API -->
spring-boot-starter-security         <!-- Autenticación/Autorización -->
spring-boot-starter-data-jpa         <!-- ORM/Persistencia -->
spring-boot-starter-test             <!-- Testing -->

<!-- JWT -->
jjwt-api / jjwt-impl / jjwt-jackson  <!-- JSON Web Tokens -->

<!-- Base de Datos -->
postgresql                           <!-- Driver PostgreSQL -->
jakarta.validation-api               <!-- Validación -->

<!-- Documentación -->
springdoc-openapi-starter-webmvc-ui  <!-- Swagger UI -->

<!-- Análisis de Cobertura -->
jacoco-maven-plugin                  <!-- Métricas de Testing -->
```

---

## Requisitos Previos

Antes de iniciar, asegúrate de tener instalado:

- **Java 21** o superior: `java -version`
- **Maven 3.9+**: `mvn -version`
- **PostgreSQL 12+**: `psql --version`
- **Git**
- **IDE recomendado**: IntelliJ IDEA Community o Professional

---

## Instalación y Configuración

### Clonar el Repositorio

```bash
git clone https://github.com/mariajoseperez01/Zeus-Codensa.git
cd Zeus-Codensa
git checkout develop
```

### Configurar Base de Datos

```sql
CREATE DATABASE techcup_db;
CREATE USER techcup_user WITH PASSWORD 'your_secure_password';
ALTER ROLE techcup_user SET client_encoding TO 'utf8';
GRANT ALL PRIVILEGES ON DATABASE techcup_db TO techcup_user;
```

### Configurar Propiedades de la Aplicación

Editar `src/main/resources/application.properties`:

```properties
# Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/techcup_db
spring.datasource.username=techcup_user
spring.datasource.password=your_secure_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Puerto del servidor
server.port=8080
server.servlet.context-path=/api/v1

# JWT
app.jwt.secret=your_super_secret_key_minimum_256_bits_or_longer_for_security
app.jwt.expiration=86400000

# Logging
logging.level.root=INFO
logging.level.com.zeus.techcup=DEBUG

# Documentación API
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Descargar Dependencias

```bash
mvn clean install
```

### Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

O desde IntelliJ IDEA: Click derecho en la clase main → Run

### Acceder a Swagger UI

```
http://localhost:8080/api/v1/swagger-ui.html
```

---

## Arquitectura del Sistema

### Patrón Arquitectónico: MVC (Modelo-Vista-Controlador)

El sistema ha sido desarrollado bajo el patrón de arquitectura MVC utilizando el framework Spring Boot.

```
┌─────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                 │
│  Controllers (REST Endpoints)                        │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│                 BUSINESS LOGIC LAYER                 │
│  Services + Strategy Pattern + Validations           │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│                 DATA ACCESS LAYER                    │
│  Repositories (JPA) + Entities + DTOs               │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│                DATABASE LAYER                        │
│  PostgreSQL                                          │
└──────────────────────────────────────────────────────┘
```

### Estructura de Carpetas

```
src/main/java/
├── controller/                 # Controladores REST
│   ├── UserController
│   ├── TeamController
│   ├── TournamentController
│   ├── InscriptionController
│   └── MatchController
│
├── core/                       # Lógica de negocio
│   ├── model/                  # Entidades del dominio
│   │   ├── User
│   │   ├── Team
│   │   ├── Tournament
│   │   └── Match
│   ├── service/                # Servicios de negocio
│   │   ├── UserService
│   │   ├── TeamService
│   │   ├── TournamentService
│   │   └── strategy/
│   ├── factory/                # Factory Method
│   ├── exception/              # Excepciones personalizadas
│   └── validator/              # Validadores de dominio
│
├── dependencies/               # Configuración e infraestructura
│   ├── config/                 # Configuración de Spring
│   │   ├── SecurityConfig
│   │   ├── JpaConfig
│   │   └── SwaggerConfig
│   ├── dto/                    # Data Transfer Objects
│   ├── mapper/                 # Mapeos Entity ↔ DTO
│   ├── security/               # Autenticación y JWT
│   └── util/                   # Utilidades generales
```

---

## Patrones de Diseño Implementados

El sistema ha sido desarrollado aplicando patrones de diseño profesionales que reducen acoplamiento, mejoran cohesión y facilitan pruebas.

### 1. Factory Method

**Ubicación**: `core/factory/UserFactory.java`

**Implementación**: En el flujo de registro y creación de usuarios. Se aplica cuando llega la información del usuario y se decide qué tipo de usuario construir según su rol.

**Ventajas**:
- Centraliza la lógica de creación de usuarios con reglas específicas
- Reduce acoplamiento entre la capa de negocio y las clases concretas
- Evita inconsistencias al aplicar siempre las mismas reglas de creación para cada rol
- Facilita extender el sistema sin tocar múltiples módulos cuando aparece un nuevo tipo de usuario
- Mantiene el principio de responsabilidad única

---

### 2. Builder Pattern

**Ubicación**: `dependencies/config/SwaggerConfig.java`

**Implementación**: En la configuración de la documentación de la API. Se usa al construir paso a paso la información general del servicio.

**Ventajas**:
- Mejora la legibilidad del código de configuración
- Construcción flexible y ordenada de objetos complejos
- Reduce el riesgo de constructores largos o inicializaciones confusas
- Disminuye errores de configuración al seguir una estructura ordenada
- Permite evolucionar metadatos sin rehacer todo el bloque

---

### 3. Strategy Pattern

**Ubicación**: `core/service/strategy/MatchmakingStrategy.java`

**Implementación**: En la lógica de generación de llaves eliminatorias del torneo. Se aplica cuando el sistema debe escoger una forma de emparejar equipos.

**Ventajas**:
- Permite intercambiar reglas de emparejamiento sin alterar el flujo principal del torneo
- El problema de emparejamiento no tiene una única regla válida para todos los torneos
- Mejora la extensibilidad: nuevas estrategias se agregan como alternativas
- Aumenta la testabilidad al permitir que cada algoritmo se pruebe de forma aislada

---

### 4. Singleton Pattern

**Ubicación**: Componentes de Spring (Servicios y Configuraciones)

**Implementación**: En los componentes compartidos del backend. Spring Boot gestiona los servicios y controladores como instancias únicas.

**Ventajas**:
- Optimiza memoria al reutilizar una sola instancia
- Mantiene comportamiento consistente evitando múltiples copias
- Simplifica la integración entre capas
- Hace más estable el ciclo de vida de los componentes

---

### 5. Dependency Injection (DI)

**Ubicación**: Todos los servicios y controladores

**Implementación**: Implementada para desacoplar los controladores de la lógica de negocio en los servicios.

**Ventajas**:
- Facilita testing mediante inyección de mocks
- Reduce acoplamiento entre componentes
- Mejora mantenibilidad del código
- Favorece una gestión de dependencias estable y predecible

---

## API Endpoints

### Autenticación (Auth)

| Método | Endpoint | Descripción | Requiere Autenticación |
|--------|----------|-------------|----------------------|
| `POST` | `/auth/login` | Inicio de sesión | No |
| `POST` | `/auth/register` | Registro de nuevo usuario | No |

### Usuarios (Users)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| `GET` | `/users` | Listar todos los usuarios | ADMIN |
| `GET` | `/users/{id}` | Obtener usuario por ID | ADMIN, USER |
| `POST` | `/users` | Crear usuario | ADMIN |
| `PUT` | `/users/{id}` | Actualizar usuario | ADMIN, OWNER |

### Equipos (Teams)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| `GET` | `/teams` | Listar todos los equipos | PUBLIC |
| `GET` | `/teams/{id}` | Obtener equipo por ID | PUBLIC |
| `POST` | `/teams` | Crear equipo | ORGANIZER, ADMIN |
| `PUT` | `/teams/{id}` | Actualizar equipo | ORGANIZER, ADMIN |

### Torneos (Tournaments)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| `GET` | `/tournaments` | Listar todos los torneos | PUBLIC |
| `POST` | `/tournaments` | Crear torneo | ORGANIZER, ADMIN |
| `PUT` | `/tournaments/{id}/config` | Configurar torneo | ORGANIZER, ADMIN |

### Inscripciones (Inscriptions)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| `POST` | `/inscriptions` | Registrar inscripción | ORGANIZER |
| `GET` | `/inscriptions` | Listar inscripciones | ORGANIZER, ADMIN |
| `PUT` | `/inscriptions/{id}/status` | Cambiar estado | ORGANIZER, ADMIN |

### Partidos (Matches)

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| `POST` | `/matches` | Crear partido | ORGANIZER, ADMIN |
| `GET` | `/matches/{id}` | Obtener partido | PUBLIC |
| `PUT` | `/matches/{id}/score` | Actualizar marcador | ORGANIZER, ADMIN |
| `POST` | `/matches/{id}/lineup` | Registrar alineación | ORGANIZER, ADMIN |
| `POST` | `/matches/{id}/cards` | Registrar tarjetas | ORGANIZER, ADMIN |

### Documentación Interactiva

- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v1/v3/api-docs`

---

## Diagramas UML

### Diagrama de Clases

El diagrama de clases muestra la estructura completa del dominio, incluyendo todas las entidades, sus atributos y relaciones:

![diagrama de clases1.1.png](docs%2Fimages%2Fdiagrama%20de%20clases1.1.png)

---

## Diagramas de Secuencia

Los siguientes diagramas documentan el flujo de interacción entre componentes del sistema para cada funcionalidad implementada en Sprint #1.

### 1. Inicio de Sesión (Login)

Permite que un usuario ingrese al sistema usando correo y contraseña. El servicio verifica que las credenciales existan y coincidan con un usuario registrado; si son correctas, devuelve un token junto con su rol para controlar permisos en las demás operaciones.

---

### 2. Registro de Usuario

Registra nuevos usuarios del sistema (por ejemplo, organizadores o administradores) validando campos obligatorios y reglas del dominio. Si la información es válida, el usuario se transforma a entidad, se almacena en memoria y se retorna su representación de respuesta.


---

### 3. Consulta de Usuarios

Obtiene el listado completo de usuarios creados en la aplicación para tareas de administración y seguimiento. La consulta toma los datos almacenados, los transforma a DTO y los entrega en un formato seguro para el cliente.


---

### 4. Creación de Equipo

Permite crear un equipo del torneo con su información principal (nombre, escudo y colores) y asociar jugadores existentes usando sus correos. Durante el proceso se valida la solicitud y se construye el equipo con su plantilla inicial.


---

### 5. Consulta de Equipos

Retorna todos los equipos registrados para facilitar la visualización de participantes del torneo. La respuesta incluye los datos relevantes del equipo y su estado actual dentro de la información disponible en memoria.


---

### 6. Creación de Torneo

Inicia un nuevo torneo con su configuración base y lo deja en estado BORRADOR para que pueda ser completado posteriormente. Esta funcionalidad centraliza la creación inicial de la competencia antes de abrir inscripciones o programar partidos.

[Diagrama de Secuencia 6 - Insertar aquí]

---

### 7. Configuración de Torneo

Actualiza la información operativa de un torneo existente, como reglamento, fechas, horarios, canchas y sanciones. Solo permite cambios cuando el torneo se encuentra en estados válidos, evitando modificaciones fuera del flujo definido por negocio.


---

### 8. Consulta de Torneos

Consulta todos los torneos creados en el sistema para mostrar su información general y estado. Sirve como base para paneles de administración, seguimiento de competencia y selección de torneo en otros procesos.


---

### 9. Registro de Inscripción

Registra la inscripción de un equipo al torneo incluyendo datos del pago o comprobante. El sistema valida la solicitud, crea el registro de inscripción y deja trazabilidad del proceso para su posterior revisión por el organizador.


---

### 10. Actualización de Estado de Inscripción

Permite que el organizador cambie el estado de una inscripción (por ejemplo, en revisión, aprobada o rechazada) de acuerdo con el flujo permitido. También valida que el nuevo estado sea correcto y que la inscripción exista antes de aplicar el cambio.


---

### 11. Consulta de Inscripciones

Muestra todas las inscripciones registradas junto con su estado actual para facilitar control administrativo y toma de decisiones. Esta vista permite identificar rápidamente qué equipos están pendientes, aprobados o rechazados.


---

### 12. Registro de Partido

Crea un partido entre dos equipos dentro del contexto de un torneo, guardando la información inicial necesaria para su gestión. Incluye validaciones de consistencia para asegurar que el encuentro quede correctamente preparado para etapas posteriores.


---

### 13. Actualización de Marcador

Actualiza los goles o puntos de un partido y consolida el resultado final del encuentro. Además, valida que los marcadores sean válidos y cambia el estado del partido a FINALIZADO cuando corresponde.


---

### 14. Registro de Alineación

Registra los jugadores que participarán en un partido para un equipo específico (local o visitante). La funcionalidad valida que el equipo pertenezca al encuentro y que la lista de jugadores cumpla condiciones mínimas antes de guardarla.


---

### 15. Registro de Tarjetas

Permite registrar eventos disciplinarios de un partido, como tarjetas amarillas o rojas asociadas a un jugador. Este control aporta trazabilidad deportiva y soporta posteriores decisiones arbitrales o sancionatorias.


---

## Requerimientos Funcionales

### RF1: Registro y Perfil de Jugador

Se implementó la entidad User que permite capturar la información técnica de los jugadores.

- **Atributos**: Nombre, correo, contraseña (hash simulado), posición, número de dorsal y foto
- **Validación de Negocio**: El UserService garantiza que no existan registros con correos duplicados antes de añadirlos a la memoria

### RF2: Gestión de Equipos

Se implementó la entidad Team para la organización del torneo.

- **Atributos**: Nombre del equipo, escudo, colores representativos y lista de jugadores asociados
- **Relación**: Un equipo puede contener múltiples objetos de tipo User (Jugadores)

### Validaciones Implementadas

- Inyección de Dependencias (DI): Implementada para desacoplar los controladores de la lógica de negocio
- POJOs / Entities: Clases planas para la representación fiel de los requerimientos del negocio
- Validación de entrada en todos los endpoints

---

## Base de Datos

### Diagrama Entidad-Relación (ER)

```
┌──────────────┐         ┌──────────────┐
│    Users     │         │    Teams     │
├──────────────┤         ├──────────────┤
│ id (PK)      │◄────────│ id (PK)      │
│ email (UQ)   │   N:M   │ name         │
│ password     ├────────►│ shield       │
│ name         │         │ colors       │
│ role         │         └──────────────┘
│ position     │
│ jersey_num   │
│ photo_url    │
└──────────────┘

┌──────────────┐         ┌──────────────┐
│ Tournaments  │         │ Inscriptions │
├──────────────┤         ├──────────────┤
│ id (PK)      │◄────────│ id (PK)      │
│ name         │  1:N    │ tournament_id│
│ description  ├────────►│ team_id      │
│ status       │         │ proof_url    │
│ rules        │         │ status       │
│ start_date   │         │ created_at   │
└──────────────┘         └──────────────┘

┌──────────────┐         ┌──────────────┐
│   Matches    │         │   Lineups    │
├──────────────┤         ├──────────────┤
│ id (PK)      │◄────────│ id (PK)      │
│ tournament_id│  1:N    │ match_id     │
│ home_team_id ├────────►│ team_id      │
│ away_team_id │         │ players_ids  │
│ home_score   │         │ created_at   │
│ away_score   │         └──────────────┘
│ status       │
└──────────────┘

┌──────────────┐
│    Cards     │
├──────────────┤
│ id (PK)      │
│ match_id (FK)│
│ player_id(FK)│
│ card_type    │
│ minute       │
└──────────────┘
```

---

## Seguridad

### Autenticación con JWT

El sistema implementa autenticación basada en JWT (JSON Web Tokens) con expiración configurable.

**Flow de Autenticación**:

```
1. Cliente envía credenciales
   POST /auth/login
   {
     "email": "user@example.com",
     "password": "password123"
   }

2. Servidor valida y genera JWT
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "expiresIn": 86400,
     "role": "PLAYER"
   }

3. Cliente incluye JWT en headers
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

4. Servidor valida JWT en cada request
```

### Roles y Permisos

| Rol | Descripción | Acceso |
|-----|-------------|--------|
| **ADMIN** | Administrador del sistema | Acceso total a todos los recursos |
| **ORGANIZER** | Organizador de torneos | Crear/editar torneos, inscripciones y partidos |
| **PLAYER** | Jugador participante | Ver torneos, registrar alineación, consultar partidos |

### Buenas Prácticas de Seguridad

- Contraseñas hasheadas con BCrypt
- JWT con expiración configurable
- Validación de entrada en todos los endpoints
- CORS configurado restrictivamente
- SQL Injection prevenido con JPA/Parameterized Queries
- Contraseña y credenciales nunca se exponen en respuestas

---

## Testing

### Estructura de Tests

```
src/test/java/
├── controller/
│   ├── UserControllerTest
│   ├── TeamControllerTest
│   └── TournamentControllerTest
├── core/
│   ├── service/
│   │   ├── UserServiceTest
│   │   ├── TeamServiceTest
│   │   └── TournamentServiceTest
│   ├── factory/
│   │   └── UserFactoryTest
│   └── validator/
│       └── UserValidatorTest
└── dependencies/
    ├── mapper/
    │   ├── UserMapperTest
    │   └── TeamMapperTest
    └── security/
        └── JwtUtilTest
```

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests específicos
mvn test -Dtest=UserServiceTest

# Con reporte de cobertura
mvn test jacoco:report
```

### Ejemplo de Test Unitario

```java
@SpringBootTest
class UserServiceTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testCreateUserSuccess() {
        // Arrange
        UserDTO userDTO = new UserDTO("John", "john@example.com", "pass123");
        User user = new User("John", "john@example.com", "pass123");
        
        when(userRepository.save(any())).thenReturn(user);
        
        // Act
        User result = userService.createUser(userDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(userRepository, times(1)).save(any());
    }
}
```

---

## Documentación

### Documentación Interactiva (Swagger)

Acceder a la interfaz Swagger en: `http://localhost:8080/api/v1/swagger-ui.html`

Descargar especificación OpenAPI: `http://localhost:8080/api/v1/v3/api-docs`

### Archivos de Prueba

- **Archivo**: `pruebas.http`
- **Uso**: Ejecutar directamente desde IntelliJ IDEA para validar el flujo End-to-End (Controlador → Servicio → Memoria)

---

## Licencia

Este proyecto está bajo licencia **MIT** - Ver archivo [LICENSE](LICENSE) para detalles.

```
MIT License

Copyright (c) 2024 TechCup Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

---

## Soporte y Contacto

- **Issues**: Reporta bugs en [GitHub Issues](https://github.com/mariajoseperez01/Zeus-Codensa/issues)
- **Discussions**: Únete a [GitHub Discussions](https://github.com/mariajoseperez01/Zeus-Codensa/discussions)
- **Email**: proximamente

---

## Créditos

Desarrollado con dedicación por el equipo **Zeus-Codensa** para **TechCup Football Platform**.

**Status**: En Desarrollo (Sprint #3)