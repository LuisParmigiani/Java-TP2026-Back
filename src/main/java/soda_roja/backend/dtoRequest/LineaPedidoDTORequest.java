package soda_roja.backend.dtoRequest;


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

public class LineaPedidoDTORequest {
	 @NotNull(message = "La cantidad no puede ser nula")
	 @Min(value = 1, message = "La cantidad debe ser al menos 1")
     @Schema(example = "5", description = "Cantidad de productos")
     private int cantidad;

    @NotNull(message = "El subtotal no puede ser nulo")
    @Min(value = 0, message = "El subtotal debe ser mayor o igual a 0")
    @Schema(example = "150.50", description = "Subtotal de la línea")
    private float subtotal;

    @NotNull(message = "ProductoZona no puede ser nulo")
    @Schema(example = "1", description = "ID de ProductoZona")
    private Long productoZonaId;
    

}
