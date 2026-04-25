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
@Schema(description = "DTO de solicitud para DiaZona")
public class DiaZonaDTORequestPut{
    @Schema(description = "ID del día", example = "1")
    private Long diaId;

    @Schema(description = "ID de la zona", example = "1")
    private Long zonaId;
    
    
}
