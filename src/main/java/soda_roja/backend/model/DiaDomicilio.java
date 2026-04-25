package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "dia_domicilio")
@Schema(description = "Entidad intermedia entre Dia y Domicilio")
public class DiaDomicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dia_id", nullable = false)
    @JsonBackReference
    private Dia dia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domicilio_id", nullable = false)
    @JsonBackReference
    private Domicilio domicilio;

    @Column(name = "estado", nullable = false)
    private String estado;
}
