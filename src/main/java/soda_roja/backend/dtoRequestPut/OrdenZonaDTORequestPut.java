package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa la relación entre una orden y una zona")
public class OrdenZonaDTORequestPut {

    @Schema(example = "1", description = "Día de la semana")
    private Integer dia;

    @Schema(example = "1", description = "Orden en el que se recorre el domicilio")
    private Integer orden;

    @Schema(example = "1", description = "Identificador de la zona")
    private Long zonaId;

    @Schema(example = "1", description = "Identificador del domicilio")
    private Long domicilioId;

}
