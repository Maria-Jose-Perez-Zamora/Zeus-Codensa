# Requerimientos del sistema – TECHCUP Fútbol
**Nombre del Equipo:** Zeus-Codensa
**Integrantes:** Nicolas Sanchez, Maria Jose Perez, Andres Pineda, Diego Andrade, Stiven Pardo

## Requerimientos funcionales principales1. Gestionar torneos: crear, iniciar, finalizar y consultar torneos.
2. Registrar usuarios y jugadores por rol (estudiante, graduado, profesor, administrativo, familiar, capitán, organizador, árbitro, administrador).
3. Gestionar equipos: crear equipo, invitar jugadores y validar reglas de conformación (mínimo/máximo de jugadores y sin duplicidad).
4. Gestionar inscripciones y pagos: cargar comprobante y administrar estados (pendiente, en revisión, aprobado, rechazado).
5. Configurar torneo: reglamento, fechas clave, cierre de inscripciones, horarios, canchas y sanciones.
6. Registrar partidos: marcador, goleadores y tarjetas.
7. Calcular automáticamente tabla de posiciones y generar llaves eliminatorias.
8. Consultar información del torneo: calendario, resultados, estadísticas e información para árbitros.

# Requerimientos no funcionales principales

1. Diseño responsivo: la plataforma debe adaptarse correctamente a pantallas de celular y computador.
2. Seguridad y acceso: autenticación mediante **JWT (JSON Web Tokens)** con correo institucional `@escuelaing.edu.co` en formato `nombre.apellido-inicial@escuelaing.edu.co`. Los tokens tienen una vigencia de **1 hora (TTL)**. Rol-based access control (RBAC) enforces resource-level permissions.
3. Transporte seguro: la aplicación expone sus servicios exclusivamente por **HTTPS en el puerto 8443** con certificado SSL (PKCS12). Las peticiones entrantes por HTTP (puerto 8080) son redirigidas automáticamente a HTTPS.
4. CORS controlado: se admiten peticiones de los orígenes de desarrollo del frontend (`localhost:3000`, `localhost:4200`, `localhost:5173`).
5. Auditoría: registrar acciones relevantes para trazabilidad de cambios y operaciones mediante SLF4J/Logback.
6. Rendimiento: tiempos de respuesta adecuados en operaciones frecuentes (consulta de tabla, partidos, equipos e inscripciones).
7. Disponibilidad y confiabilidad: el sistema debe estar estable durante periodos críticos del torneo.
8. Arquitectura mantenible: backend por capas (MVC + servicios + validadores) con API REST, frontend pendiente de integración y base de datos PostgreSQL.
9. Integridad de datos: validaciones automáticas de reglas del torneo y transiciones de estado estrictas para evitar inconsistencias.


# Requerimientos funcionales detallados


# RF-001: Gestionar Torneos

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-001 | Gestión de Torneos |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema permite crear, configurar, iniciar y finalizar torneos. Un organizador puede definir la información básica del torneo (fechas, cantidad de equipos, costo) y cambiar su estado entre Borrador, Activo, En progreso y Finalizado. | El organizador accede a la sección de torneos y crea uno nuevo con los datos básicos. Luego puede iniciar el torneo cuando sea el momento. | Organizador | El usuario debe tener rol de organizador. |

## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| tournamentName | Nombre del torneo | Texto | Único en el sistema. No puede estar vacío. | Sí |
| numeroEquipos | Número de equipos máximo | Número | Mayor que 1 | Sí |
| costoInscripcion | Costo de la inscripción | Dinero | No puede ser negativo | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| ID del torneo | Identificador único asignado | Número | Generado automáticamente | Sí |
| Estado del torneo | Estado actual del torneo | Selección | Borrador, Activo, En progreso, Finalizado | Sí |
| Mensaje de confirmación | Confirmación de la acción realizada | Texto | Mensaje de éxito o error | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Envía `POST /api/tournaments` con `tournamentName`, `numeroEquipos`, `costoInscripcion` | Sin autenticación JWT: `401 Unauthorized`. Sin rol `TOURNAMENT_ORGANIZER`: `403 Forbidden` |
| 2 | Sistema (`TournamentValidator`) | Valida que el nombre no esté duplicado y `numeroEquipos > 1` y `costoInscripcion >= 0` | Datos inválidos: `400 Bad Request` con mensaje |
| 3 | Sistema | Crea el torneo con estado `OPEN` | - |
| 4 | Sistema | Responde con el DTO del torneo creado | - |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Envía `PUT /api/tournaments/{id}` para actualizar datos | Torneo no encontrado: `404 Not Found` |
| 2 | Sistema | Valida rol `TOURNAMENT_ORGANIZER` | Sin permiso: `403 Forbidden` |


# RF-002: Registrar Usuarios y Jugadores

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-002 | Registro de Usuarios y Jugadores |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Cada participante se registra en el sistema con su **correo institucional** en formato `nombre.apellido-inicial@escuelaing.edu.co`. El sistema valida el formato del correo en el backend (`UserValidator`) antes de crear el usuario. Los roles disponibles en el sistema son: `PLAYER`, `CAPTAIN`, `REFEREE`, `TOURNAMENT_ORGANIZER`, `ADMINISTRADOR_SISTEMA`. | El usuario envía una petición `POST /api/users` con sus datos. El sistema valida el formato del correo, la contraseña (mínimo 6 caracteres), la posición y número de dorsal (obligatorios para PLAYER y CAPTAIN). | PLAYER / CAPTAIN / REFEREE / TOURNAMENT_ORGANIZER / ADMINISTRADOR_SISTEMA | Ninguna (endpoint público) |

## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| name | Nombre del usuario | Texto | No puede estar vacío | Sí |
| email | Correo institucional | Correo | Debe seguir el patrón `nombre.apellido-inicial@escuelaing.edu.co` | Sí |
| password | Contraseña | Texto | Mínimo 6 caracteres | Sí |
| role | Rol del sistema | Enum | `PLAYER`, `CAPTAIN`, `REFEREE`, `TOURNAMENT_ORGANIZER`, `ADMINISTRADOR_SISTEMA` | Sí |
| position | Posición de juego | Texto | Obligatorio para `PLAYER` y `CAPTAIN` | Condicional |
| jerseyNumber | Número de dorsal | Número | Mayor que 0. Obligatorio para `PLAYER` y `CAPTAIN` | Condicional |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| ID de usuario | Identificador único | Número | Generado automáticamente | Sí |
| Perfil completado | Confirmación de registro | Booleano | Sí/No | Sí |
| Mensaje de confirmación | Confirmación de créación | Texto | Mensaje de éxito | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Jugador | Envía `POST /api/users` con `name`, `email`, `password`, `role` | - |
| 2 | Sistema (`UserValidator`) | Valida que `email` cumpla `nombre.apellido-inicial@escuelaing.edu.co` | Formato incorrecto: `400 Bad Request` |
| 3 | Sistema | Valida que `password` tenga al menos 6 caracteres | Contraseña corta: `400 Bad Request` |
| 4 | Sistema | Para roles `PLAYER`/`CAPTAIN`: valida que `position` y `jerseyNumber` estén presentes | Datos faltantes: `400 Bad Request` |
| 5 | Sistema | Valida que el correo no esté duplicado en el sistema | Correo existente: `400 Bad Request` |
| 6 | Sistema | Crea el usuario según su rol (via `UserFactory`) y responde con el DTO | - |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Envía `POST /api/auth` con `email` y `password` | - |
| 2 | Sistema (`AuthService`) | Busca usuario por correo y contraseña | Credenciales inválidas: `400 BusinessRuleException` |
| 3 | Sistema (`JwtService`) | Genera un JWT firmado (HS256) con el rol del usuario como claim, TTL 1h | - |
| 4 | Sistema | Retorna `LoginResponseDTO` con el token y datos del usuario | - |


# RF-003: Gestionar Equipos (Creación, invitaciones, búsqueda de jugadores)

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-003 | Gestión de Equipos |

## Prototipos e Interfaces Visuales (Mockups)

🔗 **Link de Figma:** [https://tag-skit-64046987.figma.site/](https://tag-skit-64046987.figma.site/)

![img_1.png](img_1.png)

## Funcionalidad Restante

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Los capitanes crean equipos con nombre, escudo, colores de uniforme. Invitan jugadores a sus equipos respetando las reglas: mínimo 7 jugadores, máximo 12, sin duplicidad de jugador en equipos, más de la mitad de los miembros deben ser de los programas de Ingeniería de Sistemas, IA, Ciberseguridad y Estadística. Los capitanes pueden buscar jugadores disponibles por posición, semestre, edad, género, nombre e identificación. | El capitán accede a su panel, crea el equipo ingresando nombre, carga escudo e indica colores. Luego busca jugadores por criterios y envía invitaciones. Los jugadores aceptan o rechazan iniciativas. | Capitán | El usuario debe tener o cambiar a rol de capitán. Debe existir un torneo activo. |

## Datos de entrada (Crear equipo)

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| teamName | Nombre del equipo | Texto | Único en el sistema | Sí |
| shieldUrl | URL del escudo del equipo | Texto | URL válida | No |
| uniformColor | Color del uniforme | Texto | Libre | No |
| playerEmails | Lista de correos de jugadores | Lista de Texto | Entre **7 y 20** correos. Sin duplicados. Cada correo debe pertenecer a un usuario registrado. Un jugador no puede pertenecer a dos equipos simultáneamente. | Sí |

## Datos de entrada (Búsqueda de jugadores)

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| Posición | Filtro por posición de juego | Selección | Portero, Defensa, Volante, Delantero | No |
| Semestre | Filtro por semestre académico | Número | Aplica solo a estudiantes | No |
| Edad | Filtro por edad | Número | Rango mínimo y máximo | No |
| Género | Filtro por género | Selección | Masculino, Femenino, Otro | No |
| Nombre | Búsqueda por nombre | Texto | Búsqueda parcial permitida | No |
| Identificación | Búsqueda por ID | Texto | Búsqueda exacta | No |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| ID del equipo | Identificador único | Número | Generado automáticamente | Sí |
| Lista de jugadores disponibles | Resultados de búsqueda | Lista | Jugadores marcados como disponibles | Sí |
| Estado de invitación | Estado de la invitación enviada | Selección | Pendiente, Aceptada, Rechazada | Sí |
| Mensaje de confirmación | Confirmación de acción | Texto | Mensaje de éxito o error | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Envía `POST /api/teams` con `teamName` y lista `playerEmails` | Sin rol `CAPTAIN`: `403 Forbidden` |
| 2 | Sistema (`TeamValidator`) | Valida que el `teamName` sea único | Nombre duplicado: `400 Bad Request` |
| 3 | Sistema | Valida que la lista tenga entre 7 y 20 correos, sin duplicados | Tamaño inválido o duplicados: `400 Bad Request` |
| 4 | Sistema | Valida que ningún correo pertenezca ya a otro equipo | Jugador repetido: `400 Bad Request` con nombre del correo |
| 5 | Sistema (`TeamService`) | Busca en `DataStorage` los usuarios con los correos provistos y los asocia al equipo | Correo no registrado: jugador no se suma (sin error bloqueante) |
| 6 | Sistema | Agrega el equipo a `DataStorage` y retorna el DTO | - |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Intenta agregar un jugador ya en otro equipo | `TeamValidator` rechaza la petición completa con `400` |


> **Endpoint REST:** `POST /api/auth` (público) — Retorna JWT Bearer con TTL 1 h.

# RF-004: Gestionar Inscripciones y Pagos

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-004 | Gestión de Inscripciones y Pagos |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El capitán sube el comprobante de pago. El organizador o administrador cambia el estado siguiendo la **máquina de estados estricta**: `PENDING` → `IN_REVIEW` → `APPROVED` / `REJECTED`. Las transiciones fuera de secuencia son rechazadas. Solo equipos con estado `APPROVED` pueden participar en partidos. | `POST /api/registrations` (CAPTAIN, TOURNAMENT_ORGANIZER) para crear/subir comprobante; `PUT /api/registrations/{id}` (ADMINISTRADOR_SISTEMA, TOURNAMENT_ORGANIZER) para actualizar el estado. | Capitán (sube comprobante), Organizador / Administrador (cambia estado) | Torneo activo. Equipo conformado. |

## Datos de entrada (Capitán)

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| teamName | Nombre del equipo | Texto | Debe existir en el sistema | Sí |
| tournamentName | Nombre del torneo | Texto | El torneo debe existir y estar en estado `OPEN` | Sí |
| comprobantePagoUrl | URL del comprobante de pago | Texto | No puede estar vacía | Sí |

## Datos de entrada (Organizador/Administrador — Actualización de Estado)

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| nuevoEstado | Nuevo estado de la inscripción | Enum | `IN_REVIEW`, `APPROVED` o `REJECTED` según la fase actual | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| Estado de inscripción | Estado actual del equipo | Enum | `PENDING`, `IN_REVIEW`, `APPROVED`, `REJECTED` | Sí |
| Mensaje de confirmación | Confirmación de acción | Texto | Mensaje de éxito o error | Sí |
| Notificación al capitán | Notificación de aceptación/rechazo | Correo electrónico | Enviado automáticamente | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Capitán | Envía `POST /api/registrations` con `teamName`, `tournamentName`, `comprobantePagoUrl` | Sin rol `CAPTAIN`: `403 Forbidden` |
| 2 | Sistema (`RegistrationValidator`) | Valida que el equipo exista en `DataStorage` | Equipo no encontrado: `400 Bad Request` |
| 3 | Sistema | Valida que el torneo exista y esté en estado `OPEN` | Torneo cerrado o inexistente: `400 Bad Request` |
| 4 | Sistema | Valida que el equipo no tenga ya una inscripción en ese torneo | Duplicado: `400 Bad Request` |
| 5 | Sistema | Crea la inscripción con estado inicial `PENDING` | - |
| 6 | Organizador/Admin | Envía `PUT /api/registrations/{id}` con `nuevoEstado: IN_REVIEW` | Sin rol `ADMINISTRADOR_SISTEMA`/`TOURNAMENT_ORGANIZER`: `403 Forbidden` |
| 7 | Sistema (`RegistrationService`) | Valida transición `PENDING` → `IN_REVIEW` | Transición no válida: `BusinessRuleException` |
| 8 | Organizador/Admin | Envía `PUT /api/registrations/{id}` con `nuevoEstado: APPROVED` o `REJECTED` | - |
| 9 | Sistema | Valida transición `IN_REVIEW` → `APPROVED`/`REJECTED` | - |
| 10 | Sistema | Estado queda en `APPROVED` o `REJECTED` (estado final, no modificable) | Intento de modificar estado final: `BusinessRuleException` |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Intenta rechazar una inscripción ya aprobada | `RegistrationService` rechaza con `BusinessRuleException: estado definitivo` |


# RF-005: Configurar Torneo (Reglamento, fechas, horarios, canchas, sanciones)

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-005 | Configuración del Torneo |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Después de crear el torneo, el organizador define el reglamento, fechas importantes (cierre de inscripciones, inicio de fase de grupos), horarios de partidos, canchas disponibles y sanciones. Esta información se publica en la plataforma para que todos los participantes la vean. | El organizador accede a la sección "Configuración" del torneo y completa cada campo. Los cambios se guardan y se publican inmediatamente en la sección de información del torneo. | Organizador | Torneo debe existir y estar en estado "Borrador" o "Activo". |

## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| Reglamento | Reglas del torneo | Texto largo | Máximo 10000 caracteres | Sí |
| Fecha cierre inscripciones | Fecha límite para inscribirse | Fecha | Formato YYYY-MM-DD, anterior a fecha de inicio | Sí |
| Fecha inicio fase de grupos | Fecha de inicio de partidos | Fecha | Posterior a fecha inicial del torneo | Sí |
| Horarios de partidos | Horas disponibles para partidos | Lista de horas | Formato HH:MM | Sí |
| Canchas | Listado de canchas disponibles | Texto | Nombre y ubicación de cada cancha | Sí |
| Sanciones | Definición de tarjetas y expulsiones | Texto largo | Máximo 5000 caracteres | Sí |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| Confirmación de configuración | Confirmación de guardado | Booleano | Sí/No | Sí |
| Mensaje de éxito | Confirmación de cambios guardados | Texto | Mensaje de éxito | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador | Accede a "Configuración del torneo" | Organizador no autenticado: redirigir a login |
| 2 | Organizador | Completa reglamento | Información incompleta: mostrar campos obligatorios |
| 3 | Organizador | Define fechas importantes (cierre inscripciones, inicio fase) | Fechas inválidas: mostrar error |
| 4 | Organizador | Define horarios disponibles para partidos | - |
| 5 | Organizador | Ingresa canchas con nombres y ubicaciones | - |
| 6 | Organizador | Define sanciones (tarjetas, expulsiones) | - |
| 7 | Sistema | Valida información completada | - |
| 8 | Sistema | Guarda configuración y la publica en la plataforma | - |
| 9 | Sistema | Confirma cambios al organizador | - |


# RF-006: Registrar Partidos y Resultados

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-006 | Registro de Partidos y Resultados |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El organizador registra los resultados de los partidos: marcador final, goleadores (con minuto de gol), tarjetas amarillas y tarjetas rojas. Esta información se actualiza en el sistema y automáticamente se recalcula la tabla de posiciones. | El organizador accede a "Registro de partidos", selecciona el partido, ingresa el marcador, goleadores y tarjetas, y confirma. El sistema calcula puntos automáticamente. | Organizador | Partido debe estar programado en el torneo. |

## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| ID del partido | Identificador del partido | Número | Generado automáticamente | Sí |
| Equipo 1 | Equipo local | Selección | Listado de equipos del torneo | Sí |
| Equipo 2 | Equipo visitante | Selección | Listado de equipos del torneo | Sí |
| Goles Equipo 1 | Cantidad de goles marcados | Número | Mínimo 0 | Sí |
| Goles Equipo 2 | Cantidad de goles marcados | Número | Mínimo 0 | Sí |
| Goleadores | Lista de goleadores | Nombre + minuto | Formato: "Nombre (minuto)" | No |
| Tarjetas amarillas | Jugadores con tarjeta amarilla | Nombre + minuto | Formato: "Nombre (minuto)" | No |
| Tarjetas rojas | Jugadores expulsados | Nombre + minuto | Formato: "Nombre (minuto)" | No |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| ID del partido registrado | Confirmación de registro | Número | Identificador único | Sí |
| Tabla actualizada | Tabla con nueva información | Tabla | Calculada automáticamente | Sí |
| Mensaje de confirmación | Confirmación de registro | Texto | Mensaje de éxito | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Organizador/Árbitro | Envía `POST /api/matches` con `homeTeam`, `awayTeam`, `tournamentName`, `matchDate` | Sin rol `REFEREE`/`TOURNAMENT_ORGANIZER`: `403 Forbidden` |
| 2 | Sistema (`MatchValidator`) | Valida que `homeTeam != awayTeam` | Mismo equipo: `400 Bad Request` |
| 3 | Sistema | Valida que el torneo exista en `DataStorage` | Torneo inexistente: `400 Bad Request` |
| 4 | Sistema | Verifica que ambos equipos tengan inscripción con estado `APPROVED` en ese torneo | Equipo no aprobado: `400 Bad Request` |
| 5 | Sistema | Crea el partido y lo guarda en `DataStorage` | - |
| 6 | Organizador/Árbitro | Envía `PUT /api/matches/{id}` para registrar goles, goleadores y tarjetas | - |
| 7 | Sistema | Actualiza el partido con el resultado | Partido no encontrado: `404 Not Found` |


# RF-007: Calcular Tabla de Posiciones y Generar Llaves Eliminatorias

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-007 | Tabla de Posiciones y Llaves Eliminatorias |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| El sistema calcula automáticamente la tabla de posiciones en tiempo real según resultados ingresados. Muestra partidos jugados, ganados, empatados, perdidos, goles a favor, goles en contra, diferencia de gol y puntos. Después de la fase de grupos, el sistema genera automáticamente las llaves eliminatorias (cuartos de final, semifinal, final) de manera aleatoria. | Después de cada resultado ingresado, el sistema recalcula. Al finalizar fase de grupos, genera llaves automáticamente según la tabla. | Sistema/Organizador (para validar) | Deben haber resultados registrados. Fase de grupos debe estar finalizada para generar llaves. |

## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| tournament | Nombre del torneo (path param) | Texto | Debe existir en `DataStorage` | Sí |
| phase | Fase eliminatoria (path param) | Texto | Valores válidos: `quarterfinals`, `semifinals`, `final` | Sí (solo para brackets) |

## Datos de salida

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| Tabla de posiciones | Tabla ordenada por puntos | Tabla | Ordenada por puntos, diferencia gol, goles a favor | Sí |
| Partidos jugados | Cantidad de partidos | Número | Calculado automáticamente | Sí |
| Partidos ganados | Cantidad de partidos ganados | Número | Calculado automáticamente | Sí |
| Partidos empatados | Cantidad de partidos empatados | Número | Calculado automáticamente | Sí |
| Partidos perdidos | Cantidad de partidos perdidos | Número | Calculado automáticamente | Sí |
| Goles a favor | Cantidad de goles marcados | Número | Calculado automáticamente | Sí |
| Goles en contra | Cantidad de goles recibidos | Número | Calculado automáticamente | Sí |
| Diferencia de gol | GF - GC | Número | Calculado automáticamente | Sí |
| Puntos | Puntos totales (3 por victoria, 1 por empate) | Número | Calculado automáticamente | Sí |
| Llaves eliminatorias | Estructura de cuartos, semis y final | Árbol visual | Generado automáticamente | Sí |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Recibe registro de nuevo resultado | Resultado no válido: no actualizar |
| 2 | Sistema | Recalcula puntos del equipo ganador (+3 puntos) | - |
| 3 | Sistema | Recalcula puntos de equipos empatados (+1 punto cada) | - |
| 4 | Sistema | Recalcula goles a favor y goles en contra | - |
| 5 | Sistema | Ordena tabla por puntos (descendente), luego por diferencia gol | - |
| 6 | Sistema | Publica tabla actualizada en para consulta de todos | - |
| 7 | Sistema | Valida si fase de grupos terminó | Fase aún activa: guardar para luego |
| 8 | Sistema | Genera llaves eliminatorias de manera aleatoria entre los 8/16 mejores | Número de equipos insuficiente: mostrar error |
| 9 | Sistema | Asigna fechas y horarios a cada llave | - |
| 10 | Sistema | Publica llaves en la plataforma | - |

## Flujo alterno

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Sistema | Si hay empate en puntos, diferencia gol desempata | - |
| 2 | Sistema | Si aún hay empate, goles a favor desempata | - |
| 3 | Organizador | Puede revisar y validar llaves generadas | - |


# RF-008: Consultar Información del Torneo (Calendario, Resultados, Estadísticas)

## Funcionalidad

| Código | Nombre |
|--------|--------|
| RF-008 | Consulta de Información del Torneo |

| Descripción | Cómo se ejecuta | Actor principal | Precondiciones |
|-------------|-----------------|-----------------|-----------------|
| Todos los usuarios autenticados pueden consultar: calendario de partidos programados, resultados de partidos finalizados, tabla de posiciones, llaves eliminatorias por fase y estadísticas (máximos goleadores e historial por equipo). | Los endpoints son públicos bajo `/api/tournaments/query/{tournament}/...`. Los datos se calculan dinámicamente desde `DataStorage`. | Cualquier usuario autenticado | Token JWT válido. Torneo debe existir en el sistema. |

## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas/Aplicación | Obligatorio |
|--------|-------------|----------------|-------------------|-------------|
| tournament | Nombre del torneo (path param) | Texto | Debe existir en `DataStorage` | Sí |
| team | Nombre del equipo, solo para historial (path param) | Texto | Debe existir en el torneo | Condicional |

## Datos de salida

| Endpoint | Descripción | Datos devueltos |
|----------|-------------|-----------------|
| `GET /api/tournaments/query/{tournament}/calendar` | Partidos programados con status `SCHEDULED` | `id`, `homeTeam`, `awayTeam`, `matchDate`, `status` |
| `GET /api/tournaments/query/{tournament}/results` | Partidos finalizados con status `FINISHED` | `id`, `homeTeam`, `awayTeam`, `homeScore`, `awayScore`, `matchDate` |
| `GET /api/tournaments/query/{tournament}/standings` | Tabla de posiciones calculada por `StandingService` | PJ, PG, PE, PP, GF, GC, DG, Pts |
| `GET /api/tournaments/query/{tournament}/brackets/{phase}` | Llaves eliminatorias por fase | Emparejamientos según `BracketService` |
| `GET /api/tournaments/query/{tournament}/scorers` | Máximos goleadores (partidos `FINISHED`) | `playerEmail`, `goals` (orden descendente) |
| `GET /api/tournaments/query/{tournament}/history/{team}` | Historial de un equipo | `id`, `opponent`, `venue`, `homeScore`, `awayScore`, `result`, `date` |

## Flujo básico

| Paso | Actor | Descripción | Excepciones |
|------|-------|-------------|-------------|
| 1 | Usuario | Envía `GET /api/tournaments/query/{tournament}/calendar` | Sin JWT válido: `401 Unauthorized` |
| 2 | Sistema | Filtra partidos con `status = SCHEDULED` y los retorna | Sin partidos programados: mensaje informativo |
| 3 | Usuario | Envía `GET /api/tournaments/query/{tournament}/results` | - |
| 4 | Sistema | Filtra partidos con `status = FINISHED` y retorna marcadores | Sin resultados: mensaje informativo |
| 5 | Usuario | Envía `GET /api/tournaments/query/{tournament}/standings` | - |
| 6 | Sistema (`StandingService`) | Calcula tabla sumando victorias (+3), empates (+1), goles y ordena descendente | Tabla vacía: lista vacía |
| 7 | Usuario | Envía `GET /api/tournaments/query/{tournament}/scorers` | - |
| 8 | Sistema (`StatisticsService`) | Acumula goles por jugador en partidos `FINISHED` y ordena descendente | Sin goles registrados: mensaje informativo |
| 9 | Usuario | Envía `GET /api/tournaments/query/{tournament}/history/{team}` | - |
| 10 | Sistema | Filtra partidos del equipo y calcula `WIN`/`DRAW`/`LOSS` | Sin historial: mensaje informativo |
| 11 | Usuario | Envía `GET /api/tournaments/query/{tournament}/brackets/{phase}` | - |
| 12 | Sistema (`BracketService`) | Genera emparejamientos según la fase solicitada (`quarterfinals`, `semifinals`, `final`) | Fase inválida o sin equipos suficientes: error |



# Anexos

## Diagrama de Casos de Uso:

## Actores del Sistema:

- Estudiante: Se registra como jugador y puede ser capitán.

- Graduado: Se registra como jugador y puede ser capitán.

- Profesor: Se registra como jugador y puede ser capitán.

- Personal Administrativo: Se registra como jugador y puede ser capitán.

- Familiares: Se registra como jugador y puede ser capitán.

- Capitán: Crea y administra un equipo.

- Organizador: Administra el torneo.

- Árbitro: Visualiza la información de los partidos a arbitrar.

- Administrador: Control total del sistema.

[Diagrama-Casos-De-Uso3.asta](..%2Fuml%2FDiagrama-Casos-De-Uso3.asta)

![Diagrama-Casos-De-Uso.png](..%2Fimages%2FDiagrama-Casos-De-Uso.png)

## Mockup:

https://tag-skit-64046987.figma.site/

## Reglas de Negocio

| No. | Descripción |
|-----|-------------|
| RN-001 | Los participantes deben registrarse con correo institucional `nombre.apellido-inicial@escuelaing.edu.co`. El backend rechaza cualquier otro formato en `UserValidator`. |
| RN-002 | Familiares solo pueden participar si son patrocinados por un miembro de la comunidad académica registrada en el sistema. |
| RN-003 | Cada equipo debe tener mínimo 7 jugadores y máximo 20 jugadores (`TeamValidator`). |
| RN-004 | Un jugador no puede pertenecer a dos equipos simultáneamente durante el mismo torneo. |
| RN-005 | Más del 50% de los miembros de cada equipo deben ser de los programas autorizados. |
| RN-006 | Durante cada partido participan exactamente 7 jugadores por equipo (de los 12 totales). |
| RN-007 | No se permiten cambios de equipo una vez conformado. |
| RN-008 | El pago se realiza fuera de la plataforma (NEQUI o efectivo al coordinador). |
| RN-009 | Solo equipos con estado de inscripción `APPROVED` pueden participar en partidos (`MatchValidator` lo valida). |
| RN-010 | El estado de inscripción sigue una máquina de estados estricta: `PENDING` → `IN_REVIEW` → `APPROVED` / `REJECTED`. Saltos de estado son rechazados con excepción de negocio. |
| RN-011 | Solo `ADMINISTRADOR_SISTEMA` y `TOURNAMENT_ORGANIZER` pueden cambiar el estado de una inscripción (PUT). Solo `CAPTAIN` y `TOURNAMENT_ORGANIZER` pueden crearla (POST). |
| RN-012 | El reglamento del torneo prevalece sobre cualquier decisión del sistema. |
| RN-013 | Las sanciones (tarjetas) son responsabilidad del árbitro en el terreno; el sistema solo registra la información. |
| RN-014 | Los máximos goleadores se calculan de manera acumulativa durante todo el torneo (fases de grupo y eliminatorias). |

## Endpoints REST del Backend

| Método | URI | Acceso | Descripción |
|--------|-----|--------|-------------|
| POST | `/api/auth` | Público | Login — retorna JWT |
| POST | `/api/users` | Público | Registro de nuevos usuarios |
| GET | `/api/users` | Autenticado | Listar todos los usuarios |
| POST | `/api/teams` | CAPTAIN | Crear equipo |
| GET | `/api/teams` | Autenticado | Listar equipos |
| POST | `/api/tournaments` | TOURNAMENT_ORGANIZER | Crear torneo |
| PUT | `/api/tournaments/{id}` | TOURNAMENT_ORGANIZER | Actualizar torneo |
| GET | `/api/tournaments` | Público | Consultar torneos |
| POST | `/api/registrations` | CAPTAIN, TOURNAMENT_ORGANIZER | Crear inscripción / subir comprobante |
| PUT | `/api/registrations/{id}` | ADMINISTRADOR_SISTEMA, TOURNAMENT_ORGANIZER | Actualizar estado de inscripción |
| GET | `/api/registrations` | Autenticado | Listar inscripciones |
| POST | `/api/matches` | REFEREE, TOURNAMENT_ORGANIZER | Registrar partido |
| PUT | `/api/matches/{id}` | REFEREE, TOURNAMENT_ORGANIZER | Actualizar partido |
| GET | `/api/matches` | Autenticado | Listar partidos |

> **Autenticación:** todas las rutas protegidas requieren header `Authorization: Bearer <JWT>`. Un token expirado devuelve `401 Unauthorized` con mensaje JSON explicativo.


## Abreviaturas

| Abreviatura | Significado |
|-------------|-------------|
| TECHCUP | Torneo de Fútbol del programa de Ingeniería |
| ECI | Escuela Colombiana de Ingeniería |
| RF | Requerimiento Funcional |
| RNF | Requerimiento No Funcional |
| RN | Regla de Negocio |
| API | Interfaz de Programación de Aplicaciones |
| REST | Transferencia de Estado Representacional |
| NEQUI | Aplicación móvil de pago de dinero |
| PJ | Partidos Jugados |
| PG | Partidos Ganados |
| PE | Partidos Empatados |
| PP | Partidos Perdidos |
| GF | Goles a Favor |
| GC | Goles en Contra |
| DG | Diferencia de Gol |
| Pts | Puntos |
| SQL | Lenguaje de Consulta Estructurado |
| UX | Experiencia de Usuario |
| UI | Interfaz de Usuario |