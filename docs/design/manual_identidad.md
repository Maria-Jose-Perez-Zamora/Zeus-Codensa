# Manual de Identidad Visual y UI - TECHCUP FÚTBOL

Este documento establece las directrices visuales, conceptuales y de diseño de interfaz (UI) para el sistema de gestión de torneos "TECHCUP FÚTBOL". Su objetivo principal es asegurar la consistencia visual, la escalabilidad del sistema y proporcionar una referencia clara para el desarrollo frontend.

---

## 1. Identidad Visual y Concepto de Marca

La interfaz del sistema TECHCUP FÚTBOL presenta un diseño de tipo *dashboard* deportivo con un estilo tecnológico y moderno. Se construye bajo un tema oscuro (*Dark Mode*) que prioriza la legibilidad de los datos, una jerarquía visual clara y una experiencia de usuario (UX) óptima.

### 1.1. Valores Transmitidos
La combinación de fondos oscuros, acentos en verde vibrante y una iconografía deportiva proyecta una identidad que fusiona el análisis de datos con la pasión por el deporte. Esta identidad busca transmitir:
- **Profesionalismo:** Un entorno serio y confiable para la gestión de torneos.
- **Claridad:** Visualización estructurada y limpia de datos estadísticos complejos.
- **Dinamismo Competitivo:** A través del uso de contrastes vibrantes que evocan la energía del campo de juego.
- **Enfoque Tecnológico:** Una plataforma moderna, eficiente y a la vanguardia.

---

## 2. Tipografía

El diseño adopta principios de minimalismo visual, donde la tipografía juega un rol fundamental para garantizar legibilidad en entornos digitales de alta densidad de información.

- **Familia Tipográfica:** Fuente *sans-serif* moderna, limpia y geométrica (Ej: `Inter`, `Roboto` o similar).
- **Usos y Grosores:**
  - **Títulos y Encabezados (`h1`, `h2`, `h3`):** Grosores *Bold* o *Semi-Bold* para destacar secciones estructurales.
  - **Cuerpos de Texto y Tablas (`p`, `span`):** Grosores *Regular* y *Light* para datos tabulares y lectura continua.

---

## 3. Paleta de Colores

La paleta cromática está dominada por azules muy oscuros y neutros que reducen la fatiga visual, complementada con un tono verde vibrante como acento principal, reforzando la asociación al ámbito deportivo (el césped) y destacando la interactividad.

### 3.1. Colores Primarios
Representan la identidad central de la marca y dirigen la atención del usuario.
- **Verde Principal (Acento y Marca):** `#00B37E` *(Verde Esmeralda/Mentolado)*. 
  - **Uso:** Logotipo (palabra "FÚTBOL"), botones principales (Call to Action), resaltes numéricos (ej. Puntos en la tabla), elementos de navegación activos e indicadores de estado positivo.
- **Verde Secundario (Fondo Activo):** `#0A2B27` *(Verde Petróleo Oscuro)*.
  - **Uso:** Fondos sutiles para elementos interactivos en estado *Hover* o *Selected* (Ej. ítem activo en el menú lateral).

### 3.2. Colores Neutros y de Fondo
Estructuran el *Dark Mode*, creando profundidad mediante la estratificación de tonos.
- **Fondo Base (App Background):** `#0B111A` *(Azul Noche muy oscuro, casi negro)*.
- **Superficies (Tarjetas y Contenedores):** `#121926` y `#1C2434` *(Tonos azul-grisáceo oscuros)*.
  - **Uso:** Paneles informativos, tablas y encabezados que requieren elevación visual respecto al fondo base.
- **Textos Principales:** `#FFFFFF` *(Blanco)* o `#F8FAFC` *(Gris muy claro)* para máxima legibilidad.
- **Textos Secundarios:** `#94A3B8` *(Gris Azulado claro)* para metadatos, etiquetas de columnas, fechas horarias y menús inactivos.

### 3.3. Colores Semánticos (Feedback y Estados)
Diseñados para brindar retroalimentación inmediata sobre acciones o estados del sistema.
- **Éxito (Success):** `#00B37E` (Verde Principal) - "Equipo Creado", "Pago Aprobado", indicador "EN CURSO".
- **Peligro / Error (Danger):** `#EF4444` o `#D32F2F` (Rojo) - "Pago Rechazado", errores de formulario, "Sanciones/Tarjetas Rojas".
- **Advertencia (Warning):** `#FBBF24` o `#F59E0B` (Amarillo/Naranja) - "Pendiente de Aprobación", "Tarjetas Amarillas".
- **Informativo (Info):** `#3B82F6` (Azul) - Notificaciones neutrales, guías e hipervínculos.

---

## 4. Estructura Visual de la Interfaz

La interfaz (UI) se organiza metodológicamente en tres áreas principales para maximizar el flujo operativo del usuario:

### 4.1. Barra Lateral de Navegación (Sidebar)
Ubicada en el lado izquierdo, es el componente ancla de la navegación.
- **Cabecera Logotipo:** Contiene un ícono de trofeo y la tipografía "TECHCUP FÚTBOL" (con "FÚTBOL" resaltado en Verde Principal).
- **Menú de Navegación:** Opciones con iconografía lineal y minimalista.
  - *Ítems:* Panel Principal, Perfil de Jugador, Alineación, Portal de Pagos, Torneo.
  - *Comportamiento:* El ítem activo se destaca mediante un bloque contenedor con el Verde Secundario (`#0A2B27`), permitiendo rápida ubicación espacial.
- **Rodapié de Usuario:** Muestra la información de sesión actual (Avatar circular, nombre y etiqueta de rol táctico, ej. "Capitán").

### 4.2. Área Principal de Contenido
Sección central superior dedicada al contexto inmediato.
- **Encabezado de Página:** Títulos descriptivos claros (Ej. "Resumen del Torneo") acompañados de un subtítulo orientativo.
- **Zona de Acciones e Indicadores:** A los extremos se sitúan indicadores temporales (Ej. *Pill* o etiqueta ovalada "EN CURSO" con un punto verde intermitente) y botones de acción principal (Ej. "Iniciar Sesión") coloreados íntegramente en Verde Principal.

### 4.3. Paneles Informativos (Cards)
La densidad de datos se controla mediante contenedores modulares con bordes redondeados y fondos ligeramente más claros que la aplicación.
- **Tablas de Datos (Ej. Tabla de Posiciones):** Estructura ordenada mediante grillas, separaciones sutiles (líneas divisorias en `#1C2434`) y alineación clara. Los datos matemáticos clave (como la columna Pts) sobresalen en Verde Principal.
- **Tarjetas Compactas (Ej. Próximos Partidos):** Bloques de rápida consulta que subdividen la información mediante fechas, canchas y cruces de equipos, optimizando el espacio visual sin abrumar.

---

## 5. Componentes Clave (Design System Basis)

### 5.1. Botones (Buttons)
- **Primarios:** Fondo sólido `#00B37E` con texto en `#FFFFFF`, esquinas redondeadas (*border-radius* suavizado).
- **Secundarios / Outline:** Sin fondo, con borde visible de color gris claro o Verde Principal, para acciones cancelables o de menor impacto.
- **Etiquetas de Estado (Pills):** Ovalados, con iconos pequeños (ej. un círculo para indicar conectividad/curso) y texto secundario.

### 5.2. Formularios (Forms)
- Inputs con un contraste sutil (fondo más oscuro que las tarjetas, ej. `#0B111A`) y un marco resaltado en Verde Principal o azul durante el estado `:focus` para guiar la accesibilidad del usuario.

### 5.3. Iconografía
- Estilo lineal, peso (*stroke-width*) regular, del mismo grosor que la tipografía estándar para no competir visualmente.
- Deben transmitir la estética tecnológica sin perder el simbolismo clásico (trofeos, calendarios, campos de juego).

---

## 6. Referencias Oficiales

Todo el desarrollo debe tener como fuente de verdad los siguientes enlaces centralizados:
- **Prototipo Figma UI/UX:** [Soccer-Tournament-Management-System](https://www.figma.com/make/qoYhlVEwEVM0H4NcYEmsNe/Soccer-Tournament-Management-System?p=f&fullscreen=1&preview-route=%2Flineup)
- **Capturas locales:** Revisar el directorio `docs/requirements/img.png`.

*Nota: Los desarrolladores de Frontend deben mapear los valores descritos en este documento a variables globales en CSS (*Design Tokens*, e.g., `:root { --color-primary: #00B37E; }`) para asegurar una fácil escalabilidad y mantenimiento del proyecto.*
