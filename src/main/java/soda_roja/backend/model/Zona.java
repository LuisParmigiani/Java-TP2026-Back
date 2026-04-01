package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "zona")
@Schema(description = "Entidad que representa una zona")
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único de la zona")
    private long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false,unique = true)
    @Schema(example = "Fisherton", description = "Nombre de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;
    @Column(name = "detalle")
    @Schema(example = "Zona de Rosario", description = "Detalle de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{0,200}", message = "El detalle debe tener entre 0 y 200 caracteres")
    private String detalle;

}
