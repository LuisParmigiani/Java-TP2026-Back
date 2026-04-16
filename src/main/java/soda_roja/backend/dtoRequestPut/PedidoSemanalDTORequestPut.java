package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoSemanalDTORequestPut {
    @Min(value = 0, message = "La cantidad debe ser al menos de 0")
    @Schema(example = "5", description = "Cantidad de productos")
    private int cantidad;

    @Min(value = 1, message = "El id debe ser mayor que 0")
    @Schema(example = "1", description = "Id del domicilio al que quiere hacer el pedido semanal")
    private Long domicilioId;

    @Schema(example = "1", description = "ID de ProductoZona")
    private Long productoZonaId;

}
