package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO Request para crear/actualizar ProductoZona")
public class ProductoZonaDTORequest {
    @NotNull(message = "La zona no puede ser nula")
    @Schema(example = "1", description = "ID de la zona")
    private Long zonaId;

    @NotNull(message = "El producto no puede ser nulo")
    @Schema(example = "1", description = "ID del producto")
    private Long productoId;
}
