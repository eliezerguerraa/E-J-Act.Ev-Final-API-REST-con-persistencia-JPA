package com.proyecto.musica.Repository;

import com.proyecto.musica.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    /*
     * Método derivado: Spring genera el SQL automáticamente a partir del nombre.
     * Busca artistas cuyo nombre contenga el texto indicado, ignorando
     * mayúsculas/minúsculas.
     * SQL generado: SELECT * FROM artistas WHERE LOWER(nombre) LIKE
     * LOWER('%texto%')
     */
    List<Artista> findByNombreContainingIgnoreCase(String nombre);

    /*
     * Método derivado: filtra artistas por género exacto (ignorando mayúsculas).
     * SQL generado: SELECT * FROM artistas WHERE LOWER(genero) = LOWER('valor')
     */
    List<Artista> findByGeneroIgnoreCase(String genero);

    /*
     * Método derivado: filtra artistas por país.
     */
    List<Artista> findByPaisIgnoreCase(String pais);
}
