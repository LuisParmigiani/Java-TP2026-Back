package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiaDomicilioDTORequestPut {
	//Lo inclui para el update multiple, en el resto de los casos se usa el que va por url
	private Long id;
	@Schema(description = "Estado de ese dia para ese domicilio", example = "ACTIVO")
    private String estado;

    @Schema(description = "ID de el domicilio relacionado con el dia", example = "1")
    private Long domicilioId;
    
    @Schema(description = "ID de el dia relacionado con ese domicilio", example = "2")
    private Long diaId;
}
