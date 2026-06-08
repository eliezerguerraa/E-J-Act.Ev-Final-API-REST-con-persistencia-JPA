## 1. Descripción del dominio

Sistema de gestión de música que permite registrar **artistas** y sus **canciones**. Cada canción pertenece a exactamente un artista.

---

## 2. Diagrama Entidad-Relación

```
┌─────────────────────────┐         ┌───────────────────────────┐
│         ARTISTAS        │         │         CANCIONES         │
├─────────────────────────┤         ├───────────────────────────┤
│ PK  id          BIGINT  │◄────┐   │ PK  id          BIGINT    │
│     nombre      VARCHAR │     └───┤ FK  artista_id  BIGINT    │
│     genero      VARCHAR │         │     titulo      VARCHAR   │
│     anio_inicio INT     │         │     duracion_segundos INT │
│     pais        VARCHAR │         │     anio_lanzamiento INT  │
└─────────────────────────┘         │     album       VARCHAR   │
                                    └───────────────────────────┘

Relación: ARTISTAS (1) ──── (N) CANCIONES
          Un artista puede tener muchas canciones.
          Una canción pertenece a exactamente un artista.
```

**Clave foránea:** `canciones.artista_id` → `artistas.id`

---

## 3. Base de datos

- **Motor:** MySQL 8
- **Nombre de la BD:** `musica_db`
- **Creación de tablas:** automática mediante `spring.jpa.hibernate.ddl-auto=update`

---

## 4. Lista completa de endpoints

### 4.1 Artistas

| Método | Ruta | Descripción | Respuesta éxito | Respuesta error |
|--------|------|-------------|-----------------|-----------------|
| GET | `/api/v1/artistas` | Lista todos los artistas | 200 OK | — |
| GET | `/api/v1/artistas/{id}` | Artista por ID | 200 OK | 404 Not Found |
| POST | `/api/v1/artistas` | Crea un artista | 201 Created | — |
| PUT | `/api/v1/artistas/{id}` | Actualiza un artista | 200 OK | 404 Not Found |
| DELETE | `/api/v1/artistas/{id}` | Elimina un artista | 204 No Content | 404 Not Found |
| GET | `/api/v1/artistas/buscar?nombre=X` | Búsqueda por nombre (parcial) | 200 OK | — |
| GET | `/api/v1/artistas/buscar?genero=X` | Filtra por género | 200 OK | — |
| GET | `/api/v1/artistas/buscar?pais=X` | Filtra por país | 200 OK | — |

### 4.2 Canciones

| Método | Ruta | Descripción | Respuesta éxito | Respuesta error |
|--------|------|-------------|-----------------|-----------------|
| GET | `/api/v1/canciones` | Lista todas las canciones | 200 OK | — |
| GET | `/api/v1/canciones/{id}` | Canción por ID | 200 OK | 404 Not Found |
| POST | `/api/v1/canciones?artistaId=X` | Crea canción para un artista | 201 Created | 404 (artista no existe) |
| PUT | `/api/v1/canciones/{id}?artistaId=X` | Actualiza una canción | 200 OK | 404 Not Found |
| DELETE | `/api/v1/canciones/{id}` | Elimina una canción | 204 No Content | 404 Not Found |
| GET | `/api/v1/canciones/artista/{id}` | Canciones de un artista | 200 OK | — |
| GET | `/api/v1/canciones/buscar?titulo=X` | Búsqueda por título (parcial) | 200 OK | — |
| GET | `/api/v1/canciones/buscar?album=X` | Filtra por álbum | 200 OK | — |
| GET | `/api/v1/canciones/buscar?anio=X` | Filtra por año | 200 OK | — |

---

## 5. Arquitectura por capas

```
HTTP Request
     │
     ▼
┌─────────────────┐
│   Controller    │  Recibe la petición HTTP, llama al Service, devuelve ResponseEntity
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Service      │  Contiene la lógica de negocio, usa el Repository
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Repository    │  Extiende JpaRepository, accede a la BD
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Base de datos │  MySQL — tablas artistas y canciones
└─────────────────┘
```

---

## 6. Decisiones técnicas justificadas

### @JsonIgnore en Artista.canciones
Sin `@JsonIgnore`, al serializar un `Artista` a JSON, Jackson serializa su lista de canciones, y cada `Cancion` volvería a serializar su `Artista`, generando un **bucle infinito** que provoca un error `StackOverflowError`. La anotación corta ese ciclo.

### @ToString.Exclude en Artista.canciones
Lombok genera `toString()` automáticamente. Sin esta anotación, `toString()` de `Artista` incluiría sus canciones, que incluirían al artista, generando el mismo bucle en los logs.

### FetchType.LAZY
Tanto en `@ManyToOne` como en `@OneToMany` se usa `LAZY` para que Hibernate **no cargue los datos relacionados automáticamente**. Solo se cargan cuando se accede explícitamente al campo. Esto mejora el rendimiento evitando `JOINs` innecesarios.

### Optional<T>
Se usa en todos los métodos que pueden no encontrar resultado (`findById`, etc.) para evitar `NullPointerException`. En el controller siempre se encadena con `.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build())`, nunca con `.get()` directamente.

### artistaId como @RequestParam en POST /canciones
En lugar de incluir el objeto `Artista` completo dentro del body de la `Cancion`, se recibe solo el `artistaId` como parámetro de URL. Esto simplifica el body del request y deja la responsabilidad de buscar y vincular la entidad al `Service`.

### cascade = CascadeType.ALL
Al borrar un artista, sus canciones se borran también automáticamente en cascada. Esto mantiene la integridad referencial sin necesidad de borrarlas manualmente.

---

## 7. Capturas de tablas en BD

*(Añadir capturas de MySQL Workbench / DBeaver mostrando las tablas `artistas` y `canciones` con la FK `artista_id`)*
