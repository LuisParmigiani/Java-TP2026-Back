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
@Schema(description = "DTO de solicitud para DiaZona")
public class DiaZonaDTORequest {
    @NotNull(message = "El ID del día es requerido")
    @Schema(description = "ID del día", example = "1")
    private Long diaId;

    @NotNull(message = "El ID de la zona es requerido")
    @Schema(description = "ID de la zona", example = "1")
    private Long zonaId;
    
    
}
