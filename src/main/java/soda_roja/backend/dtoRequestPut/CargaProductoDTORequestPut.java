package soda_roja.backend.dtoRequestPut;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class CargaProductoDTORequestPut {
    
	@Min(value = 0, message = "La cantidad de llenos no puede ser negativa")
	@Schema(example = "10", description = "Cantidad de productos llenos")
	private int cantLleno;
    
	@Min(value = 0, message = "La cantidad de vacíos no puede ser negativa")
	@Schema(example = "5", description = "Cantidad de productos vacíos")
    private int cantVacio;
    
	@Min(value = 1, message = "El id de la carga debe ser mayor a 0")
	@Schema(example = "1", description = "ID de la carga a la que pertenece")
    private Long idCarga;
    
	@Min(value = 1, message = "El id del producto debe ser mayor a 0")
	@Schema(example = "1", description = "ID del producto que se está cargando")
    private Long idProducto;
}
