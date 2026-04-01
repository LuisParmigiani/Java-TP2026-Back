package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El tipo de carga no puede estar vacío")
    @Pattern(regexp = "Carga|Descarga", message = "El tipo de carga debe ser Carga o Descarga")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @NotNull(message = "La fecha y hora no puede estar vacía")
    @Column(name = "fechaHora", nullable = false)
    private Date fechaHora;
}
