package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "domicilio")
@Schema(description = "Entidad que representa un domicilio del hogar")
public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "calle", nullable = false)
    private String calle;
    @Column(name = "numero", nullable = false)
    private String numero;
    @Column(name = "casa")
    private String casa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "zonaId", nullable = false)
    // joinColumn: el name es el nombre de la cplumna en la que se va a guardar la clave foranea
    private Zona zona;


    @ManyToOne
    @JoinColumn(name = "personaId")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camionId")
    private Camion camion;

    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas;
    //Le avisa a Lombok que el valor por defecto de dia es un array de booleanos con 7 posiciones,
    //cada una representando un día de la semana (0: domingo, 1: lunes, ..., 6: sábado). Esto es útil para indicar en qué días se realizan los domicilios.
    @Builder.Default
    @Column(name = "dia", nullable = false)
    private boolean[] dia = new boolean[7];

    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoDomicilio> productoDomicilio;
}
