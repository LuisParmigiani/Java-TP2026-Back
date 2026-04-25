package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiaDomicilioDTORequest {
	
	@Pattern(regexp = "^(ACTIVO|INACTIVO)$", message = "El estado debe ser 'ACTIVO' o 'INACTIVO'")
    @Schema(description = "Estado de ese dia para ese domicilio", example = "ACTIVO")
    private String estado;

    @NotNull(message = "El id de el domicilio no puede ser nulo")
    @Schema(description = "ID de el domicilio relacionado con el dia", example = "1")
    private Long domicilioId;
    
    @NotNull(message = "El id de el dia no puede ser nulo")
    @Schema(description = "ID de el dia relacionado con ese domicilio", example = "2")
    private Long diaId;
}
