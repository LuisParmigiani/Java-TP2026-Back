package soda_roja.backend.model;

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
@Table(name = "productoDomicilio")
@Schema(description = "Tabla intermedia entre Producto y PersonaDomicilio")
public class ProductoDomicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "cantVaciosActuales", nullable = false)
    private Integer cantVaciosActuales;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DomicilioId", nullable = false)
    private Domicilio domicilio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productoId", nullable = false)
    private Producto producto;
}
