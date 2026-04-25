package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PagoDTORequest {
    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 0, message = "El monto debe ser mayor o igual a 0")
    private float monto;
    @FutureOrPresent(message = "La fecha no puede ser en el pasado")
    @NotNull(message = "La fecha no puede ser nula")
    @Schema(description = "Fecha del pago en formato ISO 8601", example = "2029-06-01T14:30:00")
    private Date fecha;

    @NotBlank(message = "El método de pago no puede estar vacío")
    @Schema(description = "Método de pago utilizado para la transacción", example = "Tarjeta de crédito")
    private String metodoPago;

    @NotNull(message = "El id de la pesona no puede ser nulo")
    @Schema(description = "ID de la persona asociada al pago", example = "1")
    private Long personaId;

    @NotNull
    @Schema(description = "El estado del pago puede ser Pendiente, Aprobado, Rechazado", example = "Pendiente")
    @Pattern(regexp = "Pendiente|Aprobado|Rechazado", message = "El estado debe ser uno de los siguientes: Pendiente, Aprobado, Rechazado")
    private String estado;
}
