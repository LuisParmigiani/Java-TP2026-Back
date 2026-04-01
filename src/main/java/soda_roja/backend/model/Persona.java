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
@Table(name = "persona")
@Schema(description = "Entidad que representa una persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(examples = "1", description = "Identificador único del producto")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El tipo de documento no puede estar vacío")
    @Pattern(regexp = "DNI|Pasaporte|Cédula", message = "El tipo de documento debe ser DNI, Pasaporte o Cédula")
    @Column(name = "tipoDoc", nullable = false)
    @Schema(examples = "DNI")
    private String tipoDoc;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Pattern(regexp = "\\d{7,10}", message = "El número de documento debe tener entre 7 y 10 dígitos")
    @Column(name = "nroDocumento", unique = true, nullable = false)
    @Schema(examples = "12345678")
    private String nroDocumento;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    @Column(name = "nombre", nullable = false)
    @Schema(example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El apellido debe tener entre 3 y 50 caracteres")
    @Column(name = "apellido", nullable = false)
    @Schema(example = "Pérez")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "El email debe ser válido")
    @Column(name = "email", unique = true, nullable = false)
    @Schema(example = "juan.perez@gmail.com")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    @Column(name = "telefono", nullable = false, unique = true)
    @Schema(example = "1123456789")
    private String telefono;

    @Min(value = 0, message = "La deuda no puede ser negativa")
    @Column(name = "deuda", nullable = true)
    @Schema(example = "0.0")
    private float deuda;
}