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

5. El sistema debe desarrollarse con Spring Boot en el backend, React con TypeScript en el frontend y PostgreSQL como base de datos.

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
# Prototipos:

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

