package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class PersonaDomicilioDTORequest {
    @NotBlank(message = "El día no puede estar vacío")
    @Schema(example = "Lunes", description = "Día de la semana en el que la persona recibe el pedido, debe ser un día de la semana válido (Lunes, Martes, Miércoles, Jueves, Viernes, Sábado o Domingo)")
    @Pattern(regexp = "(Lunes|Martes|Miércoles|Jueves|Viernes|Sábado|Domingo)", message = "El día debe ser un día de la semana válido (Lunes, Martes, Miércoles, Jueves, Viernes, Sábado o Domingo)")
    private String dia;

    @NotNull(message = "El id de la persona no puede estar vacío")
    @Schema(example = "1", description = "ID de la persona a la que se le asignará el domicilio, debe ser un número entero positivo")
    private Long personaId;

    @NotNull(message = "El id del domicilio no puede estar vacío")
    @Schema(example = "1", description = "ID del domicilio que se asignará a la persona, debe ser un número entero positivo")
    private Long domicilioId;;
}
