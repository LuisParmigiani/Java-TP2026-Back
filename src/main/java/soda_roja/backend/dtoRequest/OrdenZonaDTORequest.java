package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa la relación entre una orden y una zona")
public class OrdenZonaDTORequest {

    @NotNull(message = "El día no puede estar vacío")
    //El día se representa como un número del 0 al 6, donde 0 es lunes y 6 es domingo
    @Min(value = 0, message = "El día debe ser un número entre 0 y 6")
    @Max(value = 6, message = "El día debe ser un número entre 0 y 6")
    @Schema(example = "1", description = "Día de la semana")
    private Integer dia;

    @NotNull(message = "El orden no puede estar vacío")
    @Schema(example = "1", description = "Orden en el que se recorre el domicilio")
    private Integer orden;

    @NotNull(message = "El id de la zona no puede estar vacío")
    @Schema(example = "1", description = "Identificador de la zona")
    private Long zonaId;

    @NotNull(message = "El id del domicilio no puede estar vacío")
    @Schema(example = "1", description = "Identificador del domicilio")
    private Long domicilioId;

}
