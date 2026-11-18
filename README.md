# Kata 4 - Creación de un servicio web con Javalin y Arquitectura Hexagonal

## Objetivo de la Entrega
Construir un servicio web HTTP en Java con **Javalin** capaz de exponer y consultar un catálogo de datos a través de rutas REST configurables, validando los parámetros de entrada, aplicando lógica de negocio desacoplada y devolviendo respuestas serializadas en JSON con **Gson** y cabecera `application/json`. Todo el desarrollo sigue los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)**, separando el dominio de la infraestructura HTTP y de la serialización.

## Vídeos Explicativos
- **Vídeo principal (YouTube):** https://youtu.be/TsOT3WgQ2mY *(Duración: 5:12)*
- **Anexo de actualización (YouTube):** https://youtu.be/s1h_R9gU910 *(Ajustes realizados tras la actualización del enunciado: `application/json`, validación en casos de uso y control de errores HTTP `400`)*

## Entorno y Requisitos
- **JDK:** Oracle OpenJDK 27 | **Gestor:** Maven | **IDE:** IntelliJ IDEA
- **Dependencias Externas:**
  - `Javalin` (`io.javalin:javalin:7.2.4`) para levantar el servidor web embebido en el puerto `8080`, gestionar el ciclo de vida HTTP y registrar las rutas.
  - `SLF4J Simple` (`org.slf4j:slf4j-simple:2.0.20`) para el registro y trazabilidad de los eventos del servidor en consola.
  - `Gson` (`com.google.code.gson:gson:2.15.0`) para la serialización del modelo de dominio hacia formato JSON.

## Estructura del Proyecto (Arquitectura Hexagonal)
El código se organiza bajo el paquete `software.ulpgc.katas`, aplicando una separación estricta de responsabilidades e inversión de dependencias:

- **Paquete `application.model` (El Núcleo):** Entidades de dominio puras y aisladas de cualquier tecnología externa. Se utilizan `records` inmutables (`Movie`, `Game`, `Book`) y enumerados (`Genre`, `Platform`, `Category`) para tipar las categorías válidas de cada catálogo.
- **Paquete `application.ports` (Los Contratos):** Interfaces genéricas que expresan qué necesita el dominio sin depender de implementaciones concretas:
  - `Input` e `Input.Parameter`: Abstracción para la lectura dinámica de parámetros (`asString()`, `asInteger()`).
  - `Output`: Contrato que representa la respuesta procesada lista para ser entregada.
  - `MovieSerializer` / `GameSerializer` / `BookSerializer`: Puertos encargados de transformar colecciones de objetos a texto.
- **Paquete `application.domain` (Casos de Uso):** Contiene la interfaz `UseCase` y las implementaciones de negocio (`FindMoviesByGenreUseCase`, `FindGamesByPlatformUseCase`, `FindGamesByScoreUseCase`, `FindBookByAuthorUseCase`, `FindBookByCategoryUseCase`), que validan la entrada y filtran los datos en memoria con Java Streams.
- **Paquete `infrastructure` / `infrastructure.adapters` (La Infraestructura):**
  - `JavalinContextInput`: Adaptador HTTP que envuelve el `Context` de Javalin para extraer los parámetros de consulta (`queryParam`).
  - Serializadores JSON (`JsonMovieSerializer`, `JsonGamesSerializer`, `JsonBookSerializer`): Adaptadores que implementan los serializadores empleando `Gson`.
  - `Main`: Punto de entrada que inyecta dependencias, arranca Javalin en el puerto `8080`, registra las rutas `GET`, establece `Content-Type: application/json` y traduce excepciones de validación a HTTP `400`.
- **Archivo [`requests.txt`](requests.txt):** Colección de comandos `curl` listos para ejecutar y verificar peticiones válidas e inválidas en cada rama.

## Práctica Deliberada (Iteraciones)
Siguiendo la dinámica de repetición deliberada para afianzar el patrón de Puertos y Adaptadores en servicios web con Javalin:
- **Rama `master` (Iteración 1 - Catálogo de Películas):** Endpoint `GET /movies?genre={genre}` con validación de parámetro obligatorio/enum (`400 Bad Request` si falta o no existe) y respuesta `200 OK` en `application/json`.
- **Rama `iteration-2` (Iteración 2 - Catálogo de Videojuegos):** Endpoint unificado `GET /games` con soporte para catálogo completo por defecto, filtro por plataforma (`?platform={platform}`) y filtro numérico (`?score={minScore}` entre `0` y `100` usando `asInteger()`), devolviendo `400 Bad Request` ante formato no numérico, rango inválido o plataforma inexistente.
- **Rama `iteration-3` (Iteración 3 - Catálogo de Libros):** Endpoint unificado `GET /books` con catálogo completo por defecto, filtro por autor (`?author={author}`) y filtro por categoría (`?category={category}`), devolviendo `400 Bad Request` si el parámetro viene vacío o la categoría no existe.

## Flujo Git y Proceso de Desarrollo
- **Ramas base:** Uso de rama principal `master` (versión final evaluable) y rama de desarrollo `develop`.
- **Desarrollo iterativo:** Construcción de adentro hacia afuera mediante commits atómicos y semánticos (`feat(domain)`, `feat(ports)`, `feat(application)`, `feat(infrastructure)`, `fix(...)`, `docs(...)`).

## Cómo Compilar y Ejecutar
1. Clonar el repositorio y elegir la rama a probar:
   ```bash
   git clone https://github.com/adrianrobaina102/kata4.git
   cd kata4
   git checkout master       # Iteración 1 (Películas) - Rama principal evaluable
   # o bien:
   git checkout iteration-2  # Iteración 2 (Videojuegos)
   git checkout iteration-3  # Iteración 3 (Libros)
   ```
2. Compilar el proyecto con Maven:
   ```bash
   mvn clean compile
   ```
3. Arrancar el servidor (puerto `8080`):
   - **En `master` e `iteration-3`:** ejecutar `Main` desde IntelliJ (`Shift + F10`) o mediante:
     ```bash
     mvn exec:java -Dexec.mainClass="software.ulpgc.katas.infrastructure.adapters.Main"
     ```
   - **En `iteration-2`:** ejecutar `Main` desde IntelliJ (`Shift + F10`) o mediante:
     ```bash
     mvn exec:java -Dexec.mainClass="software.ulpgc.katas.infrastructure.Main"
     ```

---

## Contrato HTTP y Evidencias de Verificación (`curl`)
*(Todos los comandos completos por rama están también recopilados en [`requests.txt`](requests.txt). En PowerShell usar `curl.exe -i`; en Linux/macOS/Git Bash usar `curl -i`.)*

### 1. Rama `master` — Servicio de Películas (`Puerto 8080`, `GET /movies`)
- **Petición válida (`200 OK`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/movies?genre=action"
  ```
  Respuesta esperada (`200 OK`):
  ```json
  [
    {"title":"The Dark Knight","genre":"ACTION","year":{"year":2008},"rating":"PG-13"},
    {"title":"Mad Max: Fury Road","genre":"ACTION","year":{"year":2015},"rating":"R"}
  ]
  ```
- **Petición inválida — género inexistente (`400 Bad Request`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/movies?genre=inexistente"
  ```
  Respuesta esperada (`400 Bad Request`):
  ```json
  {"error": "No enum constant software.ulpgc.katas.application.model.Genre.INEXISTENTE"}
  ```
- **Petición inválida — parámetro ausente (`400 Bad Request`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/movies"
  ```
  Respuesta esperada (`400 Bad Request`):
  ```json
  {"error": "El parámetro genre es obligatorio"}
  ```

### 2. Rama `iteration-2` — Servicio de Videojuegos (`Puerto 8080`, `GET /games`)
- **Peticiones válidas (`200 OK`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/games"
  curl.exe -i "http://localhost:8080/games?score=95"
  curl.exe -i "http://localhost:8080/games?platform=nintendo"
  ```
- **Peticiones inválidas (`400 Bad Request`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/games?score=noventa"
  curl.exe -i "http://localhost:8080/games?score=200"
  curl.exe -i "http://localhost:8080/games?platform=sega"
  ```

### 3. Rama `iteration-3` — Servicio de Libros (`Puerto 8080`, `GET /books`)
- **Peticiones válidas (`200 OK`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/books"
  curl.exe -i "http://localhost:8080/books?category=fantasy"
  curl.exe -i "http://localhost:8080/books?author=George%20Orwell"
  ```
- **Peticiones inválidas (`400 Bad Request`, `Content-Type: application/json`):**
  ```bash
  curl.exe -i "http://localhost:8080/books?category=terror"
  curl.exe -i "http://localhost:8080/books?author="
  ```
