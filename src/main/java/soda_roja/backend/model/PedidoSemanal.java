package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "PedidoSemanal")
@Schema(description = "Entidad que representa una PedidoSemanal del cliente")

public class PedidoSemanal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="domicilioId")
    @JsonBackReference
    private Domicilio domicilio ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productoZonaId")
    @JsonBackReference
    private ProductoZona productoZona;
    @Column(name = "cantidad", nullable = false)
    private int cantidad;



}
