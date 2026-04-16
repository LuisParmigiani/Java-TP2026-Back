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
    private Long id;
    private int cantidad;
    private float subtotal;
    private ProductoZonaDTOResponse productoZona;
}
