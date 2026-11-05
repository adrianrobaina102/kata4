# Kata 4 — Servicio HTTP con Arquitectura Hexagonal

## Objetivo de la Entrega
Construir un microservicio HTTP en Java que exponga un recurso web documentado, separando de forma estricta las responsabilidades mediante **Arquitectura Hexagonal (Puertos y Adaptadores)**:
* **Modelo:** Entidades de dominio inmutables.
* **Puertos:** Interfaces abstractas que declaran las necesidades de entrada, salida y serialización de la aplicación.
* **Casos de Uso:** Lógica de negocio pura, completamente desacoplada de frameworks web o librerías de serialización.
* **Adaptadores:** Implementaciones de infraestructura (Javalin para HTTP y Gson para serialización JSON).

---

## Dependencias y Versión de JDK
* **Java:** JDK 27
* **Gestor de dependencias:** Apache Maven
* **Librerías principales:**
  * `io.javalin:javalin:7.2.4` (Servidor HTTP ligero)
  * `org.slf4j:slf4j-simple:2.0.20` (Logging)
  * `com.google.code.gson:gson:2.15.0` (Serialización JSON)

---

## Cómo Compilar y Ejecutar

### Compilación
```bash
mvn clean compile
```

### Ejecución
Ejecutar la clase principal `software.ulpgc.katas.infrastructure.adapters.Main` desde el IDE o por terminal:
```bash
mvn exec:java -Dexec.mainClass="software.ulpgc.katas.infrastructure.adapters.Main"
```
El servidor se iniciará por defecto en `http://localhost:8080`.

---

## Estructura del Proyecto

```text
src/main/java/software/ulpgc/katas/
├── application/
│   ├── domain/
│   │   ├── UseCase.java                   <- Interfaz base de caso de uso
│   │   └── FindMoviesByGenreUseCase.java  <- Lógica de negocio y filtrado
│   ├── model/
│   │   ├── Genre.java                     <- Enum de géneros cinematográficos
│   │   └── Movie.java                     <- Record de dominio inmutable
│   └── ports/
│       ├── Input.java                     <- Puerto de entrada para parámetros
│       ├── Output.java                    <- Puerto de salida para resultados
│       └── MovieSerializer.java           <- Puerto para la serialización
└── infrastructure/
    └── adapters/
        ├── JavalinContextInput.java       <- Adaptador de Javalin Context a Input
        ├── JsonMovieSerializer.java       <- Adaptador Gson a MovieSerializer
        └── Main.java                      <- Bootstrap del servidor Javalin
```

---

## Repeticiones Deliberadas
* **Repetición 1 (Base):** Endpoint `GET /movies` con filtrado opcional por query param `?genre={valor}` y catálogo de películas en memoria.
* **Repetición 2 (Variación):** Próxima variación con nuevo endpoint o filtrado adicional.

---

## Evidencias de Verificación

* **Petición General:** `GET http://localhost:8080/movies` ➔ Retorna lista completa (200 OK).
* **Filtrado por Género:** `GET http://localhost:8080/movies?genre=action` ➔ Retorna películas de acción (200 OK).
* **Parámetro Insensible a Mayúsculas:** `GET http://localhost:8080/movies?genre=sci_fi` ➔ Retorna películas Sci-Fi (200 OK).
* **Caso de Borde Controlado:** `GET http://localhost:8080/movies?genre=inexistente` ➔ Retorna `[]` sin lanzar error 500.

---

## Enlace al Vídeo Explicativo
* [Vídeo Explicativo (máx. 7 minutos) - Enlace Pendiente](https://youtube.com)
