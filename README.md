# Zeus-Codensa (TECHCUP Fútbol)
**Nombre del Equipo:** Zeus-Codensa
**Integrantes:** Nicolas Sanchez, Maria Jose Perez, Andres Pineda, Diego Andrade, Stiven Pardo
**Materia:** Diseño y Arquitectura de Software
# Requerimientos del sistema – TECHCUP Fútbol

## Requerimientos funcionales

1. El sistema debe permitir que los usuarios se registren, inicien sesión y puedan completar su perfil como jugador.

2. El sistema debe permitir que un capitán cree un equipo, agregue jugadores y administre la información del equipo.

3. El sistema debe permitir buscar jugadores disponibles y enviarles invitaciones para unirse a un equipo.

4. El sistema debe permitir al organizador crear y configurar el torneo con su información básica (fechas, número de equipos y costo).

5. El sistema debe permitir subir comprobantes de pago para la inscripción del equipo y permitir al organizador aprobar o rechazar la inscripción.

6. El sistema debe permitir registrar la información de los partidos como horario, cancha, alineaciones, resultados y sanciones.

7. El sistema debe generar automáticamente la tabla de posiciones y las llaves eliminatorias del torneo.

8. El sistema debe permitir consultar información del torneo como calendario de partidos, resultados y estadísticas.

## Requerimientos no funcionales

1. El sistema debe manejar autenticación de usuarios y control de roles para proteger la información.

2. La plataforma debe ser una aplicación web sencilla y fácil de usar.

3. El sistema debe permitir que varios usuarios puedan usar la plataforma al mismo tiempo.

4. La información del torneo debe estar disponible durante todo el tiempo que se realice la competencia.

5. El sistema debe desarrollarse con Spring Boot en el backend, React en el frontend y PostgreSQL como base de datos.

---

# Especificacion de requerimientos

## 1. El sistema debe permitir que los usuarios se registren, inicien sesión y puedan completar su perfil como jugador.
# Documento de análisis de requerimientos

## Requerimiento funcional

| Campo | Descripción |
|------|-------------|
| Identificador | RF1 |
| Nombre del requerimiento | Registro e inicio de sesión de usuarios |
| Descripción | El sistema debe permitir que los usuarios se registren, inicien sesión y completen su perfil como jugador dentro de la plataforma. |
| Actor principal | Usuario |
| Precondiciones | El usuario debe tener acceso a internet y un correo electrónico válido. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|--------------|---------------------|-------------|
| nombre | Nombre del usuario | texto | Solo letras | Sí |
| correo | Correo electrónico del usuario | email | Debe tener formato válido | Sí |
| contraseña | Contraseña para acceder al sistema | texto | Mínimo 6 caracteres | Sí |
| posición | Posición de juego | texto | Debe seleccionar una posición válida | No |
| número_dorsal | Número del jugador | número | Debe estar entre 1 y 99 | No |
| foto | Foto de perfil | imagen | Formato jpg o png | No |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| mensaje_confirmación | Mensaje que indica que el registro o inicio de sesión fue exitoso | texto | Se muestra al finalizar el proceso | Sí |
| perfil_usuario | Información del usuario guardada en el sistema | objeto | Contiene los datos del perfil del jugador | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El usuario accede a la opción de registro en la plataforma. | Si la página no carga correctamente, el sistema muestra un mensaje de error. |
| 2 | El usuario ingresa los datos solicitados para el registro. | Si algún campo obligatorio está vacío, el sistema solicita completar la información. |
| 3 | El sistema valida el formato de los datos ingresados. | Si el correo no tiene formato válido o la contraseña no cumple las reglas, el sistema muestra un mensaje de error. |
| 4 | El sistema verifica que el correo no esté registrado previamente. | Si el correo ya existe, el sistema informa al usuario que debe usar otro correo. |
| 5 | El sistema guarda la información del nuevo usuario. | Si ocurre un error al guardar la información, el sistema muestra un mensaje indicando que el registro no pudo completarse. |
| 6 | El usuario inicia sesión con su correo y contraseña. | Si los datos de acceso son incorrectos, el sistema muestra un mensaje indicando que el correo o la contraseña no son válidos. |
| 7 | El usuario completa su perfil como jugador (posición, número dorsal, foto). | Si los datos no cumplen con las reglas definidas, el sistema solicita corregir la información. |
| 8 | El sistema guarda la información del perfil y muestra un mensaje confirmando que el proceso fue exitoso. | Si ocurre un error al guardar el perfil, el sistema muestra un mensaje indicando que no fue posible guardar los cambios. |

---

## 2. El sistema debe permitir que un capitán cree un equipo, agregue jugadores y administre la información del equipo.

# Documento de análisis de requerimientos

## Requerimiento funcional

| Campo | Descripción |
|------|-------------|
| Identificador | RF2 |
| Nombre del requerimiento | Gestión de equipos |
| Descripción | El sistema debe permitir que un capitán cree un equipo, agregue jugadores y administre la información del equipo. |
| Actor principal | Capitán |
| Precondiciones | El usuario debe estar registrado en el sistema, haber iniciado sesión y tener el rol de capitán. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|--------------|---------------------|-------------|
| nombre_equipo | Nombre del equipo | texto | No debe repetirse en el sistema | Sí |
| escudo | Imagen del escudo del equipo | imagen | Formato jpg o png | No |
| colores_uniforme | Colores del uniforme | texto | Descripción simple | No |
| jugadores | Lista de jugadores invitados | lista | Los jugadores deben estar registrados en el sistema | Sí |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| mensaje_confirmación | Mensaje que indica que el equipo fue creado correctamente | texto | Se muestra después de guardar el equipo | Sí |
| información_equipo | Información del equipo guardada en el sistema | objeto | Contiene nombre, escudo, colores y jugadores | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El capitán accede a la opción de crear equipo. | Si la página no carga correctamente, el sistema muestra un mensaje de error. |
| 2 | El capitán ingresa la información del equipo (nombre, escudo y colores). | Si el nombre del equipo está vacío, el sistema solicita completar la información. |
| 3 | El sistema valida que el nombre del equipo no exista previamente. | Si el nombre del equipo ya está registrado, el sistema solicita ingresar otro nombre. |
| 4 | El capitán busca y selecciona jugadores para agregarlos al equipo. | Si el jugador no está disponible o ya pertenece a otro equipo, el sistema informa que no puede agregarse. |
| 5 | El sistema valida que el equipo cumpla con las reglas de cantidad de jugadores. | Si el equipo no cumple con el mínimo o excede el máximo permitido, el sistema muestra un mensaje indicando el problema. |
| 6 | El sistema guarda la información del equipo. | Si ocurre un error al guardar la información, el sistema muestra un mensaje indicando que no se pudo completar el proceso. |
| 7 | El sistema muestra un mensaje confirmando que el equipo fue creado correctamente. | |

---

# 3. El sistema debe permitir buscar jugadores disponibles y enviarles invitaciones para unirse a un equipo.
# Documento de análisis de requerimientos

## Requerimiento funcional

| Campo | Descripción |
|------|-------------|
| Identificador | RF3 |
| Nombre del requerimiento | Búsqueda de jugadores e invitación a equipos |
| Descripción | El sistema debe permitir buscar jugadores disponibles y enviarles invitaciones para unirse a un equipo. |
| Actor principal | Capitán |
| Precondiciones | El capitán debe estar registrado en el sistema, haber iniciado sesión y tener un equipo creado. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|--------------|---------------------|-------------|
| posición | Posición del jugador | texto | Debe corresponder a una posición válida (portero, defensa, volante, delantero) | No |
| semestre | Semestre del jugador | número | Debe ser un número válido | No |
| edad | Edad del jugador | número | Debe ser mayor a 0 | No |
| género | Género del jugador | texto | Selección de lista | No |
| nombre | Nombre del jugador | texto | Búsqueda parcial o completa | No |
| identificación | Número de identificación | número | Debe ser un número válido | No |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| lista_jugadores | Lista de jugadores que cumplen con los criterios de búsqueda | lista | Muestra jugadores disponibles | Sí |
| mensaje_invitación | Confirmación del envío de la invitación | texto | Se muestra después de enviar la invitación | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El capitán accede a la opción de buscar jugadores. | Si la página no carga correctamente, el sistema muestra un mensaje de error. |
| 2 | El capitán ingresa uno o varios criterios de búsqueda. | Si los criterios ingresados no son válidos, el sistema solicita corregirlos. |
| 3 | El sistema realiza la búsqueda de jugadores disponibles. | Si no se encuentran jugadores con esos criterios, el sistema muestra un mensaje indicando que no hay resultados. |
| 4 | El sistema muestra la lista de jugadores encontrados. | |
| 5 | El capitán selecciona un jugador de la lista y envía una invitación. | Si el jugador ya pertenece a otro equipo, el sistema indica que no puede enviarse la invitación. |
| 6 | El sistema registra la invitación enviada al jugador. | Si ocurre un error al guardar la invitación, el sistema muestra un mensaje de error. |
| 7 | El sistema muestra un mensaje confirmando que la invitación fue enviada. | |

---

# 4. El sistema debe permitir al organizador crear y configurar el torneo con su información básica (fechas, número de equipos y costo).
# Documento de análisis de requerimientos

## Requerimiento funcional

| Campo | Descripción |
|------|-------------|
| Identificador | RF4 |
| Nombre del requerimiento | Creación y configuración del torneo |
| Descripción | El sistema debe permitir al organizador crear y configurar el torneo con información básica como fechas, número de equipos y costo de inscripción. |
| Actor principal | Organizador |
| Precondiciones | El organizador debe estar registrado en el sistema y haber iniciado sesión. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|--------------|---------------------|-------------|
| nombre_torneo | Nombre del torneo | texto | No debe repetirse | Sí |
| fecha_inicio | Fecha de inicio del torneo | fecha | Debe ser una fecha válida | Sí |
| fecha_fin | Fecha de finalización del torneo | fecha | Debe ser posterior a la fecha de inicio | Sí |
| número_equipos | Cantidad de equipos participantes | número | Debe ser mayor que 0 | Sí |
| costo_inscripción | Valor de inscripción por equipo | número | Debe ser mayor o igual a 0 | Sí |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| mensaje_confirmación | Mensaje que indica que el torneo fue creado correctamente | texto | Se muestra después de guardar el torneo | Sí |
| información_torneo | Datos del torneo guardados en el sistema | objeto | Contiene nombre, fechas, número de equipos y costo | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El organizador accede a la opción de crear torneo. | Si la página no carga correctamente, el sistema muestra un mensaje de error. |
| 2 | El organizador ingresa la información del torneo (nombre, fechas, número de equipos y costo). | Si algún campo obligatorio está vacío, el sistema solicita completar la información. |
| 3 | El sistema valida la información ingresada. | Si las fechas no son válidas o la fecha final es menor que la inicial, el sistema muestra un mensaje de error. |
| 4 | El sistema verifica que el nombre del torneo no esté registrado previamente. | Si el nombre ya existe, el sistema solicita ingresar un nombre diferente. |
| 5 | El sistema guarda la información del torneo. | Si ocurre un error al guardar la información, el sistema muestra un mensaje indicando que no se pudo completar el proceso. |
| 6 | El sistema muestra un mensaje confirmando que el torneo fue creado correctamente. | |

---

# 5. El sistema debe permitir subir comprobantes de pago para la inscripción del equipo y permitir al organizador aprobar o rechazar la inscripción.
# Documento de análisis de requerimientos

## Requerimiento funcional

| Campo | Descripción |
|------|-------------|
| Identificador | RF5 |
| Nombre del requerimiento | Inscripción de equipos y validación de pago |
| Descripción | El sistema debe permitir subir comprobantes de pago para la inscripción del equipo y permitir al organizador aprobar o rechazar la inscripción. |
| Actor principal | Capitán / Organizador |
| Precondiciones | El capitán debe tener un equipo creado y el torneo debe estar abierto para inscripciones. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|--------------|---------------------|-------------|
| equipo | Equipo que realiza la inscripción | texto | Debe existir en el sistema | Sí |
| comprobante_pago | Archivo del comprobante de pago | archivo | Formato jpg, png o pdf | Sí |
| fecha_pago | Fecha en que se realizó el pago | fecha | Debe ser una fecha válida | Sí |
| observación | Comentario del organizador sobre la revisión | texto | Uso opcional | No |
| estado_inscripción | Estado de la inscripción | texto | Puede ser pendiente, aprobado o rechazado | Sí |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| mensaje_confirmación | Mensaje que confirma que el comprobante fue cargado correctamente | texto | Se muestra después de subir el archivo | Sí |
| estado_inscripción | Estado actualizado de la inscripción del equipo | texto | Indica si está pendiente, aprobado o rechazado | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El capitán accede a la opción de inscripción al torneo. | Si la página no carga correctamente, el sistema muestra un mensaje de error. |
| 2 | El capitán selecciona su equipo y carga el comprobante de pago. | Si no se carga ningún archivo, el sistema solicita adjuntar el comprobante. |
| 3 | El sistema valida el formato del archivo cargado. | Si el archivo no tiene un formato permitido, el sistema muestra un mensaje de error. |
| 4 | El sistema guarda el comprobante y registra la inscripción como pendiente de revisión. | Si ocurre un error al guardar la información, el sistema muestra un mensaje indicando que no se pudo completar el proceso. |
| 5 | El organizador revisa el comprobante de pago. | Si el comprobante no es claro o es inválido, el organizador puede rechazar la inscripción. |
| 6 | El organizador aprueba o rechaza la inscripción del equipo. | |
| 7 | El sistema actualiza el estado de la inscripción y muestra el resultado al capitán. | |

---

# 6. El sistema debe permitir registrar la información de los partidos como horario, cancha, alineaciones, resultados y sanciones.
# Documento de análisis de requerimientos

## Requerimiento funcional

| Campo | Descripción |
|------|-------------|
| Identificador | RF6 |
| Nombre del requerimiento | Registro de información de partidos |
| Descripción | El sistema debe permitir registrar la información de los partidos como horario, cancha, alineaciones, resultados y sanciones. |
| Actor principal | Organizador / Árbitro |
| Precondiciones | El torneo debe estar creado y los equipos participantes deben estar inscritos. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|--------------|---------------------|-------------|
| equipo_local | Equipo que juega como local | texto | Debe ser un equipo inscrito en el torneo | Sí |
| equipo_visitante | Equipo que juega como visitante | texto | Debe ser un equipo inscrito en el torneo | Sí |
| fecha_partido | Fecha del partido | fecha | Debe ser una fecha válida | Sí |
| hora_partido | Hora del partido | hora | Debe ser una hora válida | Sí |
| cancha | Cancha donde se juega el partido | texto | Debe existir en el sistema | Sí |
| alineación | Lista de jugadores que participan en el partido | lista | Deben pertenecer al equipo correspondiente | Sí |
| resultado | Marcador final del partido | texto | Ejemplo: 2-1 | No |
| sanciones | Tarjetas o sanciones registradas durante el partido | texto | Opcional | No |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| información_partido | Información registrada del partido | objeto | Contiene horario, equipos, cancha, alineaciones y resultado | Sí |
| mensaje_confirmación | Mensaje que indica que la información fue registrada correctamente | texto | Se muestra después de guardar los datos | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El organizador o árbitro accede a la opción de registrar información del partido. | Si la página no carga correctamente, el sistema muestra un mensaje de error. |
| 2 | El usuario ingresa los datos del partido (equipos, fecha, hora y cancha). | Si algún campo obligatorio está vacío, el sistema solicita completar la información. |
| 3 | El sistema valida que los equipos estén inscritos en el torneo. | Si alguno de los equipos no está inscrito, el sistema muestra un mensaje de error. |
| 4 | El usuario registra las alineaciones de los equipos. | Si un jugador no pertenece al equipo, el sistema solicita corregir la alineación. |
| 5 | Después del partido, el usuario registra el resultado y las sanciones. | Si el formato del resultado es incorrecto, el sistema solicita corregirlo. |
| 6 | El sistema guarda la información del partido. | Si ocurre un error al guardar la información, el sistema muestra un mensaje indicando que no se pudo completar el proceso. |
| 7 | El sistema muestra un mensaje confirmando que la información fue registrada correctamente. | |

---

# 7. El sistema debe generar automáticamente la tabla de posiciones y las llaves eliminatorias del torneo.

# RF7 - Generar tabla de posiciones y llaves eliminatorias

## Información del requerimiento

| Campo | Descripción |
|------|-------------|
| Identificador | RF7 |
| Nombre del requerimiento | Generar tabla de posiciones y llaves eliminatorias |
| Descripción | El sistema calcula automáticamente la tabla de posiciones del torneo y genera las llaves eliminatorias a partir de los resultados registrados de los partidos. |
| Actor principal | Organizador |
| Precondiciones | El torneo debe existir en el sistema y los partidos deben tener resultados registrados. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|---------------|---------------------|-------------|
| Torneo | Torneo para el cual se generará la tabla de posiciones | Selección | Debe existir en el sistema | Sí |
| Resultados de partidos | Resultados registrados de los partidos del torneo | Datos del sistema | Deben existir resultados para poder calcular la tabla | Sí |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| Tabla de posiciones | Clasificación de los equipos según resultados | Información | Se calcula con puntos, goles a favor y diferencia de goles | Sí |
| Llaves eliminatorias | Cruces entre equipos clasificados | Información | Se generan según la posición obtenida en la tabla | Sí |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El organizador accede al módulo del torneo. | |
| 2 | El organizador selecciona el torneo. | |
| 3 | El organizador selecciona la opción generar tabla de posiciones. | |
| 4 | El sistema obtiene los resultados de los partidos registrados. | Si no existen resultados, el sistema informa que no es posible generar la tabla. |
| 5 | El sistema calcula los puntos y estadísticas de cada equipo. | |
| 6 | El sistema ordena los equipos según los criterios del torneo. | |
| 7 | El sistema genera la tabla de posiciones. | |
| 8 | El sistema genera las llaves eliminatorias según la clasificación. | Si no hay suficientes equipos clasificados, solo se genera la tabla. |
| 9 | El sistema muestra la información generada al organizador. | |



## Flujo alterno

| Descripción | Excepciones |
|-------------|-------------|
| El sistema detecta que existen partidos sin resultados registrados. | El sistema solicita completar los resultados antes de generar la tabla. |
| Ocurre un error durante el cálculo de la tabla o generación de llaves. | El sistema muestra un mensaje de error y solicita intentar nuevamente. |

---

# 8. El sistema debe permitir consultar información del torneo como calendario de partidos, resultados y estadísticas.

# RF8 - Consultar información del torneo

## Información del requerimiento

| Campo | Descripción |
|------|-------------|
| Identificador | RF8 |
| Nombre del requerimiento | Consultar información del torneo |
| Descripción | El sistema permite consultar información del torneo como el calendario de partidos, resultados y estadísticas. |
| Actor principal | Usuario |
| Precondiciones | El torneo debe existir en el sistema y debe tener información registrada. |



## Datos de entrada

| Nombre | Descripción | Tipo de campo | Reglas / Aplicación | Obligatorio |
|------|-------------|---------------|---------------------|-------------|
| Torneo | Torneo del cual se desea consultar la información | Selección | Debe existir en el sistema | Sí |
| Tipo de consulta | Tipo de información a consultar (calendario, resultados o estadísticas) | Selección | Debe ser una de las opciones disponibles | Sí |



## Datos de salida

| Nombre | Descripción | Tipo | Reglas / Aplicación | Obligatorio |
|------|-------------|------|---------------------|-------------|
| Calendario de partidos | Lista de partidos programados del torneo | Información | Incluye fecha, hora y equipos | No |
| Resultados | Resultados de los partidos disputados | Información | Incluye marcador de cada partido | No |
| Estadísticas | Información estadística del torneo | Información | Puede incluir goles, puntos o posiciones | No |



## Flujo básico

| Paso | Descripción | Excepciones |
|----|-------------|-------------|
| 1 | El usuario accede al módulo de torneos. | |
| 2 | El usuario selecciona el torneo que desea consultar. | |
| 3 | El usuario selecciona el tipo de información que desea ver. | |
| 4 | El sistema busca la información del torneo en la base de datos. | Si el torneo no existe, el sistema muestra un mensaje indicando que no se encontró información. |
| 5 | El sistema muestra el calendario, resultados o estadísticas del torneo. | |
| 6 | El usuario consulta la información mostrada en el sistema. | |



## Flujo alterno

| Descripción | Excepciones |
|-------------|-------------|
| El torneo aún no tiene partidos programados. | El sistema informa que no hay calendario disponible. |
| El torneo no tiene resultados registrados. | El sistema informa que no hay resultados disponibles. |
| El torneo no tiene estadísticas generadas. | El sistema informa que no hay estadísticas disponibles. |

---

# Anexos:

[Diagrama-Casos-De-Uso2.asta](../uml/Diagrama-Casos-De-Uso2.asta)

![Diagrama-Casos-De-Uso.png](../images/Diagrama-Casos-De-Uso.png)

# Prototipos:

https://tag-skit-64046987.figma.site/
![img_1.png](docs/requirements/img_1.png)
## Reglas de negocio

| No. | Descripción |
|----|-------------|
| RN1 | Un usuario debe registrarse e iniciar sesión para poder participar en el sistema. |
| RN2 | Solo un capitán puede crear y administrar un equipo. |
| RN3 | Un jugador solo puede pertenecer a un equipo dentro del torneo. |
| RN4 | Un equipo solo puede participar en el torneo si su inscripción y pago han sido aprobados por el organizador. |
| RN5 | Los resultados de los partidos deben estar registrados para poder generar la tabla de posiciones. |
| RN6 | La tabla de posiciones se calcula según puntos obtenidos, goles a favor y diferencia de goles. |
| RN7 | Las llaves eliminatorias se generan a partir de la clasificación de la tabla de posiciones. |
| RN8 | Solo el organizador puede crear y configurar torneos. |



## Abreviaturas

| Abreviatura | Significado |
|-------------|-------------|
| RF | Requerimiento Funcional |
| RNF | Requerimiento No Funcional |
| UI | Interfaz de Usuario |
| DB | Base de Datos |
| API | Interfaz de Programación de Aplicaciones |



## Historial de revisión

| Elaborado por | Aprobado por | Fecha | Descripción y justificación de cambios |
|---------------|--------------|-------|----------------------------------------|
| Equipo de desarrollo | Profesor | 22/10/2024 | Creación inicial del documento de análisis de requerimientos |
| Equipo de desarrollo | Profesor | 23/10/2024 | Ajuste de requerimientos funcionales y reglas de negocio |

Jira:
https://mail-team-q7lj9.atlassian.net/jira/software/projects/ZEUS/list?jql=project%20%3D%20ZEUS%20ORDER%20BY%20created%20DESC

##  Proyecto TechCup Fútbol - Backend (Sprint #1)
Este repositorio contiene la lógica de negocio y la infraestructura del lado del servidor para la plataforma TechCup. En este primer sprint, se ha establecido la arquitectura base, los modelos de dominio y la API funcional para la gestión de usuarios y equipos.

#  Arquitectura y Patrones de Diseño
El sistema ha sido desarrollado bajo el patrón de arquitectura MVC (Modelo-Vista-Controlador) utilizando el framework Spring Boot.

Patrones de Diseño Aplicados:
Inyección de Dependencias (DI): Implementada para desacoplar los controladores de la lógica de negocio en los servicios.
Singleton: Spring Boot gestiona los servicios y controladores como instancias únicas para optimizar el uso de memoria.
POJOs / Entities: Clases planas para la representación fiel de los requerimientos del negocio.

# Patrones de Diseño Utilizados

##  Factory Method

### ¿Por qué lo elegimos?

Elegimos *Factory Method* porque en *TECHCUP FÚTBOL* manejamos diferentes tipos de usuarios que comparten características comunes pero tienen *reglas de creación distintas*:

- *Jugador*
    - Requiere posición de juego
    - Número dorsal
    - Disponibilidad

- *Capitán*
    - Además de ser jugador
    - Debe tener un equipo asociado
    - Tiene permisos especiales

- *Administrador*
    - Tiene privilegios para configurar el torneo

También aplica para los *partidos según la fase del torneo*:

- Fase de *grupos*
- *Cuartos de final*
- *Semifinal*
- *Final*

Cada fase puede tener *reglas diferentes* como:

- Empate permitido
- Penales
- Tiempo extra

Sin este patrón, el código tendría muchos *condicionales if-else, que habría que modificar cada vez que agreguemos un nuevo tipo, **violando el principio Open/Closed*.

---

### ¿Cómo ayuda a resolver el problema del sistema?

- *Desacopla el código cliente* de las clases concretas, permitiendo trabajar con *interfaces*.
- *Centraliza la lógica de creación y validación* en factories específicas.
- *Facilita la extensibilidad: para agregar un nuevo rol (por ejemplo **Árbitro), solo se crea su *factory sin modificar el código existente.
- *Encapsula las reglas de negocio* particulares de cada tipo de objeto.
- *Mejora la mantenibilidad*, ya que cada tipo de creación se encuentra en su propia clase.

---

##  Strategy

### ¿Por qué lo elegimos?

Seleccionamos *Strategy* porque en *TECHCUP* necesitamos *algoritmos intercambiables* para varias funcionalidades clave:

- *Cálculo de tabla de posiciones*
    - Sistema tradicional (*3-1-0*)
    - Bonificaciones por goleada
    - Criterios especiales de desempate

- *Generación de llaves eliminatorias*
    - Eliminación directa
    - Doble eliminación
    - Otros formatos de torneo

- *Sistema de puntuación*
    - El organizador puede configurar las reglas en cada edición del torneo

Sin *Strategy, tendríamos un **método grande con muchos condicionales*, difícil de mantener y extender.  
Cada vez que el organizador quisiera cambiar las reglas, habría que *modificar el código existente*.

---

### ¿Cómo ayuda a resolver el problema del sistema?

- Permite *cambiar algoritmos en tiempo de ejecución* según la configuración del torneo.
- *Aísla cada algoritmo en su propia clase*, facilitando pruebas y mantenimiento.
- Cumple el *principio Open/Closed*, ya que nuevas estrategias se agregan sin modificar el código existente.
- *Delega la responsabilidad del cálculo* a clases especializadas, manteniendo el servicio principal limpio.
- Ofrece *flexibilidad al organizador* para personalizar las reglas de cada torneo sin cambios estructurales

#  Requerimientos Funcionales Implementados (Demo Funcional)
RF1: Registro y Perfil de Jugador
Se implementó la entidad User que permite capturar la información técnica de los jugadores.

Atributos: Nombre, correo, contraseña (hash simulado), posición, número de dorsal y foto.
Validación de Negocio: El UserService garantiza que no existan registros con correos duplicados antes de añadirlos a la memoria.
RF2: Gestión de Equipos
Se implementó la entidad Team para la organización del torneo.

Atributos: Nombre del equipo, escudo, colores representativos y lista de jugadores asociados.
Relación: Un equipo puede contener múltiples objetos de tipo User (Jugadores).
#  Endpoints de la API (REST)
Para la Demo Funcional, el backend expone los siguientes puntos de acceso en http://localhost:8080:

Método	Ruta	Descripción
POST	/api/users/register	Registra un nuevo jugador (JSON Body).
GET	/api/users/all	Lista todos los jugadores registrados en la sesión actual.
POST	/api/teams/create	Crea un nuevo equipo de fútbol.
GET	/api/teams/all	Lista los equipos creados.

### Diagrama De Clases
![alt text](<Diagrama de clases TechCup2-1.png>)

### Diagrama ded Componentes General
![Diagrama de componentes general tf.drawio.png](docs%2Fuml%2FDiagrama%20de%20componentes%20general%20tf.drawio.png)

### Diagrama dde Componenetes Especifico
![Diagrama de Componentes Especifico.drawio.png](docs%2Fuml%2FDiagrama%20de%20Componentes%20Especifico.drawio.png)

#  Pruebas y Validación
Para facilitar la revisión y el trabajo del equipo de Frontend, se incluyó un archivo de pruebas rápidas en la raíz:

Archivo: pruebas.http
Uso: Ejecutar directamente desde IntelliJ IDEA para validar el flujo End-to-End (Controlador -> Servicio -> Memoria).
