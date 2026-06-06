package com.proyecto.musica.Controller;

import com.proyecto.musica.Model.Artista;
import com.proyecto.musica.Service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @RestController: combina @Controller + @ResponseBody.
 *   Todos los métodos devuelven datos (JSON) directamente, sin vistas.
 * @RequestMapping: prefijo común para todos los endpoints de este controller.
 * @RequiredArgsConstructor: Lombok genera el constructor para inyectar ArtistaService.
 */
@RestController
@RequestMapping("/api/v1/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final ArtistaService artistaService;

    // ── GET /api/v1/artistas ──────────────────────────────────────────────────
    // Devuelve todos los artistas. 200 OK.
    @GetMapping
    public ResponseEntity<List<Artista>> getAll() {
        return ResponseEntity.ok(artistaService.obtenerTodos());
    }

    // ── GET /api/v1/artistas/{id} ─────────────────────────────────────────────
    // Devuelve un artista por ID. 200 OK o 404 Not Found.
    @GetMapping("/{id}")
    public ResponseEntity<Artista> getById(@PathVariable Long id) {
        return artistaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/v1/artistas ─────────────────────────────────────────────────
    // Crea un nuevo artista. 201 Created.
    @PostMapping
    public ResponseEntity<Artista> create(@RequestBody Artista artista) {
        Artista creado = artistaService.crear(artista);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ── PUT /api/v1/artistas/{id} ─────────────────────────────────────────────
    // Actualiza un artista existente. 200 OK o 404 Not Found.
    @PutMapping("/{id}")
    public ResponseEntity<Artista> update(@PathVariable Long id, @RequestBody Artista artista) {
        return artistaService.actualizar(id, artista)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE /api/v1/artistas/{id} ──────────────────────────────────────────
    // Elimina un artista. 204 No Content o 404 Not Found.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (artistaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ── GET /api/v1/artistas/buscar ── Módulo B ───────────────────────────────
    // Búsqueda con parámetros opcionales.
    // Ejemplos:
    // GET /api/v1/artistas/buscar?nombre=queen
    // GET /api/v1/artistas/buscar?genero=rock
    // GET /api/v1/artistas/buscar?pais=españa
    @GetMapping("/buscar")
    public ResponseEntity<List<Artista>> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String pais) {

        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(artistaService.buscarPorNombre(nombre));
        }
        if (genero != null && !genero.isBlank()) {
            return ResponseEntity.ok(artistaService.buscarPorGenero(genero));
        }
        if (pais != null && !pais.isBlank()) {
            return ResponseEntity.ok(artistaService.buscarPorPais(pais));
        }
        // Sin parámetros: devuelve todos
        return ResponseEntity.ok(artistaService.obtenerTodos());
    }
}
