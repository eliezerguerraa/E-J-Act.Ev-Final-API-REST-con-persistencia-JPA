package com.proyecto.musica.Repository;

import com.proyecto.musica.Model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    /*
     * Método derivado: busca canciones cuyo título contenga el texto dado.
     */
    List<Cancion> findByTituloContainingIgnoreCase(String titulo);

    /*
     * Método derivado: busca canciones de un artista concreto por su ID.
     * Módulo A: endpoint que navega la relación.
     */
    List<Cancion> findByArtistaId(Long artistaId);

    /*
     * Método derivado: busca canciones de un álbum concreto.
     */
    List<Cancion> findByAlbumContainingIgnoreCase(String album);

    /*
     * Método derivado: busca canciones lanzadas en un año concreto.
     */
    List<Cancion> findByAnioLanzamiento(Integer anio);
}