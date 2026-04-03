package soda_roja.backend.dtoResponse;

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
@Schema(description = "DTO Response para ProductoZona")
public class ProductoZonaDTOResponse {
    @Schema(example = "1", description = "Identificador único")
    private Long id;

    @NotNull(message = "La zona no puede ser nula")
    @Schema(description = "Información de la zona")
    private ZonaDTOResponse zona;

    @NotNull(message = "El producto no puede ser nulo")
    @Schema(description = "Información del producto")
    private ProductoDTOResponse producto;
}
