package soda_roja.backend.dtoRequestPut;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ZonaDTORequestPut {

    @Schema(example = "Fisherton", description = "Nombre de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;
    @Schema(example = "Zona de Rosario", description = "Detalle de la zona")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ,\\.\\-\\s]{0,200}$", message = "El detalle debe tener entre 0 y 200 caracteres")
    private String detalle;
    @Schema(example = "1", description = "Identificador del camion al que pertenece la zona")
    private Long camionId;
    @Schema(description = "Lista de días asociados a la zona")
    private List<DiaZonaDTORequestPut> diasZona; 
    
}
