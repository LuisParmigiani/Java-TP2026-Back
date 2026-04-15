package soda_roja.backend.dtoRequestPut;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LineaPedidoDTORequestPut {
	 @Min(value = 1, message = "La cantidad debe ser al menos 1")
     @Schema(example = "5", description = "Cantidad de productos")
     private Integer cantidad;

    @Min(value = 0, message = "El subtotal debe ser mayor o igual a 0")
    @Schema(example = "150.50", description = "Subtotal de la línea")
    private Float subtotal;

    @Schema(example = "1", description = "ID de ProductoZona")
    private Long productoZonaId;
    

}
