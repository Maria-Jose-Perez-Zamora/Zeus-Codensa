# Escenarios de Prueba (Criterios de Aceptación Jira)
*Proyecto: TECHCUP FÚTBOL - Sprint 1*

A continuación se definen los escenarios de prueba exigidos para cada funcionalidad principal basados en el `requirements.md`. Estos deben ser copiados en los tickets de JIRA como "Criterios de Aceptación" en las historias de usuario correspondientes.

---

## RF-001: Gestión de Torneos

### Happy Path (Casos de Éxito)
1. **Creación de torneo exitosa:** Al ingresar un nombre único, fechas válidas (fin posterior a inicio), número de equipos (4 a 32) y costo, el sistema crea el torneo y lo guarda en estado "Borrador".
2. **Inicio de torneo:** Al seleccionar un torneo en "Borrador" y presionar "Iniciar", el estado cambia exitosamente a "Activo" y se abre el periodo de inscripciones.

### Error (Casos de Error)
1. **Fechas inválidas:** Si la fecha final es anterior o igual a la fecha inicial, el sistema rechaza la creación y muestra un error "Rango de fechas inválido".
2. **Equipos fuera del límite:** Si la cantidad de equipos es menor a 4 o mayor a 32, el sistema muestra un error de validación de límites.

### Conditional (Casos Condicionales)
1. **Permisos insuficientes:** Si un usuario sin rol de "Organizador" intenta acceder al formulario de creación, el sistema lo bloquea y lo redirige.
2. **Restricción de estado:** Si el torneo ya está "Activo" o "En progreso", el botón de "Iniciar torneo" debe estar deshabilitado u oculto.

---

## RF-002: Registro de Usuarios y Jugadores

### Happy Path (Casos de Éxito)
1. **Registro completo:** El usuario completa su correo institucional, nombre, ID válida, posiciones, dorsal (1-99), género, edad (>16) y foto. El sistema crea el perfil y guarda la disponibilidad para capitanes.
2. **Edición de perfil:** Un usuario registrado modifica su foto y posición preferida, los cambios persisten en la base de datos correctamente.

### Error (Casos de Error)
1. **Correo duplicado:** Si el correo ingresado ya existe en el sistema, la plataforma muestra "Este correo ya está registrado".
2. **Edad menor a la permitida:** Si se ingresa una edad menor a 16 años, el sistema impide el registro.

### Conditional (Casos Condicionales)
1. **Dominios de correo:** Si el rol es "Administrativo" o "Estudiante", el sistema valida que el dominio sea forzosamente `@mail.escuelaing.edu.co` (o equivalente). Si es "Familiar", se permite Gmail.

---

## RF-003: Gestión de Equipos (Creación e Invitaciones)

### Happy Path (Casos de Éxito)
1. **Creación de equipo:** Un capitán ingresa nombre, colores y sube un logo válido (<5MB). El sistema asigna el equipo al torneo activo.
2. **Invitación aceptada:** Capitán busca jugador disponible, envía invitación, el jugador acepta, y el conteo de miembros del equipo sube en +1.

### Error (Casos de Error)
1. **Nombre de equipo duplicado:** Al intentar crear un equipo con un nombre ya existente en el mismo torneo, salta error de duplicidad.
2. **Límites de plantilla:** Si el equipo ya tiene 12 jugadores (límite máximo) e intenta enviar otra invitación, el sistema lo bloquea.

### Conditional (Casos Condicionales)
1. **Jugador en múltiples equipos:** Si el jugador acepta la invitación al "Equipo A", el sistema marca su estado como "No disponible" y automáticamente declina las invitaciones pendientes del "Equipo B" y "Equipo C" válidas para el mismo torneo.
2. **Regla de programas académicos:** Cuando se envía la inscripción final, si menos del 51% de los jugadores son de Ingeniería (Sistemas, IA, Ciber, Estadística), el sistema emite una alerta bloqueante de validación.

---

## RF-004: Gestión de Inscripciones y Pagos

### Happy Path (Casos de Éxito)
1. **Carga de comprobante:** El capitán sube el comprobante de imagen (Nequi) y digita la referencia. El estado del equipo pasa a "En revisión".
2. **Aprobación de pago:** El organizador verifica el voucher en su panel, presiona "Aprobar", y el equipo queda oficialmente habilitado para jugar.

### Error (Casos de Error)
1. **Formato incorrecto:** El capitán intenta subir un archivo `.docx` o un comprobante de tamaño mayor a 5MB, el sistema muestra error y no lo guarda.
2. **Pago rechazado:** Organizador marca el pago como "Rechazado", el capitán recibe un correo con el motivo (ej. "Monto incompleto") y se reinicia el ciclo de envío.

### Conditional (Casos Condicionales)
1. **Límite de reintentos:** Si un comprobante es rechazado 3 veces, el sistema bloquea automáticamente las inscripciones manuales para ese equipo y requiere intervención administrativa.

---

## RF-006 & RF-007: Partidos y Tabla de Posiciones

### Happy Path (Casos de Éxito)
1. **Registro simple de partido:** Organizador ingresa partido (Local 2 - Visitante 1). Sistema guarda resultado, recalcula +3 pts al local, +0 al visitante, y ajusta diferencia de gol automáticamente.
2. **Cálculo de posiciones correcto:** Al actualizar varios partidos, la tabla se ordena correctamente por Puntos, luego por Diferencia de Gol y finalmente por Goles a Favor.

### Error (Casos de Error)
1. **Goles negativos:** Al intentar guardar un marcador con valores menores a 0 (ej. -1), el sistema rechaza la solicitud de guardado.

### Conditional (Casos Condicionales)
1. **Llaves automáticas en cierre:** Solo cuando el último partido de la "Fase de Grupos" es marcado como "Finalizado", el sistema dispara la generación aleatoria/ordenada de los enfrentamientos de Cuartos de Final.
2. **Sin goleadores en empates 0-0:** Si el resultado es `0-0`, el sistema desactiva condicionalmente los campos requeridos para seleccionar anotadores.

