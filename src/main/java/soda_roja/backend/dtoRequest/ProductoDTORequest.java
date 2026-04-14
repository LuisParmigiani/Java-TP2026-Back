package soda_roja.backend.dtoRequest;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTORequest {
	
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
	@Schema(example = "Gaseosa Cola 2L", description = "Nombre del producto")
	private String nombre;
	
	@Size(min = 10, max = 250, message = "El detalle no puede tener menos de 10 caracteres ni más de 250 caracteres")
	@NotBlank(message = "El detalle no puede estar vacío")
	@Schema(example = "Bebida gaseosa de cola de 2 litros", description = "Descripción del producto")
	private String detalle;
	
	@NotNull(message = "El precio no puede ser nulo")
	@Min(value = 0, message = "El precio no puede ser negativo")
	@Schema(example = "2500.0", description = "Precio del producto")
	private Double precio;
	
	@NotNull(message = "El stock no puede ser nulo")
	@Min(value = 0, message = "El stock no puede ser negativo")
	@Schema(example = "20", description = "Cantidad disponible en stock")
	private Integer stock;
	
	@Size(max = 500, message = "La URL de la imagen no puede tener más de 250 caracteres")
	@Schema(example = "https://example.com/images/gaseosa-cola-2l"
			+ "jpg", description = "URL de la imagen del producto")
	private String imagenUrl;
	
	@Schema(example = "true", description = "Indica si el producto está activo o inactivo")
	private boolean activo; // Indica si el producto está activo o inactivo
	
	
}
