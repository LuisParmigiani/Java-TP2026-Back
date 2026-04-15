package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VentaDTORequestPut {

    @Schema(example = "2024-06-01", description = "Fecha de la venta")
    @FutureOrPresent(message = "La fecha debe ser igual o posterior a la fecha actual")
    private Date fecha;
    @Min(value = 0, message = "El total debe ser mayor o igual a cero")
    @Schema(example = "150.75", description = "Total de la venta")
    private Double total;
    @Schema(example = "true", description = "Indica si la venta ha sido pagada")
    private Boolean pagado;
    @Schema(example = "1", description = "Identificador del domicilio de la persona asociado a esta venta")
    private Long idDomicilio;
    @Schema(example = "[1, 2, 3]", description = "Lista de identificadores de las líneas de pedido asociadas a esta venta")
    private List<Long> lineasPedidoIds;
    @Schema(example = "Pendiente", description = "Estado de la venta")
    @Pattern(regexp = "Pendiente|En proceso|Completada|Cancelada", message = "El estado debe ser uno de los siguientes: Pendiente, En proceso, Completada, Cancelada")
    private String estado;

}
