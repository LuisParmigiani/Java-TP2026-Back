package soda_roja.backend.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "carga")
@Schema(description = "Entidad que representa una carga y descarga de mercadería")

public class Carga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(example = "1", description = "Identificador único del producto")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El tipo de carga no puede estar vacío")
    @Pattern(regexp = "Carga|Descarga", message = "El tipo de carga debe ser Carga o Descarga")
    @Column(name = "tipo", nullable = false)
    @Schema(example = "Carga", description = "Tipo de carga, puede ser Carga o Descarga")
    private String tipo;;
    @NotNull(message = "La fecha y hora no puede estar vacía")
    @Column(name = "fechaHora", nullable = false)
    @Schema(example = "2024-06-01T14:30:00", description = "Fecha y hora de la carga o descarga en formato ISO 8601")
    private Date fechaHora;
}
