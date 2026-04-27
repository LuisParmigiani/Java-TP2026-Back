package soda_roja.backend.dtoRequest;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soda_roja.backend.dtoResponse.DiaZonaOrdenDTOResponse;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO para actualizar DiaZona con sus órdenes")
public class DiaZonaDTORequestWithOrdenes {
	
    private Long diaId;
    private Long zonaId;
    private List<DiaZonaOrdenDTORequest> diaZonaOrdenes;
}
