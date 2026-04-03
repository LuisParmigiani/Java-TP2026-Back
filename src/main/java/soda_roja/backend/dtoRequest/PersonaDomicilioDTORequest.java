package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonaDomicilioDTORequest {
    @NotNull(message = "El array de días no puede estar vacío")
    @Size(min = 7, max = 7, message = "El array de días debe tener exactamente 7 elementos")
    @Schema(example = "[true, false, true, false, true, false, true]", description = "Array de 7 elementos que indica si la persona recibe el pedido en cada día de la semana (true para activo, false para inactivo)")
    private boolean[] dia;

    @NotNull(message = "El id de la persona no puede estar vacío")
    @Schema(example = "1", description = "ID de la persona a la que se le asignará el domicilio, debe ser un número entero positivo")
    private Long personaId;

    @NotNull(message = "El id del domicilio no puede estar vacío")
    @Schema(example = "1", description = "ID del domicilio que se asignará a la persona, debe ser un número entero positivo")
    private Long domicilioId;


}
