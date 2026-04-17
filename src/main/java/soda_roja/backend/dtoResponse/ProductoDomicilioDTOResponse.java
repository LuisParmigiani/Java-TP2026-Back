package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO de respuesta para ProductoPersonaDomicilio")
public class ProductoDomicilioDTOResponse {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private ProductoDTOResponse producto;
    private DomicilioDTOResponse domicilio;
    private Integer cantVaciosActuales;
    private Long domicilioId;
}
