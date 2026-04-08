package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "lineaPedido")
@Schema(description = "Entidad que representa la cantida de productos de una venta")
public class LineaPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único de la línea de pedido")
    private Long id;
    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    @NotNull(message = "El producto no puede ser nulo")
    @Column(name = "subtotal", nullable = false)
    @Min(value = 0, message = "El subtotal debe ser mayor o igual a 0")
    private float subtotal;
    @ManyToOne
    @JoinColumn(name = "productoZonaId", nullable = false)
    private ProductoZona productoZona;

    @ManyToOne
    @JoinColumn(name = "ventaId")
    private Venta venta;


}
