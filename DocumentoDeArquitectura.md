# Documento de Arquitectura

## 1. Descripción del Servicio

### 1.1 Descripción del Servicio
Zeus-Codensa es el servicio backend de la plataforma TechCup y actúa como núcleo de procesamiento del torneo. Su responsabilidad es recibir solicitudes desde clientes web o móviles, aplicar las reglas del negocio y devolver respuestas estandarizadas a través de una API REST.

El servicio cubre el flujo operativo principal del campeonato: autenticación de usuarios, gestión de perfiles, administración de equipos y jugadores, creación y consulta de torneos, proceso de inscripción y gestión de partidos. De esta forma, concentra en un solo punto la lógica funcional y evita que las reglas críticas queden distribuidas en varios clientes.

Arquitectónicamente, Zeus-Codensa separa presentación, negocio y persistencia para mejorar mantenibilidad, trazabilidad y escalabilidad. Esta organización permite evolucionar funcionalidades de forma controlada, reforzar validaciones y mantener un manejo uniforme de errores y respuestas en todo el sistema.

### 1.2 Diagrama de contexto
![Diagrama de contexto](docs/images/DiagramaContexto.png)

El diagrama de contexto presenta a Zeus-Codensa como sistema central y su relación con actores externos. Permite delimitar el alcance del servicio y visualizar los flujos principales de entrada (solicitudes) y salida (respuestas y errores), sin entrar al detalle interno de implementación.

En el contexto definido se identifican cinco actores principales: Estudiante, Capitán, Árbitro, Administrador y Organizador. El servicio también interactúa con dos sistemas externos: Correo Institucional (autenticación) y NEQUI/Efectivo (validación de comprobantes de pago).

Las interacciones mostradas en el diagrama son:
- Estudiante: registrarse.
- Capitán: gestionar alineación, invitaciones y pagos.
- Árbitro: consultar partidos asignados.
- Administrador: gestión del sistema, usuarios, roles y auditoría.
- Organizador: registrar partidos.

Adicionalmente, el diagrama explicita reglas de negocio del torneo: mínimo y máximo de jugadores por equipo, restricción de un jugador por equipo, distribución de programas académicos por equipo, número de jugadores por partido, estados del pago (pendiente, en revisión, aprobado, rechazado) y bloqueo de cambios de equipo después de iniciar el torneo.

## 2. Tecnologías a Usar

### 2.1 Tecnologías del servicio
- Lenguaje: Java 21.
- Framework principal: Spring Boot 3.3.4.
- Frameworks y módulos: Spring Web, Spring Security, Spring Data JPA.
- Base de datos: PostgreSQL.
- Autenticación y autorización: JWT (JJWT).
- Documentación API: Springdoc OpenAPI (Swagger UI).
- Validación de datos: Jakarta Validation API.
- Testing: JUnit 5 y Mockito.
- Cobertura de pruebas: JaCoCo.
- Herramienta de construcción y dependencias: Maven.

### 2.2 Justificación por tecnología
- Java 21: proporciona rendimiento estable, tipado fuerte y compatibilidad con el ecosistema empresarial de Spring.
- Spring Boot 3.3.4: acelera la construcción de servicios REST mediante autoconfiguración y convenciones de desarrollo.
- Spring Web: facilita la exposición de endpoints HTTP con estructura clara de controladores y respuestas.
- Spring Security: permite aplicar control de acceso por autenticación/autorización de forma centralizada.
- Spring Data JPA: simplifica el acceso a datos y reduce código repetitivo en repositorios.
- PostgreSQL: ofrece consistencia transaccional, integridad relacional y buen desempeño para datos estructurados del torneo.
- JWT (JJWT): habilita autenticación stateless para proteger endpoints y manejar sesiones de forma segura.
- Springdoc OpenAPI (Swagger UI): genera documentación interactiva de la API y mejora la trazabilidad de contratos.
- Jakarta Validation API: asegura validación de campos de entrada antes de ejecutar lógica de negocio.
- JUnit 5 y Mockito: permiten pruebas unitarias y de integración con aislamiento de dependencias.
- JaCoCo: mide cobertura de pruebas para identificar módulos críticos sin validación suficiente.
- Maven: gestiona dependencias, ciclos de build y ejecución estandarizada del proyecto.

## 3. Funcionalidades

### 3.1 Funcionalidades identificadas
- Registro e inicio de sesión de usuarios.
- Gestión de usuarios y roles.
- Creación y administración de equipos.
- Gestión de jugadores por equipo.
- Creación, configuración y consulta de torneos.
- Proceso de inscripción de equipos al torneo.
- Registro y actualización de partidos.
- Consulta de partidos asignados.
- Gestión de alineaciones.
- Control disciplinario (tarjetas y sanciones).
- Validación de comprobantes de pago.
- Auditoría y administración general del sistema.

### 3.2 Casos de uso

![alt text](docs/images/Diagrama-Casos-De-Uso.png)

- CU-01 Registrar usuario (Actor: Estudiante).
- CU-02 Autenticar usuario con correo institucional (Actor: Estudiante, Capitán, Árbitro, Administrador, Organizador).
- CU-03 Crear y administrar equipo (Actor: Capitán).
- CU-04 Gestionar jugadores de equipo (Actor: Capitán).
- CU-05 Registrar inscripción y pago de equipo (Actor: Capitán).
- CU-06 Validar comprobante y estado de inscripción (Actor: Administrador/Organizador).
- CU-07 Crear y gestionar torneos (Actor: Organizador).
- CU-08 Registrar partidos y resultados (Actor: Organizador).
- CU-09 Consultar partidos asignados (Actor: Árbitro).
- CU-10 Gestionar usuarios, roles y auditoría (Actor: Administrador).



 