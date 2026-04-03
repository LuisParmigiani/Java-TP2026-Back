package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "pago")
@Schema(description = "Entidad que representa una persona")

public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "monto", nullable = false)
    private float monto;
    @Column(name = "fecha", nullable = false)
    private Date fecha;
    @Column(name = "metodoPago", nullable = false)
    private String metodoPago;
    @ManyToOne
    @JoinColumn(name="personaId")
    private Persona persona;

}
