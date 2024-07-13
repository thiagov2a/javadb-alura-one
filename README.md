<h4 align="center">
  🎓 Alura + ONE
</h4>

<h1 align="center">
  💻 ScreenMatch
</h1>

<p align="center">
  Este documento contiene el desarrollo y la documentación del proyecto ScreenMatch del programa Alura + ONE.
</p>

## 📝 Descripción del Proyecto

ScreenMatch es una aplicación diseñada para buscar series y episodios, consultar información variada y gestionar datos de series a través de un frontend interactivo. La aplicación permite guardar series en una base de datos y consultar detalles mediante una interfaz de consola.

## 📋 Funcionalidades

- **Búsqueda de Series**: Permite buscar series a través de una API y consultar detalles.
- **Gestión de Episodios**: Consulta y muestra episodios de una serie seleccionada.
- **Base de Datos**: Guarda información de series y episodios en una base de datos para su consulta futura.
- **Interfaz de Usuario**: Incluye un frontend en `src/main/resources` que facilita la interacción con el usuario.

## 📁 Estructura del Proyecto

- **`src/main/java/com/thiagov2a/screenmatch`**: Contiene el código fuente de la aplicación dividido en:
  - **`config`**: Configuración de la aplicación.
  - **`controller`**: Controladores de la aplicación.
  - **`dto`**: Objetos de transferencia de datos.
  - **`main`**: Clase principal de la aplicación.
  - **`model`**: Modelos de datos.
  - **`repository`**: Repositorios para acceso a datos.
  - **`service`**: Servicios de la aplicación.
- **`src/main/resources`**: Contiene los archivos del frontend y recursos estáticos, incluyendo:
  - **`static`**: Archivos estáticos como CSS y JavaScript.
  - **`templates`**: Plantillas HTML.
  - **`application.properties`**: Configuración de la aplicación.
- **`pom.xml`**: Archivo de configuración de Maven.

## 🚀 Cómo Ejecutar el Proyecto

1. **Clonar el Repositorio**: `git clone https://github.com/tu-usuario/javadb-alura-one.git`
2. **Navegar al Directorio**: `cd javadb-alura-one`
3. **Ejecutar el Proyecto**: Utilice su IDE favorito para compilar y ejecutar la aplicación. Asegúrese de que la base de datos está configurada correctamente y que los recursos en `src/main/resources` están accesibles.

## 🛠 Tecnologías Utilizadas

- **Java**: 💻 Lenguaje de programación principal.
- **Spring Boot**: 🚀 Framework utilizado para desarrollar la aplicación.
- **Maven**: 📦 Sistema de gestión de proyectos.
- **PostgreSQL**: 🗄️ Sistema de gestión de bases de datos.
- **HTML/CSS/JavaScript**: 🌐 Interfaz de usuario en el frontend proporcionada por Alura.

<p align="center">
  Alura + ONE | ScreenMatch
</p>
