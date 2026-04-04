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


    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL)
    private List<PersonaDomicilio> personaDomicilios;
}
