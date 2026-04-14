package soda_roja.backend.dtoRequestPut;

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
public class ProductoZonaDTORequestPut {
    @Schema(example = "1", description = "ID de la zona")
    private Long zonaId;

    @Schema(example = "1", description = "ID del producto")
    private Long productoId;
}
