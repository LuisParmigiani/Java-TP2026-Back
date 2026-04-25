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
@Schema(description = "DTO de respuesta para Dia")
public class DiaDTOResponse {
    @Schema(description = "ID del día", example = "1")
    private Long id;

    @Schema(description = "Nombre del día", example = "lunes")
    private String nombre;
}
