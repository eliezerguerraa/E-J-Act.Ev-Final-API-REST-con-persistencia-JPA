package com.proyecto.musica.Service;

import com.proyecto.musica.Model.Cancion;
import com.proyecto.musica.Repository.ArtistaRepository;
import com.proyecto.musica.Repository.CancionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancionService {

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;

    public List<Cancion> obtenerTodas() {
        return cancionRepository.findAll();
    }

    public Optional<Cancion> obtenerPorId(Long id) {
        return cancionRepository.findById(id);
    }

    /*
     * Al crear una canción se recibe el artistaId en el body.
     * El service valida que ese artista exista antes de persistir.
     * Si no existe, devuelve Optional.empty() y el controller responde 404.
     */
    public Optional<Cancion> crear(Cancion cancion, Long artistaId) {
        return artistaRepository.findById(artistaId).map(artista -> {
            cancion.setArtista(artista);
            return cancionRepository.save(cancion);
        });
    }

    public Optional<Cancion> actualizar(Long id, Cancion datosNuevos, Long artistaId) {
        return cancionRepository.findById(id).flatMap(cancion ->
            artistaRepository.findById(artistaId).map(artista -> {
                cancion.setTitulo(datosNuevos.getTitulo());
                cancion.setDuracionSegundos(datosNuevos.getDuracionSegundos());
                cancion.setAnioLanzamiento(datosNuevos.getAnioLanzamiento());
                cancion.setAlbum(datosNuevos.getAlbum());
                cancion.setArtista(artista);
                return cancionRepository.save(cancion);
            })
        );
    }

    public boolean eliminar(Long id) {
        if (cancionRepository.existsById(id)) {
            cancionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ── Módulo A: endpoint por relación ───────────────────────────────────────

    public List<Cancion> obtenerPorArtista(Long artistaId) {
        return cancionRepository.findByArtistaId(artistaId);
    }

    // ── Módulo B: métodos de búsqueda ─────────────────────────────────────────

    public List<Cancion> buscarPorTitulo(String titulo) {
        return cancionRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Cancion> buscarPorAlbum(String album) {
        return cancionRepository.findByAlbumContainingIgnoreCase(album);
    }

    public List<Cancion> buscarPorAnio(Integer anio) {
        return cancionRepository.findByAnioLanzamiento(anio);
    }
}
