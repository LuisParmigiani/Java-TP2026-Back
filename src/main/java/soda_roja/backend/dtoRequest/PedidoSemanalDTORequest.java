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
public class PedidoSemanalDTORequest
{
    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 0, message = "La cantidad debe ser al menos de 0")
    @Schema(example = "5", description = "Cantidad de productos")
    private Integer cantidad;

    @Min(value = 1, message = "El id debe ser mayor que 0")
    @Schema(example = "1", description = "Id del domicilio al que quiere hacer el pedido semanal")
    private Long domicilioId;

    @Schema(example = "1", description = "ID de ProductoZona")
    private Long productoZonaId;

    private Long productoId;

}
