package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZonaDTOResponse {
    
    @Schema(example = "1", description = "Identificador único")
    private Long id;
    
    @Schema(example = "Fisherton", description = "Nombre de la zona")
    private String nombre;
    
    @Schema(example = "Zona de Rosario", description = "Detalle de la zona")
    private String detalle;
}