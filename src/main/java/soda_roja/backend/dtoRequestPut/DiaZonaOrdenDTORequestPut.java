package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiaZonaOrdenDTORequestPut {
	//Tuve que ponerle id para el caso en que se ordenan todos al mismot tiempo, sino no los podria identificar
	private Long id;
    private Integer orden;

    @Schema(description = "Id del domicilio que se atiende en ese dia y esa zona", example = "1")
    private Long domicilioId;
    
    @Schema(description = "Id de la agregación entre dia y Zona", example = "1")
    private Long diaZonaId;
}
