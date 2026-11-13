# 🔍 BuscaYA – Comercios y Farmacias de Turno

**BuscaYA** es una aplicación móvil desarrollada para facilitar la búsqueda de farmacias de turno y el acceso a información de establecimientos comerciales de Villa del Rosario, Córdoba, Argentina.

El proyecto busca ofrecer una herramienta sencilla e intuitiva que permita consultar información de utilidad desde un dispositivo Android.

## 🎯 Objetivo del proyecto

El objetivo principal es centralizar información de interés para los habitantes de Villa del Rosario, facilitando el acceso a los datos de las farmacias de turno y sus establecimientos.

## ✨ Funcionalidades

- **Consulta de farmacias de turno:** obtiene información mediante una API REST.
- **Búsqueda local:** permite filtrar las farmacias almacenadas en la base de datos.
- **Detalle de establecimientos:** muestra información como nombre, dirección, fecha de turno y teléfono cuando está disponible.
- **Acceso a mapas:** permite abrir una aplicación de mapas para consultar una dirección.
- **Acceso telefónico:** permite iniciar una llamada desde el detalle del establecimiento.
- **Almacenamiento local:** guarda información mediante Room para disponer de datos aunque no se pueda consultar la API.
- **Datos de respaldo:** incorpora información local de ejemplo cuando no hay datos disponibles de la API y la base está vacía.

## 🛠️ Tecnologías utilizadas

| Tecnología | Utilización |
|---|---|
| Kotlin | Lenguaje de programación |
| Android Studio | Entorno de desarrollo |
| Jetpack Compose | Construcción de interfaces |
| Retrofit | Consumo de servicios REST |
| Gson | Conversión de respuestas JSON |
| Room | Persistencia de datos local |
| Kotlin Coroutines y Flow | Operaciones asíncronas y observación de datos |
| Coil | Carga de imágenes |
| MVVM | Organización de la aplicación |

## 🏗️ Arquitectura del proyecto

La aplicación utiliza el patrón arquitectónico **MVVM (Model - View - ViewModel)** para organizar el código y separar las responsabilidades.

La estructura principal se organiza de la siguiente manera:

```text
com.example.miapp/
│
├── data/
│   ├── model/
│   │   ├── Farmacia.kt
│   │   ├── FarmaciaDto.kt
│   │   └── NegocioResult.kt
│   │
│   ├── remote/
│   │   ├── BuscayaApiService.kt
│   │   └── RetrofitClient.kt
│   │
│   └── repository/
│       └── PharmacyRepository.kt
│
├── ui/
│   └── theme/
│
├── MainActivity.kt
├── MainViewModel.kt
├── home.kt
│
├── AppDatabase.kt
├── ProductoDao.kt
├── ProductoEntity.kt
└── ProductoRepository.kt
```

### ¿Cómo funciona la arquitectura?

- **Model:** contiene las clases que representan los datos de las farmacias y los resultados de búsqueda.
- **View:** corresponde a las pantallas desarrolladas con Jetpack Compose.
- **ViewModel:** administra los datos que utiliza la interfaz y comunica las acciones del usuario con el repositorio.
- **Repository:** coordina el acceso a los datos provenientes de la API y de la base de datos local.
- **Retrofit:** realiza las solicitudes HTTP al servicio web.
- **Room:** permite almacenar y consultar información localmente.

## 🌐 Consumo de API REST

Para obtener las farmacias de turno se utiliza Retrofit.

**URL base:**

```text
https://buscaya.com.ar/api/
```

**Endpoint utilizado:**

```http
GET /farmacias_turno_android
```

También se encuentra declarado un servicio de búsqueda de comercios:

```http
GET /hybrid-search-android
```

Este último está definido en el servicio Retrofit, pero todavía no está conectado al flujo de búsqueda de la pantalla principal.

## 💻 Instalación y ejecución

Para ejecutar el proyecto localmente:

1. Clonar el repositorio:

```bash
git clone https://github.com/Naomi46/BuscaYAAPP.git
```

2. Abrir Android Studio.

3. Seleccionar **Open** y elegir la carpeta del proyecto.

4. Esperar a que Gradle sincronice las dependencias.

5. Configurar un emulador Android o conectar un dispositivo físico.

6. Ejecutar la aplicación desde Android Studio mediante el botón **Run**.

### Requisitos

- Android Studio.
- JDK compatible con la configuración de Gradle.
- Android SDK 36.
- Dispositivo o emulador con Android 7.0 (API 24) o superior.
- Conexión a Internet para consultar la API.

## 👩‍💻 Desarrollo

BuscaYA es un proyecto académico realizado como parte de mi formación en Desarrollo de Software.

Durante su desarrollo trabajé con Kotlin, interfaces mediante Jetpack Compose, consumo de APIs REST, persistencia local con Room y organización del código mediante MVVM.

El proyecto me permitió aplicar conocimientos de desarrollo móvil y comprender cómo integrar servicios externos con una aplicación Android.

---

**Proyecto:** BuscaYA  
**Plataforma:** Android  
**Ubicación:** Villa del Rosario, Córdoba, Argentina  
**Repositorio:** [Naomi46/BuscaYAAPP](https://github.com/Naomi46/BuscaYAAPP)
