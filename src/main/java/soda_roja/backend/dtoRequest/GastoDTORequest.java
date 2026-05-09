package soda_roja.backend.dtoRequest;

import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GastoDTORequest {
	
	@NotBlank(message = "El detalle no puede estar vacío") // Valida que el campo no esté vacío
	@Size(min=0, max = 250, message = "El detalle no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@Schema(example = "Cambio de aceite", description = "Detalle del gasto")
	 private String detalle;
    @NotNull(message = "El monto no puede ser nulo") // Valida que el campo no sea nulo
    @Positive(message = "El monto debe ser un número positivo") // Valida que el
    @Schema(example = "5000.00", description = "Monto del gasto")
	 private double monto;
    @NotBlank(message = "La fecha no puede estar vacía") // Valida que el campo no esté vacío
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d"
    		+ "{2}$", message = "La fecha debe tener el formato YYYY-MM-DD") // Valida el formato de la fecha
    @Schema(example = "2024-06-15", description = "Fecha del gasto")
	 private String fecha;
	@Schema(example = "1", description = "Identificador del camión asociado al gasto")
	//Si no tiene camion_id, se asocia a null, es decir, el gasto no está asociado a ningún camión.
	 private Long camion_id;

}
