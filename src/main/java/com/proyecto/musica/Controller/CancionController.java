package com.proyecto.musica.Controller;

import com.proyecto.musica.Model.Cancion;
import com.proyecto.musica.Service.CancionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canciones")
@RequiredArgsConstructor
public class CancionController {

    private final CancionService cancionService;

    // ── GET /api/v1/canciones ─────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Cancion>> getAll() {
        return ResponseEntity.ok(cancionService.obtenerTodas());
    }

    // ── GET /api/v1/canciones/{id} ────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Cancion> getById(@PathVariable Long id) {
        return cancionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * ── POST /api/v1/canciones ────────────────────────────────────────────────
     * Se recibe el artistaId como @RequestParam porque la entidad Cancion
     * no puede llevar el artista entero en el body (evita complejidad).
     * Ejemplo de body:
     * {
     * "titulo": "Bohemian Rhapsody",
     * "duracionSegundos": 354,
     * "anioLanzamiento": 1975,
     * "album": "A Night at the Opera"
     * }
     * Y en la URL: POST /api/v1/canciones?artistaId=1
     */
    @PostMapping
    public ResponseEntity<Cancion> create(
            @RequestBody Cancion cancion,
            @RequestParam Long artistaId) {
        return cancionService.crear(cancion, artistaId)
                .map(creada -> ResponseEntity.status(HttpStatus.CREATED).body(creada))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── PUT /api/v1/canciones/{id} ────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Cancion> update(
            @PathVariable Long id,
            @RequestBody Cancion cancion,
            @RequestParam Long artistaId) {
        return cancionService.actualizar(id, cancion, artistaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/v1/canciones/{id} ─────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (cancionService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ── GET /api/v1/canciones/artista/{artistaId} ── Módulo A ─────────────────
    // Devuelve todas las canciones de un artista concreto (navega la relación).
    @GetMapping("/artista/{artistaId}")
    public ResponseEntity<List<Cancion>> getByArtista(@PathVariable Long artistaId) {
        return ResponseEntity.ok(cancionService.obtenerPorArtista(artistaId));
    }

    // ── GET /api/v1/canciones/buscar ── Módulo B ──────────────────────────────
    // Búsqueda con parámetros opcionales.
    // Ejemplos:
    // GET /api/v1/canciones/buscar?titulo=love
    // GET /api/v1/canciones/buscar?album=thriller
    // GET /api/v1/canciones/buscar?anio=1985
    @GetMapping("/buscar")
    public ResponseEntity<List<Cancion>> buscar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String album,
            @RequestParam(required = false) Integer anio) {

        if (titulo != null && !titulo.isBlank()) {
            return ResponseEntity.ok(cancionService.buscarPorTitulo(titulo));
        }
        if (album != null && !album.isBlank()) {
            return ResponseEntity.ok(cancionService.buscarPorAlbum(album));
        }
        if (anio != null) {
            return ResponseEntity.ok(cancionService.buscarPorAnio(anio));
        }
        // Sin parámetros: devuelve todas
        return ResponseEntity.ok(cancionService.obtenerTodas());
    }
}
