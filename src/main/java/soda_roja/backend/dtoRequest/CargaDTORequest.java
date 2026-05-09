package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargaDTORequest {

    @NotBlank(message = "El tipo de carga no puede estar vacío")
    @Pattern(regexp = "Carga|Descarga", message = "El tipo de carga debe ser Carga o Descarga")
    @Schema(example = "Carga", description = "Tipo de carga, puede ser Carga o Descarga")
    private String tipo;;
    @NotNull(message = "La fecha y hora no puede estar vacía")
    @Schema(example = "2024-06-01T14:30:00", description = "Fecha y hora de la carga o descarga en formato ISO 8601")
    private Date fechaHora;

    @NotNull(message = "El id del usuario no puede estar vacío")
    @Schema(example = "1", description = "ID del usuario asociado a la carga o descarga")
    private Long idUsuario;
    
    @NotNull(message = "El id del camion no puede estar vacío")
    @Schema(example = "1", description = "ID del camion asociado a la carga o descarga")
    private Long idCamion;
    @NotNull(message = "La lista de productos no puede estar vacía")
    private List<CargaProductoDTORequest> cargaProductos;
}
