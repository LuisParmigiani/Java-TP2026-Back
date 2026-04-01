package soda_roja.backend.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "venta")
@Schema(description = "Entidad que representa una venta")
public class Venta {
    @Id
    @Column(name="id",unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único de la venta")
    private long id;
    @NotNull(message = "La fecha no puede ser nula")
    @Column(name = "fecha", nullable = false)
    @Schema(example = "2024-06-01", description = "Fecha de la venta")
    @FutureOrPresent(message = "La fecha debe ser igual o posterior a la fecha actual")
    private Date fecha;
    @Min(value = 0, message = "El total debe ser mayor o igual a cero")
    @NotBlank(message = "El total no puede estar vacío")
    @Schema(example = "150.75", description = "Total de la venta")
    @Column(name = "total", nullable = false)
    private double total;
    @Schema(example = "true", description = "Indica si la venta ha sido pagada")
    @NotNull(message = "El estado de pago no puede ser nulo")
    @Column(name = "pagado", nullable = false)
    private boolean pagado;
}
