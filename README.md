

# TechCup Football - Backend API

[![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-336791)](https://www.postgresql.org/)
[![JWT Authentication](https://img.shields.io/badge/Security-JWT-blue)](https://jwt.io/)
[![GitHub Actions](https://img.shields.io/badge/Automation-GitHub_Actions-2088FF?logo=github-actions&logoColor=white)](https://github.com/features/actions)
[![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![Azure](https://img.shields.io/badge/Cloud-Azure-0078D4?logo=microsoftazure&logoColor=white)](https://azure.microsoft.com/)
[![Azure DevOps](https://img.shields.io/badge/CI/CD-Azure_DevOps-0078D7?logo=azuredevops&logoColor=white)](https://azure.microsoft.com/products/devops)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)

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
- [Requerimientos Funcionales](#requerimientos-funcionales)
- [Reglas de Negocio](#reglas-de-negocio)
- [Campos de Entrada](#campos-de-entrada)
- [Excepciones y Manejo de Errores](#excepciones-y-manejo-de-errores)
- [Diagramas UML](#diagramas-uml)
- [Diagramas de Secuencia](#diagramas-de-secuencia)
- [Base de Datos](#base-de-datos)
- [Seguridad](#seguridad)
- [Testing](#testing)
- [Documentación](#documentación)
- [Licencia](#licencia)


### Archivos
- [manual_identidad.md](docs/design/manual_identidad.md)
- [Arquitectura_Backend_TechCup_Football.docx](docs/requirements/Arquitectura_Backend_TechCup_Football.docx)
- [Arquitectura_Backend_TechCup_Football.pdf](docs/requirements/Arquitectura_Backend_TechCup_Football.pdf)
- [jira_test_scenarios.md](docs/requirements/jira_test_scenarios.md)
- [requirements.md](docs/requirements/requirements.md)
- [DocumentoDeArquitectura.md](DocumentoDeArquitectura.md)




---

## Descripción General

**TechCup Football Backend** es una API REST que gestiona la lógica de negocio completa para la organización y ejecución de torneos de fútbol. Este repositorio contiene la lógica de negocio y la infraestructura del lado del servidor para la plataforma TechCup.

Implementa funcionalidades de:

- Autenticación y autorización basada en roles (JWT)
- Gestión integral de usuarios y equipos
- Administración completa de torneos
- Inscripciones y control de participantes
- Registro y actualización de partidos
- Alineaciones y control disciplinario
- Documentación automática de API (Swagger/OpenAPI).

---

## Características Principales

### Sprint #1 - Funcionalidades Core Implementadas

#### Gestión de Usuarios
- Registro de nuevos usuarios con validación de datos
- Autenticación segura con JWT
- Perfiles de usuario (Jugador, Organizador, Admin)
- Control de duplicados por correo electrónico
- Validación de dominios aceptados

#### Gestión de Equipos
- Creación de equipos con información (nombre, escudo, colores)
- Asociación de jugadores a equipos mediante invitaciones
- Consulta y listado de equipos
- Códigos únicos para identificación de equipos

#### Gestión de Torneos
- Creación de torneos con estado BORRADOR
- Configuración operativa (reglamento, fechas, horarios, canchas)
- Estados del torneo controlados (flujo validado)
- Consulta de torneos activos e históricos

#### Inscripciones
- Registro de inscripción de equipos con comprobante de pago
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

Configura variables de entorno (el proyecto usa `src/main/resources/application.yaml`):

```bash
# Base de datos
DB_URL=jdbc:postgresql://localhost:5432/techcuo_db
DB_USER=techcuo_user
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your_super_secret_key_minimum_256_bits_or_longer_for_security
JWT_TTL_MS=3600000

# OAuth2 Google
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
OAUTH2_SUCCESS_REDIRECT_URL=http://localhost:3000/dashboard

# SSL / servidor
PORT=8443
KEY_STORE_PASSWORD=password
```

Notas de configuración reales:

- El backend corre por defecto en `https://localhost:8443`.
- SSL está habilitado (`server.ssl.enabled=true`).
- No se usa `server.servlet.context-path`, por lo que las rutas API inician en `/api/...`.

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
https://localhost:8443/swagger-ui/index.html
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
src/main/java/com/zeuscodensa/techcupfutbol/
├── config/                     # Configuración (Security, CORS, OpenAPI)
├── controller/
│   ├── api/                    # Endpoints REST
│   ├── dto/                    # DTOs de entrada/salida
│   ├── handler/                # Manejador global de excepciones
│   └── mapper/                 # Mapeos DTO <-> dominio
├── core/
│   ├── model/                  # Entidades de dominio
│   ├── repository/             # Interfaces de repositorio de dominio
│   ├── service/                # Servicios de negocio
│   ├── strategy/               # Estrategias (emparejamiento, etc.)
│   ├── validator/              # Validaciones de reglas
│   └── enums/                  # Enumeraciones de dominio
├── persistence/
│   ├── entity/                 # Entidades JPA
│   ├── repository/             # Repositorios Spring Data
│   └── mapper/                 # Mappers persistencia <-> dominio
├── security/                   # JWT, OAuth2 handlers y utilidades
└── TechcupFutbolApplication.java
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

**Ubicación**: `config/SwaggerConfig.java`

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

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth` | Inicio de sesión (JWT) |
| GET | `/api/auth/google/start` | Inicio de flujo OAuth2 con Google |

### Usuarios (Users)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/users` | Crear usuario |
| GET | `/api/users` | Listar usuarios |

### Equipos (Teams)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/teams` | Crear equipo |
| GET | `/api/teams` | Listar equipos |

### Torneos (Tournaments)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/tournaments` | Crear torneo |
| PUT | `/api/tournaments/{id}` | Configurar torneo |
| GET | `/api/tournaments/query/all` | Historial/listado de torneos |
| GET | `/api/tournaments/query/{id}` | Detalle de torneo |

### Consultas de Torneo

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tournaments/query` | Endpoint base de consultas |
| GET | `/api/tournaments/query/{tournament}/standings` | Tabla de posiciones |
| GET | `/api/tournaments/query/{tournament}/brackets/{phase}` | Llaves eliminatorias |
| GET | `/api/tournaments/query/{tournament}/calendar` | Calendario de partidos |
| GET | `/api/tournaments/query/{tournament}/results` | Resultados históricos |
| GET | `/api/tournaments/query/{tournament}/statistics` | Estadísticas globales |
| GET | `/api/tournaments/query/{tournament}/scorers` | Tabla de goleadores |
| GET | `/api/tournaments/query/{tournament}/history/{team}` | Historial por equipo |

### Inscripciones (Inscriptions)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/registrations` | Registrar inscripción |
| PUT | `/api/registrations/{id}/status` | Cambiar estado de inscripción |
| GET | `/api/registrations` | Listar inscripciones |

### Jugadores e Invitaciones

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/players/available` | Buscar jugadores disponibles |
| POST | `/api/players/invitations` | Enviar invitación |
| PATCH | `/api/players/invitations/{id}/acceptance?playerEmail=...` | Aceptar/Rechazar invitación |

### Partidos (Matches)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/matches` | Crear partido |
| PUT | `/api/matches/{id}/score` | Actualizar marcador |
| PUT | `/api/matches/{id}/lineup` | Registrar alineación |
| PUT | `/api/matches/{id}/cards` | Registrar tarjetas |
| PUT | `/api/matches/{id}/referee` | Asignar árbitro |
| GET | `/api/matches/referee/{refereeEmail}` | Obtener partidos por árbitro |
| GET | `/api/matches` | Listar partidos |

### Salud del Servicio

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/health` | Health check público (app + conectividad DB) |

### Documentación Interactiva

- **Swagger UI**: `https://localhost:8443/swagger-ui/index.html`
- **OpenAPI JSON**: `https://localhost:8443/v3/api-docs`

---

## Requerimientos Funcionales

### RF1: Registro de Usuario

El sistema permite que nuevos usuarios se registren en la plataforma proporcionando su información personal y de contacto. El registro valida que el correo sea único dentro del sistema y que cumple con los dominios aceptados. El usuario puede seleccionar su rol durante el registro.

**Flujo Principal:**
1. El usuario accede al formulario de registro
2. Completa los campos requeridos: `fullName`, `emailAddress`, `password`, `userRole`
3. El sistema valida que el correo no exista previamente
4. Se crea el usuario y se genera un token de autenticación

**Flujos Alternos:**

**E1 - Correo duplicado**
- Si el correo ya existe en el sistema, se rechaza el registro

**E2 - Dominio no permitido**
- Si el dominio del correo no está en la lista de dominios aceptados, se rechaza la solicitud

**E3 - Formato inválido**
- Si algún campo requerido está vacío o tiene un formato incorrecto, se valida antes de procesar

---

### RF2: Inicio de Sesión

El usuario registrado puede iniciar sesión en la plataforma usando sus credenciales de correo y contraseña. El sistema autentica al usuario y proporciona un token de sesión que permite acceder a todas las funcionalidades según su rol.

**Flujo Principal:**
1. El usuario accede a la página de inicio de sesión
2. Ingresa `emailAddress` y `password`
3. El sistema verifica las credenciales contra los registros existentes
4. Si son válidas, se genera un token y se otorga acceso a la plataforma

**Flujos Alternos:**

**E1 - Credenciales inválidas**
- Si el correo no existe o la contraseña es incorrecta, se rechaza el inicio de sesión

**E2 - Token expirado**
- Si el token anterior ha expirado, se requiere un nuevo inicio de sesión

---

### RF3: Creación de Torneo

Un organizador puede crear un nuevo torneo en la plataforma. El sistema proporciona un código único para identificar el torneo. El torneo se crea en estado BORRADOR para permitir ajustes antes de abrirlo a inscripciones.

**Flujo Principal:**
1. El organizador accede al módulo de creación de torneos
2. Completa: `tournamentName`, `description`, `regulationsText`
3. El sistema genera un `tournamentCode` único
4. El torneo se crea en estado BORRADOR

**Flujos Alternos:**

**E1 - Nombre duplicado**
- Si ya existe un torneo con el mismo nombre, se rechaza y se solicita uno diferente

**E2 - Datos incompletos**
- Si falta información requerida, se valida antes de crear el torneo

---

### RF4: Creación de Equipo

Un usuario puede crear un equipo e invitar a jugadores a unirse. El equipo recibe un código único (`teamCode`) y puede ser configurado con colores y escudo. Los jugadores deben ser invitados por su correo electrónico.

**Flujo Principal:**
1. El usuario accede a crear equipo
2. Completa: `teamName`, `shieldUrl`, `primaryColor`, `secondaryColor`
3. Invita jugadores por correo electrónico
4. Se genera un `teamCode` único y se crea el equipo

**Flujos Alternos:**

**E1 - Jugador no existe**
- Si el correo del jugador no está registrado, se genera una invitación

**E2 - Jugador ya en equipo**
- Si el jugador ya pertenece a otro equipo en el mismo torneo, se rechaza

**E3 - Correo con dominio no permitido**
- Si el jugador externo (Google) no cumple restricciones, se rechaza

---

### RF5: Inscripción a Torneo

Un equipo puede inscribirse a un torneo disponible. El organizador revisa la inscripción y determina si es aprobada o rechazada. Se requiere un comprobante de pago para completar la inscripción.

**Flujo Principal:**
1. El capitán del equipo selecciona un torneo y solicita inscripción
2. Proporciona: `paymentProofUrl`, `teamCode`, `tournamentCode`
3. La inscripción se crea en estado PENDIENTE
4. El organizador revisa y aprueba o rechaza

**Flujos Alternos:**

**E1 - Torneo no disponible**
- Si el torneo ya cerró inscripciones, se rechaza la solicitud

**E2 - Equipo duplicado**
- Si el equipo ya está inscrito en este torneo, se rechaza

**E3 - Comprobante inválido**
- Si el comprobante de pago no es válido o está vencido, se rechaza

---

### RF6: Creación de Partido

El organizador crea partidos entre equipos dentro de un torneo. Cada partido incluye fecha, hora, ubicación y equipos participantes. El sistema genera un código único para cada partido.

**Flujo Principal:**
1. El organizador selecciona un torneo
2. Completa: `homeTeamCode`, `awayTeamCode`, `matchDate`, `matchTime`, `venue`
3. El sistema crea el partido en estado PROGRAMADO
4. Se genera un `matchCode` único

**Flujos Alternos:**

**E1 - Equipos no inscritos**
- Si alguno de los equipos no está inscrito en el torneo, se rechaza

**E2 - Conflicto de horario**
- Si algún equipo ya tiene un partido en la misma hora, se rechaza

**E3 - Fecha inválida**
- Si la fecha del partido es anterior a la actual, se rechaza

---

### RF7: Actualización de Resultado

Tras finalizar un partido, el organizador registra el resultado final. El sistema calcula automáticamente puntos y actualiza la tabla de posiciones del torneo.

**Flujo Principal:**
1. El organizador accede al partido jugado
2. Ingresa: `homeTeamGoals`, `awayTeamGoals`
3. El sistema valida los goles y calcula el resultado
4. Se actualiza la tabla de posiciones automáticamente

**Flujos Alternos:**

**E1 - Goles negativos**
- Si se ingresa un número negativo de goles, se rechaza

**E2 - Partido ya cerrado**
- Si el partido ya fue finalizado, no se permite editar el resultado

---
## Matriz de Trazabilidad.

| ID | Requerimiento / Funcionalidad | Responsable | Pantalla Frontend | Endpoint Backend | Estado | Observaciones |
|---|---|---|---|---|---|---|
| RF-01 | Login con JWT | Backend / Frontend | Login | `POST /api/auth` | Implementado | Flujo completo |
| RF-02 | Login con Google OAuth2 | Backend / Frontend | Login | `GET /api/auth/google/start` | Implementado | Redirección OAuth |
| RF-03 | Registro de usuarios | Backend / Frontend | Registro Usuario | `POST /api/users` | Implementado | Roles soportados |
| RF-04 | Listado de usuarios | Backend / Frontend | Administración Usuarios | `GET /api/users` | Implementado | Vista administrativa |
| RF-05 | Buscar jugadores disponibles | Backend / Frontend | Buscar Jugadores | `GET /api/players/available` | Implementado | Filtros por nombre y posición |
| RF-06 | Invitar jugador a equipo | Backend / Frontend | Gestión de Equipo | `POST /api/players/invitations` | Implementado | Capitán invita |
| RF-07 | Aceptar/Rechazar invitación | Backend / Frontend | Notificaciones / Invitaciones | `PATCH /api/players/invitations/{id}/acceptance` | Implementado | Flujo jugador |
| RF-08 | Crear equipo | Backend / Frontend | Crear Equipo | `POST /api/teams` | Implementado | |
| RF-09 | Listar equipos | Backend / Frontend | Equipos | `GET /api/teams` | Implementado | |
| RF-10 | Crear torneo | Backend / Frontend | Crear Torneo | `POST /api/tournaments` | Implementado | Estado inicial DRAFT |
| RF-11 | Configurar torneo | Backend / Frontend | Configuración Torneo | `PUT /api/tournaments/{id}` | Implementado | Reglas, canchas y horarios |
| RF-12 | Historial de torneos | Backend / Frontend | Torneos | `GET /api/tournaments/query/all` | Implementado | |
| RF-13 | Detalle de torneo | Backend / Frontend | Detalle Torneo | `GET /api/tournaments/query/{id}` | Implementado | |
| RF-14 | Tabla de posiciones | Backend / Frontend | Tabla Posiciones | `GET /api/tournaments/query/{tournament}/standings` | Implementado | |
| RF-15 | Brackets eliminatorios | Backend / Frontend | Brackets | `GET /api/tournaments/query/{tournament}/brackets/{phase}` | Implementado | |
| RF-16 | Calendario de partidos | Backend / Frontend | Calendario | `GET /api/tournaments/query/{tournament}/calendar` | Implementado | |
| RF-17 | Resultados históricos | Backend / Frontend | Resultados | `GET /api/tournaments/query/{tournament}/results` | Implementado | |
| RF-18 | Estadísticas globales | Backend / Frontend | Estadísticas | `GET /api/tournaments/query/{tournament}/statistics` | Implementado | |
| RF-19 | Tabla de goleadores | Backend / Frontend | Goleadores | `GET /api/tournaments/query/{tournament}/scorers` | Implementado | |
| RF-20 | Historial por equipo | Backend / Frontend | Perfil Equipo | `GET /api/tournaments/query/{tournament}/history/{team}` | Implementado | |
| RF-21 | Inscribir equipo a torneo | Backend / Frontend | Inscripción Torneo | `POST /api/registrations` | Implementado | Incluye comprobante |
| RF-22 | Aprobar/Rechazar inscripción | Backend / Frontend | Gestión Inscripciones | `PUT /api/registrations/{id}/status` | Implementado | Organizer/Admin |
| RF-23 | Listar inscripciones | Backend / Frontend | Inscripciones | `GET /api/registrations` | Implementado | |
| RF-24 | Crear partido | Backend / Frontend | Gestión Partidos | `POST /api/matches` | Implementado | |
| RF-25 | Actualizar marcador | Backend / Frontend | Marcador en Vivo | `PUT /api/matches/{id}/score` | Implementado | |
| RF-26 | Registrar alineación | Backend / Frontend | Alineación | `PUT /api/matches/{id}/lineup` | Implementado | Vista árbitro |
| RF-27 | Asignar árbitro | Backend / Frontend | Gestión Partidos | `PUT /api/matches/{id}/referee` | Implementado | |
| RF-28 | Partidos por árbitro | Backend / Frontend | Panel Árbitro | `GET /api/matches/referee/{refereeEmail}` | Implementado | |
| RF-29 | Listar partidos | Backend / Frontend | Partidos | `GET /api/matches` | Implementado | |

---
 ## Objetivo
- Relacionar cada funcionalidad del sistema con su responsable técnico.
- Vincular pantallas del frontend con endpoints del backend.
- Identificar el estado actual de implementación.
- Mantener alineación con el documento de requerimientos actualizado.

##  Estados
-  **Implementado** → funcionalidad operativa y alineada
-  **Pendiente** → requiere desarrollo frontend/backend
-  **Deuda Técnica** → implementado pero necesita mejora

---

##  Validación con Criterios de Aceptación
- ✔ Cada funcionalidad tiene responsable asignado
- ✔ Existe relación Pantalla Front ↔ Endpoint Back
- ✔ Todas las filas tienen estado actual
- ✔ Alineado con requerimientos funcionales actualizados
---

## Reglas de Negocio

### RN1: Dominios de Correo Aceptados

El sistema acepta cuentas de correo corporativas y educativas. Los dominios permitidos incluyen:

- Instituciones educativas locales: `*.edu.co`
- Proveedores corporativos: `*.empresa.com.co`
- Plataformas estándar: `gmail.com`, `outlook.com`

Los administradores pueden agregar dominios a la lista de blancos según necesidad.

### RN2: Restricciones para Usuarios Google

Los usuarios que se registren con cuentas de Google (`google.com`) deben cumplir validaciones adicionales:

- El nombre debe completarse correctamente en su perfil
- Debe aceptar explícitamente los términos de uso
- No pueden crear torneos directamente; solo pueden inscribir equipos bajo supervisión de un organizador

### RN3: Códigos Únicos

Todos los identificadores principales (`tournamentCode`, `teamCode`, `matchCode`) deben ser únicos a nivel de sistema. Se generan automáticamente usando algoritmos que producen códigos alfanuméricos de 8 caracteres, legibles y no reutilizables.

### RN4: Estados del Torneo

Transiciones permitidas:

- `BORRADOR` → `ACTIVO` (cuando está listo para inscripciones)
- `ACTIVO` → `EN CURSO` (cuando inician los partidos)
- `EN CURSO` → `FINALIZADO` (cuando se juegan todos los partidos)

### RN5: Puntuación en Partidos

Criterios de puntos:

- **Victoria**: 3 puntos
- **Empate**: 1 punto
- **Derrota**: 0 puntos

### RN6: Validación de Alineaciones

Cada equipo debe registrar una alineación antes de que el partido inicie. La alineación debe incluir un mínimo de 7 jugadores y un máximo de 11. Si no se registra alineación 15 minutos antes del partido, se considera abandono.

---

## Campos de Entrada

Todos los campos de datos de entrada están especificados en inglés siguiendo estándares de desarrollo.

### Registro de Usuario

| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| `fullName` | Text | Sí |
| `emailAddress` | Email | Sí |
| `password` | Password | Sí |
| `userRole` | Enum | Sí |

### Creación de Torneo

| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| `tournamentName` | Text | Sí |
| `description` | Text | No |
| `regulationsText` | Text | Sí |

### Creación de Equipo

| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| `teamName` | Text | Sí |
| `shieldUrl` | URL | No |
| `primaryColor` | Hex Color | Sí |
| `secondaryColor` | Hex Color | No |
| `playerEmails` | List[Email] | Sí |

### Inscripción a Torneo

| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| `teamCode` | Code | Sí |
| `tournamentCode` | Code | Sí |
| `paymentProofUrl` | URL | Sí |

### Creación de Partido

| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| `homeTeamCode` | Code | Sí |
| `awayTeamCode` | Code | Sí |
| `matchDate` | Date | Sí |
| `matchTime` | Time | Sí |
| `venue` | Text | Sí |

### Actualización de Resultado

| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| `homeTeamGoals` | Integer | Sí |
| `awayTeamGoals` | Integer | Sí |

---

## Excepciones y Manejo de Errores

### Clasificación de Excepciones

#### Nivel E1 - Críticas (HTTP 400/401)

Errores de validación de entrada, credenciales inválidas o recursos no encontrados. El usuario debe corregir su solicitud.

#### Nivel E2 - Restricciones de Negocio (HTTP 409)

Conflictos con las reglas de negocio o el estado actual de los datos. La solicitud es válida sintácticamente pero viola una regla del dominio.

#### Nivel E3 - Errores del Sistema (HTTP 500)

Fallos inesperados en el servidor, base de datos o servicios externos. El usuario no puede resolver estos errores directamente.

### Tabla de Excepciones por Funcionalidad

| Funcionalidad | Condición de Error | Nivel | HTTP |
|---|---|---|---|
| Registro | Correo ya existe | E1 | 400 |
| Registro | Dominio no permitido | E2 | 409 |
| Registro | Campo requerido vacío | E1 | 400 |
| Login | Credenciales inválidas | E1 | 401 |
| Login | Token expirado | E2 | 401 |
| Crear Torneo | Nombre duplicado | E2 | 409 |
| Crear Torneo | Datos incompletos | E1 | 400 |
| Crear Equipo | Jugador no existe | E1 | 404 |
| Crear Equipo | Jugador ya en equipo | E2 | 409 |
| Crear Equipo | Usuario Google restringido | E2 | 403 |
| Inscribir | Torneo cerrado | E2 | 409 |
| Inscribir | Equipo duplicado | E2 | 409 |
| Inscribir | Comprobante inválido | E1 | 400 |
| Crear Partido | Equipos no inscritos | E1 | 404 |
| Crear Partido | Conflicto de horario | E2 | 409 |
| Crear Partido | Fecha inválida | E1 | 400 |
| Actualizar Resultado | Goles negativos | E1 | 400 |
| Actualizar Resultado | Partido ya cerrado | E2 | 409 |

---

## Diagramas UML

### Diagrama de Clases — TechCup Definitivo

El diagrama de clases muestra la estructura completa del dominio, incluyendo todas las entidades, sus atributos, métodos y relaciones entre capas (Web, Core, Persistencia):

![Diagrama de Clases](images/DIAGRAMA%20DE%20CLASES%20TECHCUP%20DEFINITIVO.svg)

### Diagrama de Clases — Base de Datos

![Diagrama de Clases Base de Datos](images/DIAGRAMA%20DE%20CLASES%20BASE%20DE%20DATOS.png)

---

## Diagramas de Secuencia

Los siguientes diagramas documentan el flujo de interacción entre componentes del sistema para cada funcionalidad implementada.

### 1. Inicio de Sesión (Login)
![alt text](<docs/images/Diagrama de secuencia 1.png>)
Permite que un usuario ingrese al sistema usando correo y contraseña. El servicio verifica que las credenciales existan y coincidan; si son correctas, devuelve un token junto con su rol.

### 2. Registro de Usuario
![alt text](<docs/images/Diagrama de secuencia 2.png>)
Registra nuevos usuarios del sistema validando campos obligatorios y reglas del dominio. Si la información es válida, se almacena el usuario y se retorna su representación de respuesta.

### 3. Consulta de Usuarios
![alt text](<docs/images/Diagrama de secuencia 3.png>)
Obtiene el listado completo de usuarios creados en la aplicación. La consulta toma los datos almacenados y los transforma a DTO.

### 4. Creación de Equipo
![alt text](<docs/images/Diagrama de secuencia 4.png>)
Permite crear un equipo con su información principal y asociar jugadores existentes usando sus correos. Se valida la solicitud y se construye el equipo.

### 5. Consulta de Equipos
![alt text](<docs/images/Diagrama de secuencia 5.png>)
Retorna todos los equipos registrados. La respuesta incluye los datos relevantes del equipo y su estado actual.

### 6. Creación de Torneo
![alt text](<docs/images/Diagrama de secuencia 6.png>)
Inicia un nuevo torneo con su configuración base en estado BORRADOR para permitir ajustes posteriores.

### 7. Configuración de Torneo
![alt text](<docs/images/Diagrama de secuencia 7.png>)
Actualiza la información operativa de un torneo existente. Solo permite cambios cuando el torneo está en estados válidos.

### 8. Consulta de Torneos
![alt text](<docs/images/Diagrama de secuencia 8.png>)
Consulta todos los torneos creados en el sistema para mostrar su información general y estado.

### 9. Registro de Inscripción
![alt text](<docs/images/Diagrama de secuencia 9.png>)
Registra la inscripción de un equipo al torneo incluyendo datos del pago. El sistema valida y deja trazabilidad del proceso.

### 10. Actualización de Estado de Inscripción
![alt text](<docs/images/Diagrama de secuencia 10.png>)
Permite cambiar el estado de una inscripción de acuerdo con el flujo permitido.

### 11. Consulta de Inscripciones
![alt text](<docs/images/Diagrama de secuencia 11.png>)
Muestra todas las inscripciones registradas junto con su estado actual.

### 12. Registro de Partido
![alt text](<docs/images/Diagrama de secuencia 12.png>)
Crea un partido entre dos equipos dentro de un torneo con información inicial necesaria.

### 13. Actualización de Marcador
![alt text](<docs/images/Diagrama de secuencia 13.png>)
Actualiza los goles de un partido y consolida el resultado final.

### 14. Registro de Alineación
![alt text](<docs/images/Diagrama de secuencia 14.png>)
Registra los jugadores que participarán en un partido para cada equipo.

### 15. Registro de Tarjetas
![Diagrama de secuencia 15](<docs/images/Diagrama de secuencia 15.png>)
Permite registrar eventos disciplinarios de un partido.

### 16. Iniciar OAuth2 Google (`GET /api/auth/google/start`)
![Diagrama de secuencia 16](<docs/images/SECUENCIA OAUTH2.png>)
Inicia el flujo de autenticación con Google. El controlador redirige al filtro de Spring Security, que construye la URL de autorización de Google y responde con un `302 Redirect` hacia la pantalla de consentimiento de Google.

### 17. Buscar Jugadores Disponibles (`GET /api/players/available`)
![Diagrama de secuencia 17](<docs/images/SECUENCIA BUSCAR JUGADORES DISPONIBLES.png>)
Retorna la lista de jugadores sin equipo asignado. Acepta filtros opcionales por nombre y posición. Si no se encuentran jugadores con los criterios dados, retorna lista vacía con `200 OK`.

### 18. Enviar Invitación a Equipo (`POST /api/players/invitations`)
![Diagrama de secuencia 18](<docs/images/SECUENCIA ENVIAR INVITACION A EQUIPO.png>)
Permite al capitán de un equipo invitar a un jugador disponible. El sistema valida que el jugador exista, que no tenga equipo asignado y que el equipo destino sea válido. Crea la invitación en estado `PENDING`.

### 19. Procesar Invitación (`PATCH /api/players/invitations/{id}/acceptance`)
![Diagrama de secuencia 19](<docs/images/SECUENCIA PROCESAR INVITACION.png>)
Permite al jugador aceptar o rechazar una invitación pendiente. Si el estado es `ACCEPTED`, se actualiza el `teamId` del jugador en la base de datos. Valida que la invitación esté en estado `PENDING` antes de procesarla.

### 20. Detalle de un Torneo (`GET /api/tournaments/query/{id}`)
![Diagrama de secuencia 20](<docs/images/SECUENCIA DETALLE DE TORNEO.png>)
Retorna la información completa de un torneo específico. Valida que el `id` tenga formato UUID válido. Retorna `404 Not Found` si el torneo no existe, o `200 OK` con el `TournamentDetailsDTO`.

### 21. Tabla de Posiciones (`GET /api/tournaments/query/{id}/standings`)
![Diagrama de secuencia 21](<docs/images/SECUENCIA TABLA DE POSICIONES.png>)
Calcula y retorna la tabla de posiciones del torneo. Consolida todos los partidos terminados, calcula puntos, goles a favor, goles en contra y diferencia de gol, ordenando los equipos de mayor a menor puntuación.

### 22. Brackets Eliminatorios (`GET /api/tournaments/query/{id}/brackets/{phase}`)
![Diagrama de secuencia 22](<docs/images/SECUENCIA BRACKETS ELIMINATORIOS.png>)
Retorna la estructura de los cruces eliminatorios para una fase específica (cuartos, semis, final). Valida la fase solicitada y organiza los nodos del bracket con los equipos y resultados correspondientes.

### 23. Calendario Programado (`GET /api/tournaments/query/{id}/calendar`)
![Diagrama de secuencia 23](<docs/images/SECUENCIA CALENDARIO PROGRAMADO.png>)
Retorna todos los partidos programados a futuro para el torneo. Los resultados se ordenan cronológicamente y se agrupan por jornada para facilitar su visualización.

### 24. Resultados Históricos (`GET /api/tournaments/query/{id}/results`)
![Diagrama de secuencia 24](<docs/images/SECUENCIA RESULTADOS HISTORICOS.png>)
Retorna todos los partidos ya jugados y sus resultados finales. Filtra partidos con estado `COMPLETED` y los mapea a `ResultsDTO` con marcadores y estadísticas del encuentro.

### 25. Estadísticas Globales (`GET /api/tournaments/query/{id}/statistics`)
![Diagrama de secuencia 25](<docs/images/SECUENCIA ESTADISTICAS GLOBALES.png>)
Agrega y retorna las estadísticas generales del torneo: número de equipos participantes, total de goles, tarjetas amarillas, tarjetas rojas y promedios por partido.

### 26. Tabla de Goleadores (`GET /api/tournaments/query/{id}/scorers`)
![Diagrama de secuencia 26](<docs/images/SECUENCIA TABLA DE GOLEADORES.png>)
Retorna el ranking de goleadores del torneo ordenado descendentemente por cantidad de goles. Agrega los eventos de gol por jugador y retorna la lista de `ScorerDTO`.

### 27. Historial de un Equipo (`GET /api/tournaments/query/{id}/history/{teamId}`)
![Diagrama de secuencia 27](<docs/images/SECUENCIA HISTORIAL DE UN EQUIPO.png>)
Retorna el historial completo de un equipo en el torneo: partidos jugados, resultados, rendimiento (Win Rate) y estadística de goles. Retorna `404` si el equipo no tiene participación registrada.

### 28. Asignar Árbitro (`PUT /api/matches/{id}/referee`)
![Diagrama de secuencia 28](<docs/images/SECUENCIA ASIGNAR ARBITRO.png>)
Asigna un árbitro a un partido. Valida que el usuario exista y que su rol sea `REFEREE`. Además, valida que el partido no esté en estado `COMPLETED`. En caso de éxito, actualiza el `refereeId` en el partido.

### 29. Partidos de un Árbitro (`GET /api/matches/referee/{refereeEmail}`)
![Diagrama de secuencia 29](<docs/images/SECUENCIA PARTIDOS DE UN ARBITRO.png>)
Retorna todos los partidos asignados a un árbitro específico, identificado por su correo electrónico. Valida el formato de la dirección de correo y retorna `404` si el árbitro no existe en el sistema.

### 30. Listar Todos los Partidos (`GET /api/matches`)
![Diagrama de secuencia 30](<docs/images/SECUENCIA LISTAR TODOS LOS PARTIDOS.png>)
Retorna el listado completo de todos los partidos registrados en el sistema, sin importar su estado. Si no hay registros, retorna una lista vacía con `200 OK`.

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
│ role         │         │ teamCode     │
│ position     │         └──────────────┘
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
│ tourneyCode  │         │ created_at   │
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
│ matchCode    │
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
  POST /api/auth
   {
     "emailAddress": "user@example.com",
     "password": "password123"
   }

2. Servidor valida y genera JWT
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "expiresIn": 86400,
     "userRole": "PLAYER"
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
- Validación de dominios de correo permitidos
- Restricciones adicionales para usuarios Google
- Credenciales nunca se exponen en respuestas

---

## Testing

### Estructura de Tests

```
src/test/java/com/zeuscodensa/techcupfutbol/
├── config/
├── controller/
│   ├── api/
│   ├── handler/
│   └── mapper/
├── core/
│   ├── service/
│   ├── strategy/
│   └── validator/
├── persistence/
└── security/
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

Acceder a la interfaz Swagger en: `https://localhost:8443/swagger-ui/index.html`

Descargar especificación OpenAPI: `https://localhost:8443/v3/api-docs`

### Archivos de Prueba

- **Archivo**: `pruebas.http`
- **Uso**: Ejecutar directamente desde IntelliJ IDEA para validar el flujo End-to-End (Controlador → Servicio → Base de Datos)

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

**Status**: En Desarrollo (Sprint #4)

---

## 🚀 Despliegue en Azure — Estado Actual

### Ambientes Desplegados

| Ambiente | URL | Estado |
|---|---|---|
| **QA** | `https://techcup-qa.happymushroom-f55d56eb.eastus.azurecontainerapps.io` | 🟡 Configurado |
| **PROD** | `https://techcup-prod.happymushroom-f55d56eb.eastus.azurecontainerapps.io` | 🟡 Pendiente aprobación |

### Health Check

```bash
# QA
curl https://techcup-qa.happymushroom-f55d56eb.eastus.azurecontainerapps.io/health
# Respuesta esperada: {"status":"UP","version":"1.3.3","db":"OK"}

# PROD
curl https://techcup-prod.happymushroom-f55d56eb.eastus.azurecontainerapps.io/health
```

### Infraestructura Azure (rg-zeus-codensa)

| Recurso | Nombre | Región |
|---|---|---|
| Resource Group | `rg-zeus-codensa` | East US |
| Container Registry | `zeuscodensaacr.azurecr.io` | East US |
| PostgreSQL Flexible Server | `techcup-dbserver.postgres.database.azure.com` | East US |
| Container App QA | `techcup-qa` | East US |
| Container App PROD | `techcup-prod` | East US |

### Pipeline CI/CD

| Workflow | Rama | Trigger | Acción |
|---|---|---|---|
| `ci.yml` | develop / main | push / PR | Tests + JaCoCo + SonarCloud + Docker → ACR |
| `cd-qa.yml` | develop | CI exitoso | Deploy automático → Container App QA |
| `cd-prod.yml` | main | CI exitoso | Deploy manual (3 aprobadores) → Container App PROD |

> **Versión desplegada:** `v1.3.3`

