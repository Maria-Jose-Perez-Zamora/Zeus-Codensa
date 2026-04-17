# Requerimientos del sistema – TECHCUP Fútbol
**Nombre del Equipo:** Zeus-Codensa  
**Integrantes:** Nicolas Sanchez, Maria Jose Perez, Andres Pineda, Diego Andrade, Stiven Pardo

## Requerimientos funcionales principales

1. Gestionar torneos: crear, iniciar, finalizar y consultar torneos.
2. Registrar usuarios y jugadores por rol (estudiante, graduado, profesor, administrativo, familiar, capitán, organizador, árbitro, administrador).
3. Gestionar equipos: crear equipo, invitar jugadores y validar reglas de conformación (mínimo 7 / máximo 20 jugadores y sin duplicidad).
4. Gestionar inscripciones y pagos: cargar comprobante y administrar estados (`PENDING` → `IN_REVIEW` → `APPROVED` / `REJECTED`).
5. Configurar torneo: reglamento, fechas clave, cierre de inscripciones, horarios, canchas y sanciones.
6. Registrar partidos: marcador, goleadores y tarjetas.
7. Calcular automáticamente tabla de posiciones y generar llaves eliminatorias.
8. Consultar información del torneo: calendario, resultados, estadísticas e información para árbitros.

# Requerimientos no funcionales principales

1. Diseño responsivo: la plataforma debe adaptarse correctamente a pantallas de celular y computador.
2. Seguridad y acceso: autenticación mediante **JWT (JSON Web Tokens)** con correo institucional `@escuelaing.edu.co` en formato `nombre.apellido-inicial@escuelaing.edu.co`. Los tokens tienen una vigencia de **1 hora (TTL)**. Control de acceso basado en roles (RBAC) que aplica permisos a nivel de recurso.
3. Transporte seguro: la aplicación expone sus servicios exclusivamente por **HTTPS en el puerto 8443** con certificado SSL (PKCS12). Las peticiones entrantes por HTTP (puerto 8080) son redirigidas automáticamente a HTTPS.
4. CORS controlado: se admiten peticiones de los orígenes de desarrollo del frontend (`localhost:3000`, `localhost:4200`, `localhost:5173`).
5. Auditoría: registrar acciones relevantes para trazabilidad de cambios y operaciones mediante SLF4J/Logback.
6. Rendimiento: tiempos de respuesta adecuados en operaciones frecuentes (consulta de tabla, partidos, equipos e inscripciones).
7. Disponibilidad y confiabilidad: el sistema debe estar estable durante periodos críticos del torneo.
8. Arquitectura mantenible: backend por capas (MVC + servicios + validadores) con API REST, frontend pendiente de integración y base de datos PostgreSQL.
9. Integridad de datos: validaciones automáticas de reglas del torneo y transiciones de estado estrictas para evitar inconsistencias.


# Requerimientos funcionales detallados


# RF-001: Login – Flujo de Autenticación

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-001 | Login – Flujo de Autenticación |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite a los usuarios autenticarse mediante su correo institucional y contraseña. Si las credenciales son válidas, se genera un JWT firmado con el rol del usuario como claim y un TTL de 1 hora. | El usuario envía `POST /api/auth/login` con su correo y contraseña. El sistema valida las credenciales, asigna el rol y retorna el token. | Cualquier usuario registrado | El usuario debe estar previamente registrado en el sistema. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `correo` | Correo institucional del usuario | `String` | No puede ser nulo. Debe cumplir el patrón `nombre.apellido-inicial@escuelaing.edu.co`. | Sí |
| `contrasena` | Contraseña del usuario | `String` | No puede ser nula. Mínimo 6 caracteres. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `userId` | Identificador único del usuario | `Long` | Generado automáticamente. | Sí |
| `correo` | Correo del usuario autenticado | `String` | Correo institucional del usuario. | Sí |
| `role` | Rol del usuario en el sistema | `String` | Rol asignado al usuario (`PLAYER`, `CAPTAIN`, `REFEREE`, `TOURNAMENT_ORGANIZER`, `ADMINISTRADOR_SISTEMA`). | Sí |
| `token` | JWT generado | `String` | Firmado con HS256. TTL: 1 hora. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente | Envía `POST /api/auth/login` con `correo` y `contrasena`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|


# RF-002: Registro de Usuario

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-002 | Registro de Usuario |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Cada participante se registra en el sistema con su **correo institucional** en formato `nombre.apellido-inicial@escuelaing.edu.co`. El sistema valida el formato del correo en el backend (`UserValidator`) antes de crear el usuario. Los roles disponibles son: `PLAYER`, `CAPTAIN`, `REFEREE`, `TOURNAMENT_ORGANIZER`, `ADMINISTRADOR_SISTEMA`. | El usuario envía `POST /api/users/register` con sus datos. El sistema valida el correo, la contraseña (mínimo 6 caracteres), la posición y el número de dorsal (obligatorios para `PLAYER` y `CAPTAIN`). | `PLAYER`, `CAPTAIN`, `REFEREE`, `TOURNAMENT_ORGANIZER`, `ADMINISTRADOR_SISTEMA` | Ninguna (endpoint público). |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `name` | Nombre completo del usuario | `String` | No puede estar vacío. | Sí |
| `email` | Correo institucional | `String` | Debe cumplir el patrón `nombre.apellido-inicial@escuelaing.edu.co`. Único en el sistema. | Sí |
| `password` | Contraseña | `String` | Mínimo 6 caracteres. Se almacena cifrada (BCrypt). | Sí |
| `role` | Rol del usuario en el sistema | `Enum` | `PLAYER`, `CAPTAIN`, `REFEREE`, `TOURNAMENT_ORGANIZER`, `ADMINISTRADOR_SISTEMA`. | Sí |
| `position` | Posición de juego | `String` | Obligatorio para `PLAYER` y `CAPTAIN`. Valores: `Portero`, `Defensa`, `Volante`, `Delantero`. | Condicional |
| `jerseyNumber` | Número de dorsal | `Integer` | Mayor que 0. Obligatorio para `PLAYER` y `CAPTAIN`. | Condicional |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `id` | Identificador único del usuario | `Long` | Generado automáticamente. | Sí |
| `profileCompleted` | Indica si el perfil fue creado exitosamente | `Boolean` | `true` si el registro fue exitoso. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente | Envía `POST /api/users/register` con `name`, `email`, `password`, `role`. | — |
| 2 | Sistema (`UserController`) | Valida que el token JWT sea válido si se requiere. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`UserValidator`) | Ejecuta `validateForRegistration(requestDTO)`: valida formato de `email`, longitud de `password`, y campos condicionales de `PLAYER`/`CAPTAIN`. | Datos inválidos o regla incumplida: `400 Bad Request` con `BusinessRuleException`. |
| 4 | Sistema (`UserMapper`) | Ejecuta `toEntity(requestDTO)` para convertir el DTO al modelo `User`. | — |
| 5 | Sistema (`UserRepository`) | Ejecuta `save(newUser)`. Si el correo ya está registrado, lanza excepción. | Correo ya registrado: `400 Bad Request` con `BusinessRuleException`. |
| 6 | Sistema (`UserMapper`) | Ejecuta `toDTO(newUser)` para obtener `UserResponseDTO`. | — |
| 7 | Sistema | Retorna `200 OK + UserResponseDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error inesperado durante el proceso. | `500 Internal Server Error` con `GenericException`. |


# RF-003: Consulta de Usuarios

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-003 | Consulta de Usuarios |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite listar todos los usuarios registrados. Los datos se retornan como una lista de `UserResponseDTO`. | El cliente envía `GET /api/users/all`. El sistema valida el JWT, consulta el repositorio y mapea los resultados. | Cualquier usuario autenticado | Token JWT válido y no expirado. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `Authorization` | Header con el token JWT | `String` | Formato `Bearer <token>`. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `List<UserResponseDTO>` | Lista de usuarios registrados | `List` | Cada elemento contiene los datos públicos del usuario. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente | Envía `GET /api/users/all` con header `Authorization: Bearer <token>`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error inesperado durante la consulta. | `500 Internal Server Error` con `GenericException`. |


# RF-004: Creación de Equipo

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-004 | Creación de Equipo |

## Prototipos e Interfaces Visuales (Mockups)

🔗 **Link de Figma:** https://www.figma.com/make/wZnY6r0oYU309jDTJmjvGY/Mockup-Zeus-Codensa?fullscreen=1&t=bSMPtVvNZMNYLrdZ-1&preview-route=%2Fauth%2Flogin

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Los capitanes crean equipos con nombre, escudo y colores de uniforme. Se validan las reglas: mínimo 7 jugadores, máximo 20, sin duplicidad de jugador entre equipos, y más del 50% de los miembros deben pertenecer a programas autorizados. | El capitán envía `POST /api/teams/create` con `TeamRequestDTO`. El sistema valida el JWT, aplica reglas de negocio y persiste el equipo. | Capitán | El usuario debe tener rol `CAPTAIN`. Debe existir al menos un torneo en estado `OPEN`. |

## Datos de entrada (Crear equipo)

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `teamName` | Nombre del equipo | `String` | Único en el sistema. No puede estar vacío. | Sí |
| `shieldUrl` | URL del escudo del equipo | `String` | URL válida (formato `http/https`). | No |
| `uniformColor` | Color del uniforme | `String` | Valor libre (texto descriptivo). | No |
| `jugadoresCorreos` | Lista de correos de jugadores | `List<String>` | Entre **7 y 20** correos. Sin duplicados. Cada correo debe pertenecer a un usuario registrado con rol `PLAYER`. Un jugador no puede pertenecer a dos equipos simultáneamente. | Sí |

## Datos de entrada (Búsqueda de jugadores)

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `position` | Posición de juego | `Enum` | `Portero`, `Defensa`, `Volante`, `Delantero`. | No |
| `semester` | Semestre académico | `Integer` | Solo aplica para usuarios con tipo `Estudiante`. | No |
| `ageMin` | Edad mínima | `Integer` | Mayor que 0. | No |
| `ageMax` | Edad máxima | `Integer` | Mayor o igual a `ageMin`. | No |
| `gender` | Género del jugador | `Enum` | `Masculino`, `Femenino`, `Otro`. | No |
| `name` | Nombre del jugador | `String` | Búsqueda parcial (contiene). | No |
| `identification` | Número de identificación | `String` | Búsqueda exacta. | No |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `teamId` | Identificador único del equipo | `Long` | Generado automáticamente. | Sí |
| `availablePlayers` | Lista de jugadores disponibles | `List<PlayerDTO>` | Solo jugadores que no pertenecen a ningún equipo. | Sí |
| `invitationStatus` | Estado de la invitación enviada | `Enum` | `PENDING`, `ACCEPTED`, `REJECTED`. | Sí |
| `message` | Mensaje de confirmación o error | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Capitán) | Envía `POST /api/teams/create` con `TeamRequestDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Intenta agregar un jugador que ya pertenece a otro equipo. | `TeamValidator` rechaza toda la petición con `400 Bad Request`. |


# RF-005: Consulta de Equipos

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-005 | Consulta de Equipos |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite listar todos los equipos registrados. Los datos se retornan como una lista de `TeamResponseDTO`. | El cliente envía `GET /api/teams/all`. El sistema valida el JWT, consulta el repositorio y mapea los resultados. | Cualquier usuario autenticado | Token JWT válido y no expirado. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `Authorization` | Header con el token JWT | `String` | Formato `Bearer <token>`. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `List<TeamResponseDTO>` | Lista de equipos registrados | `List` | Cada elemento contiene los datos del equipo. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente | Envía `GET /api/teams/all` con header `Authorization: Bearer <token>`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`TeamService`) | Ejecuta `getAllTeams()` → llama a `TeamRepository.findAll()`. | — |
| 4 | Sistema | Si la lista está vacía, retorna `[]`. | Lista vacía: `204 No Content`. |
| 5 | Sistema (`TeamMapper`) | Por cada equipo: ejecuta `toDTO(Team)` → obtiene `TeamResponseDTO`. (Loop) | — |
| 6 | Sistema | Retorna `200 OK + List<TeamResponseDTO>`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error inesperado durante la consulta. | `500 Internal Server Error` con `GenericException`. |


# RF-006: Creación de Torneo

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-006 | Creación de Torneo |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite crear torneos con información básica. El organizador define nombre, número de equipos y costo de inscripción. El torneo se crea con estado `DRAFT`. | El organizador envía `POST /api/torneos/create` con `TorneoRequestDTO`. El sistema valida el JWT, aplica reglas y persiste el torneo. | Organizador | El usuario debe tener rol `TOURNAMENT_ORGANIZER`. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `tournamentName` | Nombre del torneo | `String` | Único en el sistema. No puede estar vacío. | Sí |
| `numeroEquipos` | Número máximo de equipos | `Integer` | Mayor que 1. | Sí |
| `costoInscripcion` | Costo de inscripción (en COP) | `Decimal` | No puede ser negativo (`>= 0`). | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `id` | Identificador único del torneo | `Long` | Generado automáticamente. | Sí |
| `status` | Estado actual del torneo | `Enum` | El estado inicial al crear es `DRAFT`. Estados posibles: `DRAFT`, `OPEN`, `IN_PROGRESS`, `FINISHED`. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Organizador) | Envía `POST /api/torneos/create` con `TorneoRequestDTO`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`TorneoValidator`) | Ejecuta `validateForCreation(request)`: valida nombre único, `numeroEquipos > 1`, `costoInscripcion >= 0`. | Datos inválidos o regla incumplida: `400 Bad Request` con `BusinessRuleException`. |
| 4 | Sistema (`TorneoMapper`) | Ejecuta `toEntity(request)` → obtiene objeto `Torneo`. | — |
| 5 | Sistema (`TorneoRepository`) | Ejecuta `save(torneo)`. | Error en BD: `500 Internal Server Error` con `GenericException`. |
| 6 | Sistema | Retorna el `Torneo` persistido. | — |
| 7 | Sistema (`TorneoMapper`) | Ejecuta `toDTO(torneo)` → obtiene `TorneoResponseDTO`. | — |
| 8 | Sistema | Retorna `200 OK + TorneoResponseDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error en base de datos durante el guardado. | `500 Internal Server Error` con `GenericException`. |


# RF-007: Configuración de Torneo

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-007 | Configuración del Torneo |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Después de crear el torneo, el organizador define el reglamento, fechas importantes, horarios, canchas y sanciones. Cada campo es opcional en la actualización (campos `Opt`). El torneo debe estar en estado `DRAFT` u `OPEN`. | El organizador envía `PUT /api/torneos/{id}/configurar` con `TorneoRequestDTO`. El sistema valida el JWT, el estado del torneo y aplica los cambios. | Organizador | El usuario debe tener rol `TOURNAMENT_ORGANIZER`. El torneo debe existir y estar en estado `DRAFT` u `OPEN`. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `reglamento` | Reglas del torneo | `String` | Máximo 10 000 caracteres. | No (Opt) |
| `fechaCierreInscripciones` | Fecha límite para inscribirse | `Date` (`YYYY-MM-DD`) | Debe ser anterior a `fechaInicioFaseGrupos`. | No (Opt) |
| `fechaInicioFaseGrupos` | Fecha de inicio de partidos | `Date` (`YYYY-MM-DD`) | Debe ser posterior a la fecha de creación del torneo. | No (Opt) |
| `horariosPartidos` | Horas disponibles para partidos | `List<String>` | Formato `HH:mm` (24 h). | No (Opt) |
| `canchas` | Canchas disponibles | `List<String>` | Nombre y ubicación de cada cancha. | No (Opt) |
| `sanciones` | Definición de tarjetas y expulsiones | `String` | Máximo 5 000 caracteres. | No (Opt) |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `TorneoResponseDTO` | Torneo actualizado con la nueva configuración | `Object` | Refleja todos los campos actualizados. | Sí |
| `message` | Mensaje de éxito o error | `String` | Mensaje de confirmación o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Organizador) | Envía `PUT /api/torneos/{id}/configurar` con `TorneoRequestDTO`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`TorneoService`) | Ejecuta `configurarTorneo(id, configInfo)` → llama a `TorneoRepository.findById(id)`. | — |
| 4 | Sistema | Si el torneo no existe, lanza excepción. | Torneo no encontrado: `404 Not Found` con `ResourceNotFoundException`. |
| 5 | Sistema | Valida que el estado del torneo sea `BORRADOR` u `OPEN`. | Estado no permitido: `400 Bad Request` con `BusinessRuleException`. |
| 6 | Sistema | Aplica cada campo de configuración si no es `null` (campos `Opt`): `setReglamento(...)`, `setFechaCierreInscripciones(...)`, `setFechaInicioFaseGrupos(...)`, `setHorariosPartidos(...)`, `setCanchas(...)`, `setSanciones(...)`. | — |
| 7 | Sistema (`TorneoRepository`) | Ejecuta `save(torneo actualizado)`. Retorna `Torneo` persistido. | — |
| 8 | Sistema (`TorneoMapper`) | Ejecuta `toDTO(torneo)` → obtiene `TorneoResponseDTO`. | — |
| 9 | Sistema | Retorna `100 OK + TorneoResponseDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Intenta configurar un torneo en estado `IN_PROGRESS` o `FINISHED`. | `400 Bad Request` con `BusinessRuleException: estado no permitido`. |


# RF-008: Consulta de Torneos

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-008 | Consulta de Torneos |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite listar todos los torneos registrados. Los datos se retornan como una lista de `TorneoResponseDTO`. | El cliente envía `GET /api/torneos/consulta/all`. El sistema valida el JWT, consulta el repositorio y mapea los resultados. | Cualquier usuario autenticado | Token JWT válido y no expirado. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `Authorization` | Header con el token JWT | `String` | Formato `Bearer <token>`. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `List<TorneoResponseDTO>` | Lista de torneos registrados | `List` | Cada elemento contiene los datos del torneo. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente | Envía `GET /api/torneos/consulta/all` con header `Authorization: Bearer <token>`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`TorneoService`) | Ejecuta `getAllTorneos()` → llama a `TorneoRepository.findAll()`. | — |
| 4 | Sistema | Si la lista está vacía, retorna `[]`. | Lista vacía: `204 No Content`. |
| 5 | Sistema (`TorneoMapper`) | Por cada torneo: ejecuta `toDTO(torneo)` → obtiene `TorneoResponseDTO`. (Loop) | — |
| 6 | Sistema | Retorna `200 OK + List<TorneoResponseDTO>`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error inesperado durante la consulta. | `500 Internal Server Error` con `GenericException`. |


# RF-009: Creación de Inscripción

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-009 | Creación de Inscripción |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El capitán registra la inscripción de su equipo en un torneo y adjunta el comprobante de pago. La inscripción se crea con estado inicial `PENDING`. El pago se realiza fuera de la plataforma (NEQUI o efectivo al coordinador). | El capitán envía `POST /api/inscripciones/create` con `InscripcionRequestDTO`. El sistema valida el JWT, las reglas de negocio y persiste la inscripción. | Capitán | Token JWT válido. Torneo en estado `OPEN`. Equipo conformado con al menos 7 jugadores. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `teamName` | Nombre del equipo | `String` | Debe existir en el sistema. | Sí |
| `tournamentName` | Nombre del torneo | `String` | El torneo debe existir y estar en estado `OPEN`. | Sí |
| `comprobantePagoUrl` | URL del comprobante de pago | `String` | No puede estar vacía. Debe ser una URL válida. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `InscripcionResponseDTO` | Inscripción creada con estado `PENDING` | `Object` | Refleja todos los datos de la inscripción. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Capitán) | Envía `POST /api/inscripciones/create` con `InscripcionRequestDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Torneo cerrado o usuario ya inscrito al momento del `save`. | `400 Bad Request` con `BusinessRuleException`. |


# RF-010: Consulta de Inscripciones

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-010 | Consulta de Inscripciones |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite listar todas las inscripciones registradas. Los datos se retornan como una lista de `InscripcionResponseDTO`. | El cliente envía `GET /api/inscripciones/all`. El sistema valida el JWT, consulta el repositorio y mapea los resultados. | Cualquier usuario autenticado | Token JWT válido y no expirado. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `Authorization` | Header con el token JWT | `String` | Formato `Bearer <token>`. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `List<InscripcionResponseDTO>` | Lista de inscripciones registradas | `List` | Cada elemento contiene los datos de la inscripción. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente | Envía `GET /api/inscripciones/all` con header `Authorization: Bearer <token>`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`InscripcionService`) | Ejecuta `getAll()` → llama a `InscripcionRepository.findAll()`. | — |
| 4 | Sistema | Si la lista está vacía, retorna `[]`. | Lista vacía: `204 No Content`. |
| 5 | Sistema (`InscripcionMapper`) | Por cada inscripción: ejecuta `toDTO(Inscripcion)` → obtiene `InscripcionResponseDTO`. (Loop) | — |
| 6 | Sistema | Retorna `200 OK + List<InscripcionResponseDTO>`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error inesperado durante la consulta. | `500 Internal Server Error` con `GenericException`. |


# RF-011: Actualización de Estado de Inscripción

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-011 | Actualización de Estado de Inscripción |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El organizador o administrador cambia el estado de una inscripción siguiendo la **máquina de estados estricta**: `PENDING` → `IN_REVIEW` → `APPROVED` / `REJECTED`. Los estados `APPROVED` y `REJECTED` son finales. Solo equipos con estado `APPROVED` pueden participar en partidos. | El organizador / admin envía `PUT /api/inscripciones/{id}/estado` con el nuevo estado. El sistema valida el JWT, el estado permitido y actualiza la inscripción. | Organizador / Administrador | Token JWT válido. Inscripción existente. Rol `ADMINISTRADOR_SISTEMA` o `TOURNAMENT_ORGANIZER`. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `estado` | Nuevo estado de la inscripción | `Enum` | Estados válidos: `PENDING`, `IN_REVIEW`, `APPROVED`, `REJECTED`. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `InscripcionResponseDTO` | Inscripción actualizada con el nuevo estado | `Object` | Refleja el estado actualizado. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## State Machine – Registration

```
PENDING → IN_REVIEW → APPROVED
                      → REJECTED
```

`APPROVED` y `REJECTED` son estados finales. No admiten transiciones posteriores.

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Organizador / Admin) | Envía `PUT /api/inscripciones/{id}/estado` con `{estado}` en el body. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`InscripcionController`) | Valida que el body contenga el campo `estado`. | Estado no enviado: `400 Bad Request`, `"Es necesario el campo 'estado'"`. |
| 4 | Sistema (`InscripcionService`) | Ejecuta `actualizarEstado(id, nuevoEstado)` → valida que el estado sea uno de los permitidos (`PENDIENTE`, `EN_REVISION`, `APROBADO`, `RECHAZADO`). | Estado inválido: `400 Bad Request` con `BusinessRuleException`. |
| 5 | Sistema (`InscripcionRepository`) | Busca la inscripción por `id`. | Inscripción no encontrada: `404 Not Found` con `ResourceNotFoundException`. |
| 6 | Sistema | Ejecuta `setEstado(nuevoEstado)` sobre la inscripción encontrada. | — |
| 7 | Sistema (`InscripcionRepository`) | Guarda la inscripción actualizada. Retorna `Inscripcion` persistida. | — |
| 8 | Sistema | Retorna `InscripcionResponseDTO` actualizado con `200 OK + InscripcionResponseDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Intenta cambiar el estado de una inscripción en estado final (`APROBADO` o `RECHAZADO`). | `400 Bad Request` con `BusinessRuleException: estado definitivo`. |


# RF-012: Creación de Partido

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-012 | Creación de Partido |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El organizador o árbitro registra un nuevo partido entre dos equipos del torneo. Ambos equipos deben tener inscripción con estado `APPROVED`. El partido se crea con estado `SCHEDULED`. | El organizador / árbitro envía `POST /api/partidos/create` con `PartidoRequestDTO`. El sistema valida el JWT, las reglas y persiste el partido. | Organizador, Árbitro | Token JWT válido. Roles `REFEREE` o `TOURNAMENT_ORGANIZER`. Ambos equipos con inscripción `APPROVED`. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `homeTeam` | Equipo local | `String` | Debe existir en el torneo con inscripción `APPROVED`. Distinto de `awayTeam`. | Sí |
| `awayTeam` | Equipo visitante | `String` | Debe existir en el torneo con inscripción `APPROVED`. Distinto de `homeTeam`. | Sí |
| `tournamentName` | Nombre del torneo | `String` | Debe existir en `DataStorage`. | Sí |
| `matchDate` | Fecha y hora del partido | `DateTime` (`YYYY-MM-DD HH:mm`) | Dentro del periodo activo del torneo. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `PartidoResponseDTO` | Partido creado con estado `SCHEDULED` | `Object` | Refleja todos los datos del partido. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Organizador / Árbitro) | Envía `POST /api/partidos/create` con `PartidoRequestDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Error en base de datos durante el guardado. | `500 Internal Server Error` con `GenericException`. |


# RF-013: Actualización de Marcador

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-013 | Actualización de Marcador |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El organizador o árbitro actualiza el marcador de un partido en curso. Al confirmar el marcador, el partido cambia su estado a `FINALIZADO`. El sistema recalcula la tabla de posiciones automáticamente. | El organizador / árbitro envía `PUT /api/partidos/{id}/marcador` con `marcadorLocal` y `marcadorVisitante`. | Organizador, Árbitro | Token JWT válido. El partido debe existir y estar en estado `EN_CURSO`. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `marcadorLocal` | Goles del equipo local | `Integer` | Mayor o igual a 0. | Sí |
| `marcadorVisitante` | Goles del equipo visitante | `Integer` | Mayor o igual a 0. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `PartidoResponseDTO` | Partido actualizado con estado `FINALIZADO` | `Object` | Refleja el marcador final y el nuevo estado. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Organizador / Árbitro) | Envía `PUT /api/partidos/{id}/marcador` con `marcadorLocal` y `marcadorVisitante`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`PartidoService`) | Ejecuta `actualizarMarcador(id, marcadorLocal, marcadorVisitante)` → valida que los marcadores sean `>= 0`. | Marcadores inválidos: `400 Bad Request` con `BusinessRuleException`. |
| 4 | Sistema (`PartidoRepository`) | Ejecuta `findById(id)`. | Partido no encontrado: `404 Not Found` con `ResourceNotFoundException`. |
| 5 | Sistema | Valida que el estado del partido sea `EN_CURSO`. | Partido no en curso: `400 Bad Request` con `BusinessRuleException`. |
| 6 | Sistema | Ejecuta `setMarcadorLocal(marcadorLocal)`, `setMarcadorVisitante(marcadorVisitante)`, `setEstado("FINALIZADO")`. | — |
| 7 | Sistema (`PartidoRepository`) | Ejecuta `save(partido actualizado)`. Retorna `Partido` persistido. | — |
| 8 | Sistema (`PartidoMapper`) | Ejecuta `toDTO(partido)` → obtiene `PartidoResponseDTO`. | — |
| 9 | Sistema | Retorna `200 OK + PartidoResponseDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Intenta actualizar el marcador de un partido no en estado `EN_CURSO`. | `400 Bad Request` con `BusinessRuleException: Partido no en curso`. |


# RF-014: Registro de Alineación

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-014 | Registro de Alineación |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El organizador o árbitro registra la alineación (lista de jugadores) de un equipo para un partido. Se valida que el equipo participe en el partido y que la lista de jugadores no esté vacía. | El organizador / árbitro envía `PUT /api/partidos/{id}/alineacion` con `nombreEquipo` y la lista de `jugadores`. | Organizador, Árbitro | Token JWT válido. El partido debe existir. El equipo debe participar en el partido como local o visitante. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `nombreEquipo` | Nombre del equipo que registra alineación | `String` | Debe ser el equipo local o visitante del partido. | Sí |
| `jugadores` | Lista de jugadores en la alineación | `List<String>` | No puede estar vacía. Exactamente 7 jugadores por regla de negocio (RN-006). | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `PartidoResponseDTO` | Partido actualizado con la alineación registrada | `Object` | Refleja la alineación del equipo. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Organizador / Árbitro) | Envía `PUT /api/partidos/{id}/alineacion` con `nombreEquipo` y `jugadores`. | — |
| 2 | Sistema (`JWTProvider`) | Valida el token JWT. | Token inválido/expirado: `401 Unauthorized`. |
| 3 | Sistema (`PartidoService`) | Ejecuta `registrarAlineacion(id, nombreEquipo, jugadores)` → valida que `jugadores` no esté vacío. | Alineación vacía: `400 Bad Request` con `BusinessRuleException`. |
| 4 | Sistema (`PartidoRepository`) | Ejecuta `findById(id)`. | Partido no encontrado: `404 Not Found` con `ResourceNotFoundException`. |
| 5 | Sistema | Valida que `nombreEquipo` participe en el partido (local o visitante). | Equipo no participa: `400 Bad Request` con `BusinessRuleException`. |
| 6 | Sistema | Ejecuta `setAlineacionLocal(jugadores)` o `setAlineacionVisitante(jugadores)` según corresponda. | — |
| 7 | Sistema (`PartidoRepository`) | Ejecuta `save(partido actualizado)`. Retorna `Partido` persistido. | — |
| 8 | Sistema (`PartidoMapper`) | Ejecuta `toDTO(partido)` → obtiene `PartidoResponseDTO`. | — |
| 9 | Sistema | Retorna `200 OK + PartidoResponseDTO`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | El equipo indicado no participa en el partido. | `400 Bad Request` con `BusinessRuleException: Equipo no participa`. |


# RF-015: Registro de Tarjetas

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-015 | Registro de Tarjetas |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El organizador o árbitro registra una tarjeta (amarilla o roja) a un jugador durante un partido en curso. Se valida el tipo de tarjeta, la existencia del partido, que el jugador participe y que el partido esté en estado `EN_CURSO`. Las sanciones son responsabilidad del árbitro en el terreno; el sistema solo registra la información. | El árbitro / organizador envía `PUT /api/partidos/{id}/tarjetas` con `jugador` y `tipoTarjeta`. | Árbitro, Organizador | Token JWT válido. El partido debe existir y estar en estado `EN_CURSO`. El jugador debe participar en el partido. |

## Datos de entrada

| Nombre | Descripción | Tipo de dato | Reglas / Validación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `jugador` | Nombre o identificador del jugador sancionado | `String` | Debe pertenecer a uno de los equipos del partido. | Sí |
| `tipoTarjeta` | Tipo de tarjeta aplicada | `Enum` | `AMARILLA` o `ROJA`. | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de dato | Reglas / Aplicación | Obligatorio |
|--------|-------------|--------------|---------------------|-------------|
| `PartidoResponseDTO` | Partido actualizado con el evento de tarjeta registrado | `Object` | Refleja el evento de tarjeta agregado. | Sí |
| `message` | Mensaje de confirmación | `String` | Mensaje de éxito o descripción del error. | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Cliente (Árbitro / Organizador) | Envía `PUT /api/partidos/{id}/tarjetas` con `jugador` y `tipoTarjeta`. | — |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Árbitro | El jugador indicado no participa en el partido. | `400 Bad Request` con `BusinessRuleException: Jugador no participa`. |
| 2 | Árbitro | El partido no está en estado `EN_CURSO`. | `400 Bad Request` con `BusinessRuleException: Partido no en curso`. |


# Anexos

## Diagrama de Casos de Uso:

## Actores del Sistema:

- Estudiante: Se registra como jugador y puede ser capitán.
- Graduado: Se registra como jugador y puede ser capitán.
- Profesor: Se registra como jugador y puede ser capitán.
- Personal Administrativo: Se registra como jugador y puede ser capitán.
- Familiares: Se registra como jugador y puede ser capitán (requiere patrocinador de la comunidad académica).
- Capitán: Crea y administra un equipo.
- Organizador: Administra el torneo.
- Árbitro: Visualiza y registra información de los partidos que arbitra.
- Administrador: Control total del sistema.

## Mockup:

https://www.figma.com/make/wZnY6r0oYU309jDTJmjvGY/Mockup-Zeus-Codensa?fullscreen=1&t=8dnOrDdsbGlZBPMa-1&preview-route=%2Fauth%2Flogin

## Reglas de Negocio

| No. | Descripción |
|-----|-------------|
| RN-001 | Los participantes deben registrarse con correo institucional `nombre.apellido-inicial@escuelaing.edu.co`. El backend rechaza cualquier otro formato en `UserValidator`. |
| RN-002 | Familiares solo pueden participar si son patrocinados por un miembro de la comunidad académica registrada en el sistema. |
| RN-003 | Cada equipo debe tener mínimo 7 jugadores y máximo 20 jugadores (`TeamValidator`). |
| RN-004 | Un jugador no puede pertenecer a dos equipos simultáneamente durante el mismo torneo. |
| RN-005 | Más del 50% de los miembros de cada equipo deben ser de los programas autorizados (Ingeniería de Sistemas, IA, Ciberseguridad o Estadística). |
| RN-006 | Durante cada partido participan exactamente 7 jugadores por equipo (de los registrados en el plantel). |
| RN-007 | No se permiten cambios en la nómina del equipo una vez conformado e inscrito. |
| RN-008 | El pago se realiza fuera de la plataforma (NEQUI o efectivo al coordinador). La plataforma solo gestiona el comprobante. |
| RN-009 | Solo equipos con estado de inscripción `APPROVED` pueden participar en partidos (`MatchValidator` lo verifica). |
| RN-010 | El estado de inscripción sigue una máquina de estados estricta: `PENDING` → `IN_REVIEW` → `APPROVED` / `REJECTED`. Los saltos de estado son rechazados con excepción de negocio. |
| RN-011 | Solo `ADMINISTRADOR_SISTEMA` y `TOURNAMENT_ORGANIZER` pueden cambiar el estado de una inscripción (PUT). Solo `CAPTAIN` y `TOURNAMENT_ORGANIZER` pueden crearla (POST). |
| RN-012 | El reglamento del torneo prevalece sobre cualquier decisión del sistema. |
| RN-013 | Las sanciones (tarjetas) son responsabilidad del árbitro en el terreno; el sistema solo registra la información suministrada. |
| RN-014 | Los máximos goleadores se calculan de manera acumulativa durante todo el torneo (fases de grupo y eliminatorias). |

## Endpoints REST del Backend

> **Autenticación:** todas las rutas protegidas requieren el header `Authorization: Bearer <JWT>`. Un token expirado devuelve `401 Unauthorized` con mensaje JSON explicativo.

| Método | URI | Roles permitidos | Descripción |
|--------|-----|-----------------|-------------|
| `POST` | `/api/auth/login` | Público | Login — retorna JWT. |
| `POST` | `/api/users/register` | Público | Registro de nuevos usuarios. |
| `GET` | `/api/users/all` | Autenticado | Listar todos los usuarios. |
| `POST` | `/api/teams/create` | `CAPTAIN` | Crear equipo. |
| `GET` | `/api/teams/all` | Autenticado | Listar equipos. |
| `POST` | `/api/torneos/create` | `TOURNAMENT_ORGANIZER` | Crear torneo. |
| `PUT` | `/api/torneos/{id}/configurar` | `TOURNAMENT_ORGANIZER` | Configurar torneo (reglamento, fechas, horarios, canchas, sanciones). |
| `GET` | `/api/torneos/consulta/all` | Autenticado | Consultar todos los torneos. |
| `POST` | `/api/inscripciones/create` | `CAPTAIN`, `TOURNAMENT_ORGANIZER` | Crear inscripción / subir comprobante. |
| `PUT` | `/api/inscripciones/{id}/estado` | `ADMINISTRADOR_SISTEMA`, `TOURNAMENT_ORGANIZER` | Actualizar estado de inscripción. |
| `GET` | `/api/inscripciones/all` | Autenticado | Listar inscripciones. |
| `POST` | `/api/partidos/create` | `REFEREE`, `TOURNAMENT_ORGANIZER` | Registrar partido. |
| `PUT` | `/api/partidos/{id}/marcador` | `REFEREE`, `TOURNAMENT_ORGANIZER` | Actualizar marcador del partido. |
| `PUT` | `/api/partidos/{id}/alineacion` | `REFEREE`, `TOURNAMENT_ORGANIZER` | Registrar alineación de un equipo. |
| `PUT` | `/api/partidos/{id}/tarjetas` | `REFEREE`, `TOURNAMENT_ORGANIZER` | Registrar tarjeta a un jugador. |

## Abreviaturas

| Abreviatura | Significado |
|-------------|-------------|
| TECHCUP | Torneo de Fútbol del programa de Ingeniería |
| ECI | Escuela Colombiana de Ingeniería |
| RF | Requerimiento Funcional |
| RNF | Requerimiento No Funcional |
| RN | Regla de Negocio |
| API | Interfaz de Programación de Aplicaciones (Application Programming Interface) |
| REST | Transferencia de Estado Representacional (Representational State Transfer) |
| JWT | JSON Web Token |
| RBAC | Control de Acceso Basado en Roles (Role-Based Access Control) |
| HTTPS | Protocolo seguro de transferencia de hipertexto |
| SSL | Capa de Conexión Segura (Secure Sockets Layer) |
| CORS | Intercambio de Recursos de Origen Cruzado (Cross-Origin Resource Sharing) |
| MVC | Modelo-Vista-Controlador |
| DTO | Objeto de Transferencia de Datos (Data Transfer Object) |
| NEQUI | Aplicación móvil de pagos digitales |
| BCrypt | Algoritmo de cifrado de contraseñas |
| TTL | Tiempo de vida del token (Time To Live) |
| COP | Peso Colombiano |
| PJ | Partidos Jugados |
| PG | Partidos Ganados |
| PE | Partidos Empatados |
| PP | Partidos Perdidos |
| GF | Goles a Favor |
| GC | Goles en Contra |
| DG | Diferencia de Gol (GF − GC) |
| Pts | Puntos |
| SQL | Lenguaje de Consulta Estructurado (Structured Query Language) |
| UX | Experiencia de Usuario (User Experience) |
| UI | Interfaz de Usuario (User Interface) |
| SRS | Especificación de Requerimientos de Software |