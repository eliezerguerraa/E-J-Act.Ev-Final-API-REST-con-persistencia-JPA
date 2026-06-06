package com.proyecto.musica.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "artistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String genero;

    // Año de inicio de la carrera musical
    private Integer anioInicio;

    private String pais;

    /*
     * @OneToMany: un artista tiene muchas canciones.
     * mappedBy = "artista" indica que la FK está en la tabla de canciones.
     * cascade = ALL: si borramos un artista, se borran sus canciones.
     * fetch = LAZY: las canciones NO se cargan automáticamente (mejor rendimiento).
     *
     * @JsonIgnore: evita la referencia circular al serializar a JSON.
     * Sin esto, al devolver un Artista, Jackson intentaría serializar
     * sus canciones, y cada canción volvería a serializar su artista → bucle
     * infinito.
     *
     * @ToString.Exclude: evita el mismo bucle infinito con el toString() de Lombok.
     */
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Cancion> canciones;
}
