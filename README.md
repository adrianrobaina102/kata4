# Kata 4 - Servicio HTTP con Javalin y Arquitectura Hexagonal

## Objetivo de la Entrega
Construir un servicio HTTP en Java capaz de exponer y consultar un catálogo de datos a través de endpoints configurables, procesando parámetros de consulta (`queryParams`), aplicando lógica de negocio desacoplada y transformando los objetos de dominio a respuestas en formato JSON. Todo el desarrollo se rige bajo los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**, separando estrictamente la interfaz HTTP, los puertos de entrada/salida, el caso de uso, el serializador y el modelo de datos.

## Vídeo Explicativo
- **Enlace al vídeo (YouTube):** https://youtu.be/TsOT3WgQ2mY
- **Duración aproximada:** 5:12

## Entorno y Requisitos
- **JDK:** Oracle OpenJDK 27 | **Gestor:** Maven | **IDE:** IntelliJ IDEA
- **Dependencias Externas:**
  - `Javalin` (`io.javalin:javalin:7.2.4`) para levantar el servidor web embebido, gestionar el ciclo de vida HTTP y definir las rutas de los endpoints.
  - `SLF4J Simple` (`org.slf4j:slf4j-simple:2.0.20`) para el registro y trazabilidad de los eventos del servidor en consola.
  - `Gson` (`com.google.code.gson:gson:2.15.0`) para la serialización del modelo de dominio hacia formato JSON.

## Estructura del Proyecto (Arquitectura Hexagonal)
El código se organiza bajo el paquete `software.ulpgc.katas`, aplicando una separación estricta de responsabilidades e inversión de dependencias:

- **Paquete `application.model` (El Núcleo):** Entidades de dominio puras y aisladas de cualquier tecnología externa. Se utiliza el `record` inmutable [`Movie`](src/main/java/software/ulpgc/katas/application/model/Movie.java) y el enumerado [`Genre`](src/main/java/software/ulpgc/katas/application/model/Genre.java) para tipar y blindar las categorías válidas del catálogo.
- **Paquete `application.ports` (Los Contratos):** Interfaces genéricas que expresan qué necesita el dominio para comunicarse con el exterior sin depender de implementaciones concretas:
  - [`Input`](src/main/java/software/ulpgc/katas/application/ports/Input.java) e `Input.Parameter`: Abstracción para la lectura de parámetros de entrada.
  - [`Output`](src/main/java/software/ulpgc/katas/application/ports/Output.java): Contrato que representa la respuesta procesada lista para ser entregada.
  - [`MovieSerializer`](src/main/java/software/ulpgc/katas/application/ports/MovieSerializer.java): Puerto encargado de transformar colecciones de objetos `Movie` a su representación textual.
- **Paquete `application.domain` (Casos de Uso):** Contiene la interfaz [`UseCase`](src/main/java/software/ulpgc/katas/application/domain/UseCase.java) y su implementación [`FindMoviesByGenreUseCase`](src/main/java/software/ulpgc/katas/application/domain/FindMoviesByGenreUseCase.java), que recibe el catálogo y el serializador por inyección de dependencias, normaliza el parámetro recibido y filtra las películas mediante Java Streams.
- **Paquete `infrastructure.adapters` (La Infraestructura):** Implementaciones concretas de los puertos y ensamblaje de la aplicación:
  - [`JavalinContextInput`](src/main/java/software/ulpgc/katas/infrastructure/adapters/JavalinContextInput.java): Adaptador HTTP que envuelve el `Context` de Javalin para extraer los parámetros de la URL (`queryParam`).
  - [`JsonMovieSerializer`](src/main/java/software/ulpgc/katas/infrastructure/adapters/JsonMovieSerializer.java): Adaptador que implementa `MovieSerializer` empleando `Gson` para generar la salida JSON.
  - [`Main`](src/main/java/software/ulpgc/katas/infrastructure/adapters/Main.java): Punto de entrada que actúa como inyector de dependencias, inicializa el servidor Javalin en el puerto `8080` y registra el endpoint `GET /movies`.

## Práctica Deliberada (Iteraciones)
Siguiendo la dinámica de repetición deliberada para afianzar el patrón de Puertos y Adaptadores en servicios HTTP:
- **Iteración 1 (Principal - Demostrada en el vídeo):** Servicio HTTP de catálogo de películas con filtrado dinámico por género (`GET /movies?genre={genre}`), validación mediante `enum` y serialización JSON con Gson.

## Flujo Git y Proceso de Desarrollo
- **Ramas base:** Uso de rama principal `master` y rama de desarrollo `develop`.
- **Desarrollo iterativo:** El código se construyó de adentro hacia afuera mediante commits atómicos y semánticos (`feat(domain)`, `feat(ports)`, `feat(application)`, `feat(infrastructure)`):
  1. Definición del modelo de dominio (`Movie`, `Genre`).
  2. Creación de los contratos de entrada, salida y serialización (`Input`, `Output`, `MovieSerializer`).
  3. Implementación de la lógica de negocio en el caso de uso (`UseCase`, `FindMoviesByGenreUseCase`).
  4. Implementación de los adaptadores de infraestructura (`JavalinContextInput`, `JsonMovieSerializer`) y configuración del servidor en `Main`.

## Ejecución Rápida y Verificación
1. Clonar el repositorio y abrir el proyecto en **IntelliJ IDEA**:
   ```bash
   git clone https://github.com/adrianrobaina102/kata4.git
   ```
2. Esperar a que Maven descargue las dependencias definidas en el `pom.xml` o compilar manualmente:
   ```bash
   mvn clean compile
   ```
3. Ejecutar la clase [`Main`](src/main/java/software/ulpgc/katas/infrastructure/adapters/Main.java) desde el IDE (`Shift + F10`) o por terminal:
   ```bash
   mvn exec:java -Dexec.mainClass="software.ulpgc.katas.infrastructure.adapters.Main"
   ```
4. Con el servidor activo en `http://localhost:8080`, verificar las siguientes peticiones desde el navegador, Postman o `curl`:

### Pruebas y Resultados Esperados
- **Consulta por género (Caso normal - Acción):**
  - **URL:** `http://localhost:8080/movies?genre=action` (o `?genre=ACTION`)
  - **Estado HTTP:** `200 OK`
  - **Respuesta JSON:**
    ```json
    [
      {"title":"The Dark Knight","genre":"ACTION","year":{"year":2008},"rating":"PG-13"},
      {"title":"Mad Max: Fury Road","genre":"ACTION","year":{"year":2015},"rating":"R"}
    ]
    ```
- **Consulta por género (Caso normal - Ciencia Ficción):**
  - **URL:** `http://localhost:8080/movies?genre=sci_fi`
  - **Estado HTTP:** `200 OK`
  - **Respuesta JSON:**
    ```json
    [
      {"title":"Inception","genre":"SCI_FI","year":{"year":2010},"rating":"PG-13"},
      {"title":"Interstellar","genre":"SCI_FI","year":{"year":2014},"rating":"PG-13"}
    ]
    ```
- **Casos de Borde y Errores:**
  - **Parámetro inválido o inexistente:** `http://localhost:8080/movies?genre=inexistente` ➔ El servidor rechaza el valor al no pertenecer al enumerado `Genre`, devolviendo un código HTTP de error (`500 Internal Server Error`) controlado por Javalin.
  - **Ruta no registrada:** `http://localhost:8080/otra-ruta` ➔ Devuelve código HTTP `404 Not Found`.
  - **Servicio no disponible:** Al detener la ejecución de `Main`, cualquier petición a `http://localhost:8080/movies` resulta en error de red (`ERR_CONNECTION_REFUSED`).
