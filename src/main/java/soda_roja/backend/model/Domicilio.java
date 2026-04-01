package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
@Table(name = "domicilio")
@Schema(description = "Entidad que representa un domicilio del hogar")
public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(example = "1", description = "Identificador único del domicilio")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "La calle no puede estar vacía")
    @Column(name = "calle", nullable = false)
    @Schema(example = "Av jorge newbery", description = "Nombre de la calle del domicilio")
    private String calle;
    @NotBlank(message = "El número no puede estar vacío")
    @Pattern(regexp = "\\d{1,5}", message = "El número debe tener entre 1 y 5 dígitos. El numero solo puede contener numeros")
    @Schema(example = "1234", description = "Número de la calle del domicilio, debe contener solo números y tener entre 1 y 5 dígitos")
    @Column(name = "numero", nullable = false)
    private String numero;
    @Pattern(regexp = "\\d{0,5}", message = "El número de casa debe tener entre 0 y 5 dígitos. El numero solo puede contener numeros")
    @Schema(example = "12", description = "Número de la casa o del departamento dentro de un conjunto de hogares, debe contener solo números y tener entre 0 y 5 dígitos. Si el domicilio no tiene número de casa o departamento, se puede dejar vacío")
    @Column(name = "casa")
    private String casa;

}
