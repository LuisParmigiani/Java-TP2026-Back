package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO Response para ProductoZona")
public class ProductoZonaDTOResponse {
    private Long id;
    private ZonaDTOResponse zona;
    private ProductoDTOResponse producto;
    private List<PedidoSemanalDTOResponse> pedidoSemanal;
}
