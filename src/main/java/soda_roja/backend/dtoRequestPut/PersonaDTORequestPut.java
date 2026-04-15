package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa una persona")
public class PersonaDTORequestPut {

    @Pattern(regexp = "DNI|Pasaporte|Cédula", message = "El tipo de documento debe ser DNI, Pasaporte o Cédula")
    @Schema(examples = "DNI")
    private String tipoDoc;
    @Pattern(regexp = "\\d{7,10}", message = "El número de documento debe tener entre 7 y 10 dígitos")
    @Schema(examples = "12345678")
    private String nroDocumento;
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(example = "Juan")
    private String nombre;
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El apellido debe tener entre 3 y 50 caracteres")
    @Schema(example = "Pérez")

    private String apellido;
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "El email debe ser válido")
    @Schema(example = "juan.perez@gmail.com")

    private String email;
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    @Schema(example = "1123456789")

    private String telefono;
    @Schema(example = "0.0" , description = "El saldo de la persona, que se actualiza con cada pago realizado. No puede ser negativo.")
    private Float saldo;





}
