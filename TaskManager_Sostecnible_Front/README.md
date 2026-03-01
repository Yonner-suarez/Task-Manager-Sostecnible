# Task Manager - Frontend (Sostecnible)

Este proyecto es una **Single Page Application (SPA)** desarrollada para la gestión eficiente de tareas, cumpliendo con los requerimientos técnicos de la evaluación para Desarrollador FullStack en **Sostecnible**.

## 🛠️ Stack Tecnológico

- **Framework:** React.js (Vite)
- **Gestión de Estado:** Zustand (Estado Global)
- **Consumo de API:** React Query / TanStack Query (Caching y Sincronización)
- **Pruebas:** Vitest / React Testing Library (Pruebas unitarias)

---

## 🚀 Requisitos Previos

Es necesario tener instalado **Node.js** (versión LTS) para ejecutar los comandos de `npm`.

Clonar el repositorio:

```bash
git clone <url-de-tu-repo>
cd <carpeta-de-tu-proyecto>
```

## ⚙️ Configuración e Instalación

1. **Instalar dependencias:**

   ```bash
   npm i o npm isnstall
   ```

2. **Variables de Entorno (.env):**
   Crea un archivo .env en la raíz del proyecto para conectar con el backend (API RESTful en Java/Spring Boot):

   ```bash
   VITE_URL_API=tu conexion al servidor
   ```

## 🏃 Configuración e Instalación

**Modo Desarrollo**

Para iniciar el servidor local con Vite y HMR:

```bash
   npm run dev
```

**Ejecutar Pruebas**

Para ejecutar las pruebas unitarias que validan la lógica crítica:

```bash
   npm run test
```

## 📋 Funcionalidades Implementadas

- **Operaciones CRUD:** Gestión completa de tareas (Creación, Lectura, Actualización y Eliminación)
- **Modelo de Datos:** Incluye ID, Título, Descripción (obligatoria), Prioridad (Alta, Media, Baja), Fechas y Estado
- **Interfaz de Usuario:**
  - **Listado Principal:** Visualización de tareas obtenidas del backend
  - **Barra Lateral (Sidebar):** Panel para filtrar por Prioridad o Estado
  - **Buscador:** Campo interactivo para búsqueda por Título
  - **Detalle y Formulario:** Vistas para edición y creación con validación de datos
