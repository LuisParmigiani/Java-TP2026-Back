package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiaZonaOrdenDTORequest {
	//Solo se va a utilizar para el orden de los repartos
	private Long id;
	
    @NotNull(message = "El orden no puede ser nulo")
    @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
    private Integer orden;

    @NotNull(message = "El id del domicilio no puede ser nulo")
    @Schema(description = "Id del domicilio que se atiende en ese dia y esa zona", example = "1")
    private Long domicilioId;
    
    @NotNull(message = "El id del DiaZona no puede ser nulo")
    @Schema(description = "Id de la agregación entre dia y Zona", example = "1")
    private Long diaZonaId;
}
