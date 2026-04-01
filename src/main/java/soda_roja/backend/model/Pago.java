package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "persona")
@Schema(description = "Entidad que representa una persona")

public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único del pago")
    private Long id;
    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 0, message = "El monto debe ser mayor o igual a 0")
    @Column(name = "monto", nullable = false)
    private float monto;
    @NotNull(message = "La fecha no puede ser nula")
    @Column(name = "fecha", nullable = false)
    @FutureOrPresent(message = "La fecha no puede ser en el pasado")
    private Date fecha;
    @NotBlank(message = "El método de pago no puede estar vacío")
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
}
