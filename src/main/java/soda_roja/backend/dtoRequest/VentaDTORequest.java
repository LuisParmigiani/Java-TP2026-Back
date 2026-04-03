package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VentaDTORequest {

    @NotNull(message = "La fecha no puede ser nula")
    @Schema(example = "2024-06-01", description = "Fecha de la venta")
    @FutureOrPresent(message = "La fecha debe ser igual o posterior a la fecha actual")
    private Date fecha;
    @Min(value = 0, message = "El total debe ser mayor o igual a cero")
    @NotNull(message = "El total no puede estar vacío")
    @Schema(example = "150.75", description = "Total de la venta")
    private double total;
    @Schema(example = "true", description = "Indica si la venta ha sido pagada")
    @NotNull(message = "El estado de pago no puede ser nulo")
    private boolean pagado;
    @JoinColumn(name = "idPersonaDomicilio", nullable = false)
    @Schema(example = "1", description = "Identificador del domicilio de la persona asociado a esta venta")
    private Long idPersonaDomicilio;
}
