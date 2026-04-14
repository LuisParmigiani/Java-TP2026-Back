package soda_roja.backend.dtoRequestPut;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CamionDTORequestPut {
	

	
	@Schema(example = "AA010GH", description = "Patente del camion")
	@Size(max = 7, message = "La patente no puede tener más de 7 caracteres ") // Valida el tamaño máximo del campo
	@Pattern(regexp = "^([A-Z]{3}\\s?\\d{3}|[A-Z]{2}\\s?\\d{3}\\s?[A-Z]{2})$", message = "La patente debe tener formato argentino: AAA 000 o AA 000 AA")
    private String patente;
	
	@Size(min=10, max = 250, message = "El modelo no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@Schema(example = "Sprinter Chasis", description = "Modelo del camion")
    private String modelo;
	
	@Size(min=10, max = 250, message = "La marca no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@Schema(example = "Mercedez Benz", description = "Marca del camion")
	private String marca;
	
	@Min(value = 0, message = "El kilometraje no puede ser negativo") // Valida que el valor sea mayor o igual a 0
	@Schema(example = "20", description = "Kilometraje del camión")
	private Integer kilometraje;



}
