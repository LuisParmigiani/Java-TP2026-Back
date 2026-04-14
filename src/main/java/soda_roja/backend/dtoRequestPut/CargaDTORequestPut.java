package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargaDTORequestPut {

    @Pattern(regexp = "Carga|Descarga", message = "El tipo de carga debe ser Carga o Descarga")
    @Schema(example = "Carga", description = "Tipo de carga, puede ser Carga o Descarga")
    private String tipo;;
    @Schema(example = "2024-06-01T14:30:00", description = "Fecha y hora de la carga o descarga en formato ISO 8601")
    private Date fechaHora;

    @Schema(example = "1", description = "ID del usuario asociado a la carga o descarga")
    private Long idUsuario;
    
    @Schema(example = "1", description = "ID del camion asociado a la carga o descarga")
    private Long idCamion;
}
