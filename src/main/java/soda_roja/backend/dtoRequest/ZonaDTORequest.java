package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ZonaDTORequest {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(example = "Fisherton", description = "Nombre de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;
    @Schema(example = "Zona de Rosario", description = "Detalle de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{0,200}", message = "El detalle debe tener entre 0 y 200 caracteres")
    private String detalle;

}
