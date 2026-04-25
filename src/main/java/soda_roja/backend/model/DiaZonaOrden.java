package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "dia_zona_orden")
@Schema(description = "Entidad que representa el orden en el que se atiende a las personas en una zona por día")

public class DiaZonaOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "orden", nullable = false)
    private Integer orden;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="domicilioId")
    @JsonBackReference
    private Domicilio domicilio;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="diaZonaId")
    @JsonBackReference
    private DiaZona diaZona;

}
