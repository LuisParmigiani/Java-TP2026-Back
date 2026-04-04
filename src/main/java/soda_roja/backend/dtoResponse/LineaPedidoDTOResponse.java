package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO Response para LineaPedido")
public class LineaPedidoDTOResponse {
    @Schema(example = "1", description = "Identificador único de la línea de pedido")
    private Long id;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Schema(example = "5", description = "Cantidad de productos")
    private int cantidad;

    @NotNull(message = "El subtotal no puede ser nulo")
    @Min(value = 0, message = "El subtotal debe ser mayor o igual a 0")
    @Schema(example = "150.50", description = "Subtotal de la línea")
    private float subtotal;

    @NotNull(message = "ProductoZona no puede ser nulo")
    @Schema(description = "Información completa de ProductoZona")
    private ProductoZonaDTOResponse productoZona;
}
