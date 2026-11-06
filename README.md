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

- **Paquete `application.model` (El Núcleo):** Entidades de dominio puras y aisladas de cualquier tecnología externa. Se utilizan `records` inmutables (ej. `Movie`, `Game`) y enumerados (`Genre`, `Platform`) para tipar y blindar las categorías válidas de cada catálogo.
- **Paquete `application.ports` (Los Contratos):** Interfaces genéricas que expresan qué necesita el dominio para comunicarse con el exterior sin depender de implementaciones concretas:
  - `Input` e `Input.Parameter`: Abstracción para la lectura dinámica de parámetros (`asString()`, `asInteger()`).
  - `Output`: Contrato que representa la respuesta procesada lista para ser entregada.
  - `MovieSerializer` / `GameSerializer`: Puertos encargados de transformar colecciones de objetos a su representación textual.
- **Paquete `application.domain` (Casos de Uso):** Contiene la interfaz `UseCase` y las implementaciones de negocio (`FindMoviesByGenreUseCase`, `FindGamesByPlatformUseCase`, `FindGamesByScoreUseCase`), que reciben el catálogo y el serializador por inyección de dependencias y aplican filtrado funcional con Java Streams.
- **Paquete `infrastructure.adapters` (La Infraestructura):** Implementaciones concretas de los puertos y ensamblaje de la aplicación:
  - `JavalinContextInput`: Adaptador HTTP que envuelve el `Context` de Javalin para extraer los parámetros de la URL de forma genérica.
  - `JsonMovieSerializer` / `JsonGamesSerializer`: Adaptadores que implementan los serializadores empleando `Gson` para generar la salida JSON.
  - `Main`: Punto de entrada que actúa como inyector de dependencias, inicializa el servidor Javalin en el puerto `8080` y registra los endpoints REST.

## Práctica Deliberada (Iteraciones)
Siguiendo la dinámica de repetición deliberada para afianzar el patrón de Puertos y Adaptadores en servicios HTTP a través de distintos dominios:
- **Rama `master` (Iteración 1 - Principal, demostrada en el vídeo):** Servicio HTTP de catálogo de películas con filtrado dinámico por género (`GET /movies?genre={genre}`), validación mediante `enum` y serialización JSON con Gson.
- **Rama `iteration-2` (Iteración 2 - Catálogo de Videojuegos):** Servicio HTTP unificado en `GET /games` con dos casos de uso desacoplados según el parámetro recibido:
  - Filtrado numérico por puntuación mínima (`?score={minScore}`) implementando `asInteger()` en el puerto `Input`.
  - Filtrado por plataforma (`?platform={platform}`).
  - Retorno del catálogo completo por defecto si no se especifican filtros.
  - Gestión defensiva de nulos y excepciones de formato numérico (`NumberFormatException`).
- **Rama `iteration-3` (Iteración 3 - Pokédex Express):** Siguiente iteración planificada para afianzar la arquitectura con un nuevo dominio.

## Flujo Git y Proceso de Desarrollo
- **Ramas base:** Uso de rama principal `master` y rama de desarrollo `develop`.
- **Desarrollo iterativo:** El código se construyó de adentro hacia afuera mediante commits atómicos y semánticos (`feat(domain)`, `feat(ports)`, `feat(application)`, `feat(infrastructure)`):
  1. Definición del modelo de dominio (`Movie`, `Genre` en iteración 1; `Game`, `Platform` en iteración 2).
  2. Creación de los contratos de entrada, salida y serialización (`Input`, `Output`, serializers).
  3. Implementación de la lógica de negocio en los casos de uso con Streams.
  4. Implementación de los adaptadores de infraestructura (`JavalinContextInput`, serializadores JSON) y configuración del servidor en `Main`.

## Ejecución Rápida y Verificación
1. Clonar el repositorio y abrir el proyecto en **IntelliJ IDEA**:
   ```bash
   git clone https://github.com/adrianrobaina102/kata4.git
   ```
2. Hacer `git checkout` a la rama que se desee probar (`master` o `iteration-2`):
   ```bash
   git checkout master       # Para probar la Iteración 1 (Películas)
   # o bien:
   git checkout iteration-2  # Para probar la Iteración 2 (Videojuegos)
   ```
3. Compilar el proyecto con Maven:
   ```bash
   mvn clean compile
   ```
4. Ejecutar la clase `Main` desde el IDE (`Shift + F10`) o por terminal:
   ```bash
   mvn exec:java -Dexec.mainClass="software.ulpgc.katas.infrastructure.Main"
   ```

---

### Verificación de la Iteración 1 (Rama `master` - Películas)
- **Consulta por género (Acción):** `http://localhost:8080/movies?genre=action` ➔ Devuelve *The Dark Knight* y *Mad Max: Fury Road* (`200 OK`).
- **Consulta por género (Sci-Fi):** `http://localhost:8080/movies?genre=sci_fi` ➔ Devuelve *Inception* e *Interstellar* (`200 OK`).
- **Parámetro inválido:** `http://localhost:8080/movies?genre=inexistente` ➔ Error 500 controlado al no pertenecer al enum.

---

### Verificación de la Iteración 2 (Rama `iteration-2` - Videojuegos)
- **Catálogo completo (sin parámetros):** `http://localhost:8080/games` ➔ Retorna los 11 juegos (`200 OK`).
- **Filtro por puntuación mínima:** `http://localhost:8080/games?score=95` ➔ Retorna únicamente los juegos con nota $\ge 95$ (*Zelda BotW*, *Super Mario Odyssey*, *The Last of Us Part I*, *Baldur's Gate 3*).
- **Filtro por plataforma:** `http://localhost:8080/games?platform=nintendo` ➔ Retorna los juegos de Nintendo (`Zelda`, `Mario`).
- **Filtro inexistente:** `http://localhost:8080/games?platform=sega` ➔ Retorna lista vacía `[]` de forma segura (`200 OK`).
- **Servicio no disponible:** Al detener la ejecución de `Main`, cualquier petición resulta en error de red (`ERR_CONNECTION_REFUSED`).
