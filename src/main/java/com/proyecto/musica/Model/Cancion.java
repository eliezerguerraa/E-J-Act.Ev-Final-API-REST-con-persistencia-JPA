package com.proyecto.musica.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "canciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    // Duración en segundos
    private Integer duracionSegundos;

    private Integer anioLanzamiento;

    private String album;

    /*
     * @ManyToOne: muchas canciones pertenecen a un artista.
     * 
     * @JoinColumn: define el nombre de la FK en la tabla "canciones".
     * Sin @JoinColumn, Hibernate generaría un nombre automático poco legible.
     * fetch = LAZY: el artista no se carga hasta que se acceda al campo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;
}
