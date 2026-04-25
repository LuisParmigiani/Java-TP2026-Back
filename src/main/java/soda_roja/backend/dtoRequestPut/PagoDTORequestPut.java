package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PagoDTORequestPut {
    @Min(value = 0, message = "El monto debe ser mayor o igual a 0")
    private Float monto;
    @FutureOrPresent(message = "La fecha no puede ser en el pasado")
    @Schema(description = "Fecha del pago en formato ISO 8601", example = "2029-06-01T14:30:00")
    private Date fecha;

    @Schema(description = "Método de pago utilizado para la transacción", example = "Tarjeta de crédito")
    private String metodoPago;

    @Schema(description = "ID de la persona asociada al pago", example = "1")
    private Long personaId;
    @Schema(description = "El estado del pago puede ser Pendiente, Aprobado, Rechazado", example = "Pendiente")

    @Pattern(regexp = "Pendiente|Aprobado|Rechazado", message = "El estado debe ser uno de los siguientes: Pendiente, Aprobado, Rechazado")
    private String estado;
}
