package soda_roja.backend.dtoRequest;

import lombok.*;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoDTORequest {
	
	
	@NotBlank(message = "El nombre no puede estar vacío") // Valida que el campo no esté vacío
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres") // Valida el tamaño máximo del campo
	@Schema(example = "Gaseosa Cola 2L", description = "Nombre del producto")
	private String nombre;
	
	@Size(min=10, max = 250, message = "El detalle no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@NotBlank(message = "El detalle no puede estar vacío")
	@Schema(example = "Bebida gaseosa de cola de 2 litros", description = "Descripción del producto")
	private String detalle;
	
	@NotNull(message = "El precio no puede ser nulo") // Valida que el campo no sea nulo
	@Min(value = 0, message = "El precio no puede ser negativo") // Valida que el valor sea mayor o igual a 0
	@Schema(example = "2500.0", description = "Precio del producto")
	private double precio;
	
	@NotNull(message = "El stock no puede ser nulo") // Valida que el campo no sea nulo
	@Min(value = 0, message = "El stock no puede ser negativo") // Valida que el valor sea mayor o igual a 0
	@Schema(example = "20", description = "Cantidad disponible en stock")
	private int stock;
	
	@Schema(example = "[1]", description = "Lista de zonas asociadas al producto")
	private List<Long> zonasId;

	

}
