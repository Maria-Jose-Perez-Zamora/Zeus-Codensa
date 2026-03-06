# Manual de Identidad Visual y UI - TECHCUP FÚTBOL

Este documento establece las directrices visuales, conceptuales y de diseño de interfaz (UI) para el sistema de gestión de torneos "TECHCUP FÚTBOL". Su objetivo principal es asegurar la consistencia visual, la escalabilidad del sistema y proporcionar una referencia clara para el desarrollo frontend.

## 1. Identidad Visual y Concepto de Marca

La interfaz del sistema TECHCUP FÚTBOL presenta un diseño tipo *dashboard* enfocado en la gestión deportiva, adoptando una estética tecnológica y moderna. Se estructura sobre un tema oscuro (*Dark Mode*) diseñado para optimizar la legibilidad de datos complejos, establecer una jerarquía visual técnica y garantizar un rendimiento ergonómico prolongado.

### 1.1. Valores y Posicionamiento
La propuesta cromática y estructural, basada en fondos de baja luminosidad con acentos en verde de alta saturación, busca proyectar:
- **Profesionalismo:** Un entorno corporativo y seguro para la administración deportiva.
- **Eficiencia Cognitiva:** Presentación clara y estructurada de indicadores y estadísticas.
- **Dinamismo Competitivo:** Uso estratégico del contraste para evocar la energía inherente al deporte.
- **Vanguardia Tecnológica:** Una plataforma digital robusta y contemporánea.

## 2. Tipografía

El sistema adopta principios de diseño modular y funcional, donde la selección tipográfica es determinante para la legibilidad en interfaces de alta densidad informativa.

- **Familia Tipográfica Principal:** Fuente sans-serif contemporánea, de proporción geométrica y alta legibilidad en pantalla (Ej. `Inter`, `Roboto` o similar).
- **Sistema de Pesos:**
  - **Títulos y Estructura Organizativa (`h1`, `h2`, `h3`):** Grosores *Bold* (700) o *Semi-Bold* (600) para definir jerarquía y separar secciones.
  - **Cuerpos de Texto, Tablas y Componentes (`p`, `span`, `td`):** Grosores *Regular* (400) y *Light* (300) para facilitar la lectura de datos continuos y tabulares.

## 3. Sistema de Color

La arquitectura de color se fundamenta en tonos oscuros desaturados para minimizar la fatiga visual, empleando un verde vibrante como color de acción primario. Este verde actúa como *call-to-action* (CTA) y refuerza la conexión semántica con el campo de juego.

### 3.1. Tonos Primarios de Marca y Acción
Definen la identidad interactiva y focalizan la atención en elementos críticos.
- **Verde Principal (Acento):** `#00B37E`
  - **Aplicación:** Logotipo (textos de resalte), botones de acción afirmativa (CTA), indicadores de valores numéricos positivos, elementos de navegación seleccionados y estados de retroalimentación de éxito.
- **Verde Secundario (Fondos Activos):** `#0A2B27`
  - **Aplicación:** Uso restringido a fondos semi-transparentes o tenues para denotar estados interactivos (*hover*, *active* o *selected*) sin saturar la composición visual.

### 3.2. Tonos Neutros y Estructura de Superficies
Constituyen la base del tema oscuro, generando profundidad mediante la superposición de capas.
- **Fondo Base (App Background):** `#0B111A`
- **Superficies y Contenedores (Cards/Modals):** `#121926` y `#1C2434`
  - **Aplicación:** Elevación visual de paneles informativos, tablas de datos y módulos superpuestos.
- **Textos Primarios:** `#FFFFFF` o `#F8FAFC`
  - **Aplicación:** Legibilidad máxima sobre fondos oscuros.
- **Textos Secundarios y Metadatos:** `#94A3B8`
  - **Aplicación:** Etiquetas descriptivas, encabezados de columnas, información auxiliar, menús inactivos y *placeholders*.

### 3.3. Paleta Semántica
Colores funcionales para la comunicación de estados del sistema.
- **Éxito (Success):** `#00B37E` (Acciones completadas, operaciones en línea).
- **Peligro / Error / Alertas Críticas (Danger):** `#EF4444` o `#D32F2F` (Errores de validación, acciones destructivas, sanciones disciplinarias).
- **Advertencia (Warning):** `#FBBF24` o `#F59E0B` (Avisos preventivos, estados pendientes, amonestaciones).
- **Informativo (Info):** `#3B82F6` (Notificaciones del sistema, hipervínculos).

## 4. Arquitectura de la Interfaz

La disposición espacial de los elementos responde a principios ergonómicos, optimizando la navegación y el consumo de información.

### 4.1. Panel de Navegación Lateral (Sidebar)
Elemento de anclaje para el enrutamiento principal.
- **Cabecera Institucional:** Isotipo técnico (e.g., trofeo abstracto) y logotipo denominativo "TECHCUP FÚTBOL".
- **Estructura de Menú:** Ítems representados mediante iconografía lineal de peso uniforme (Panel de Control, Perfil, Alineación, Pagos, Gestión de Torneos).
- **Indicador de Estado Activo:** Destacado volumétrico utilizando el Verde Secundario sobre el ítem seleccionado.
- **Módulo de Identidad de Usuario:** Situado en la sección inferior, presenta un avatar, credenciales y rol en el sistema.

### 4.2. Superficie Principal de Trabajo
Área de despliegue del contexto operativo.
- **Cabecera de Contexto (Header):** Títulos descriptivos unívocos y subtítulos funcionales para contextualización inmediata.
- **Zona de Utilidad Global:** Ubicación de indicadores de estado en tiempo real, notificaciones globales y botones de acción principal.

### 4.3. Componentes Modulares Informativos (Cards)
Estrategia de contención para la segmentación de datos estadísticos y operativos.
- **Tablas de Rendimiento:** Sistemas de grillas estructuradas con bordes divisibles tenues (`#1C2434`). Resalte selectivo (Verde Principal) para métricas de alto valor cognitivo (e.g., puntuación total).
- **Módulos de Resumen (Widgets):** Tarjetas de alta densidad informativa, diseñadas para consultas rápidas sobre programación y cruces deportivos.

## 5. Diseño de Componentes (Design System Core)

### 5.1. Controles Interactivos (Botones)
- **Acción Primaria (Solid):** Fondo `#00B37E`, tipografía `#FFFFFF`, contornos suavizados para amabilidad visual.
- **Acción Secundaria (Outline):** Ausencia de relleno (transparente), delineado en borde gris claro o `#00B37E`, utilizado para reducir el peso visual en acciones no críticas.
- **Indicadores de Estado (Badges/Pills):** Geometría oval, combinando iconos de estado (e.g., conectividad) con tipografía secundaria.

### 5.2. Componentes de Entrada de Datos (Formularios)
- Contenedores de entrada con fondo sutilmente contrastante (`#0B111A` sobre tarjetas) y transiciones visibles en `:focus` (borde `#00B37E`) para asegurar accesibilidad y retroalimentación inmediata durante la interacción.

### 5.3. Sistema Iconográfico
- Estilo vectorial lineal, peso tipográfico equilibrado con la fuente base para evitar interferencias visuales. Simbología estándar de fácil decodificación orientada al entorno deportivo y tecnológico.

## 6. Documentación de Referencia

La implementación fidedigna debe basarse en los siguientes recursos centralizados:
- **Prototipo Interactivo (UI/UX):** [Figma - Soccer Tournament Management System](https://www.figma.com/make/qoYhlVEwEVM0H4NcYEmsNe/Soccer-Tournament-Management-System?p=f&fullscreen=1&preview-route=%2Flineup)
- **Archivos Locales:** Recursos disponibles en la ruta `docs/requirements/img.png`.

*Directriz Técnica: Se requiere que la implementación del Frontend utilice arquitecturas basadas en Design Tokens (e.g., variables CSS globales `:root { --color-primary: #00B37E; }`) para garantizar la mantenibilidad y consistencia del sistema.*
