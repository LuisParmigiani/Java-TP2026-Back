package soda_roja.backend.dtoRequestPut;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Schema(description = "Entidad que representa el usuario de una persona")
public class UsuarioDTORequestPut {
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,}$", message = "El nombre de usuario debe tener al menos 3 caracteres y solo puede contener letras, números y guiones bajos.")
    @Schema(example = "juan_perez", description = "Nombre de usuario único para iniciar sesión")
    private String nombreUsuario;
    @Email(message = "El mail debe ser válido")
    @Schema(example = "juan_perez@gmail.com", description = "Correo electrónico del usuario, debe ser único y válido")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
    @Schema(example = "P@ssw0rd", description = "Contraseña segura para el usuario, debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
    private String contrasena;
    @Pattern(regexp = "Administrador|Usuario|Empleado", message = "El nivel de acceso debe ser algunas de las opciones correctas: Administrador, Usuario o Empleado")
    @Schema(example = "Usuario", description = "Nivel de acceso del usuario, puede ser Administrador, Usuario o Empleado")
    private String nivelAcceso;
    @Schema(example = "1", description = "Identificador del usuario al que pertenece este domicilio")
    private Long personaId;

    private PersonaDTORequestPut persona;
}
