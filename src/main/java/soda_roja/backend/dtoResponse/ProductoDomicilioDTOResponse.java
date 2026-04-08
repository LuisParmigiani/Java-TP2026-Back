package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO de respuesta para ProductoPersonaDomicilio")
public class ProductoDomicilioDTOResponse {

    @Schema(description = "ID de la relación", example = "1")
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Coca Cola")
    private String nombreProducto;

    @Schema(description = "ID de la persona domicilio", example = "1")
    private Long DomicilioId;

    @Schema(description = "Cantidad de vacíos actuales", example = "5")
    private Integer cantVaciosActuales;

    @Schema(description = "Aproximación semanal de consumo de ese producto", example = "2")
    private Integer aproxSemanal;
}
