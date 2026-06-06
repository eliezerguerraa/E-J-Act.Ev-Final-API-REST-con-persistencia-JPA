package com.proyecto.musica.Service;

import com.proyecto.musica.Model.Artista;
import com.proyecto.musica.Repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
 * @Service: marca esta clase como componente de la capa de negocio.
 * @RequiredArgsConstructor (Lombok): genera un constructor con todos los campos
 *   marcados como 'final', lo que permite la inyección de dependencias sin @Autowired.
 */
@Service
@RequiredArgsConstructor
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public List<Artista> obtenerTodos() {
        return artistaRepository.findAll();
    }

    public Optional<Artista> obtenerPorId(Long id) {
        return artistaRepository.findById(id);
    }

    public Artista crear(Artista artista) {
        return artistaRepository.save(artista);
    }

    public Optional<Artista> actualizar(Long id, Artista datosNuevos) {
        return artistaRepository.findById(id).map(artista -> {
            artista.setNombre(datosNuevos.getNombre());
            artista.setGenero(datosNuevos.getGenero());
            artista.setAnioInicio(datosNuevos.getAnioInicio());
            artista.setPais(datosNuevos.getPais());
            return artistaRepository.save(artista);
        });
    }

    public boolean eliminar(Long id) {
        if (artistaRepository.existsById(id)) {
            artistaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ── Módulo B: métodos de búsqueda ──────────────────────────────────────────

    public List<Artista> buscarPorNombre(String nombre) {
        return artistaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Artista> buscarPorGenero(String genero) {
        return artistaRepository.findByGeneroIgnoreCase(genero);
    }

    public List<Artista> buscarPorPais(String pais) {
        return artistaRepository.findByPaisIgnoreCase(pais);
    }
}
