package soda_roja.backend.dtoRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Schema(description = "Entidad que representa el usuario de una persona")
public class UsuarioDTORequest {
    @Schema(example = "1", description = "Identificador único del producto")
    private Long id;
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,}$", message = "El nombre de usuario debe tener al menos 3 caracteres y solo puede contener letras, números y guiones bajos.")
    @Schema(example = "juan_perez", description = "Nombre de usuario único para iniciar sesión")
    private String nombreUsuario;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
    @Schema(example = "P@ssw0rd", description = "Contraseña segura para el usuario, debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
    private String contrasena;
    @NotBlank(message = "El nivel de acceso no puede estar vacío")
    @Pattern(regexp = "Administrador|Usuario|Empleado", message = "El nivel de acceso debe ser algunas de las opciones correctas: Administrador, Usuario o Empleado")
    @Schema(example = "Usuario", description = "Nivel de acceso del usuario, puede ser Administrador, Usuario o Empleado")
    private String nivelAcceso;
    @NotNull(message = "El id de la persona no puede estar vacío")
    @Schema(example = "1", description = "Identificador del usuario al que pertenece este domicilio")
    private Long personaId;
}
