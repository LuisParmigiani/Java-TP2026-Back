package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomicilioDTORequest {
    @NotBlank(message = "La calle no puede estar vacía")
    @Schema(description = "La calle del domicilio", example = "Calle Falsa")
    private String calle;

    @NotBlank(message = "El número no puede estar vacío")
    @Pattern(regexp = "\\d{1,5}", message = "El número debe tener entre 1 y 5 dígitos")
    private String numero;

    @Pattern(regexp = "\\d{0,5}", message = "El número de casa debe tener entre 0 y 5 dígitos")
    @Schema(description = "El número de casa del domicilio (opcional)", example = "123")
    private String casa;

    @NotNull(message = "El array de días no puede estar vacío")
    @Size(min = 7, max = 7, message = "El array de días debe tener exactamente 7 elementos")
    @Schema(example = "[true, false, true, false, true, false, true]", description = "Array de 7 elementos que indica si la persona recibe el pedido en cada día de la semana")
    private boolean[] dia;

    @NotNull(message = "El id de la zona no puede estar vacío")
    @Schema(description = "El ID de la zona a la que pertenece el domicilio", example = "1")
    private Long zonaId;

    @NotNull(message = "El id de la persona no puede estar vacío")
    @Schema(description = "El ID de la persona asociada al domicilio", example = "1")
    private Long personaId;

    @Schema(description = "El ID del camión asignado al domicilio (opcional)", example = "1")
    private Long camionId;


    @Schema(description = "Indica si el domicilio está activo o no", example = "true")
    @NotNull(message = "El estado de actividad del domicilio no puede estar vacío")
    private Boolean activo;


}
