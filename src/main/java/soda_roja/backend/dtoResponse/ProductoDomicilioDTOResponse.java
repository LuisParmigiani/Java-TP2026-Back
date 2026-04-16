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
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Long DomicilioId;
    private Integer cantVaciosActuales;
}
