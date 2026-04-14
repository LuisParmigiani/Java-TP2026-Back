package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa un login")

public class RegisterDTORequestPut {
	
    @Pattern(regexp = "DNI|Pasaporte|Cédula", message = "El tipo de documento debe ser DNI, Pasaporte o Cédula")
    @Schema(examples = "DNI")
	private String persona_tipoDoc;
	
    @Pattern(regexp = "\\d{7,10}", message = "El número de documento debe tener entre 7 y 10 dígitos")
    @Schema(examples = "12345678")
	private String persona_nroDoc;
	
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(example = "Juan")
	private String persona_nombre;
	
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El apellido debe tener entre 3 y 50 caracteres")
    @Schema(example = "Pérez")
	private String persona_apellido;
	
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    @Schema(example = "1123456789")
	private String persona_telefono;
    
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "El email debe ser válido")
    @Schema(example = "juan.perez@gmail.com")
	private String email;
	
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
    @Schema(example = "P@ssw0rd", description = "Contraseña segura para el usuario, debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
	private String usuario_contrasena;
	
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,}$", message = "El nombre de usuario debe tener al menos 3 caracteres y solo puede contener letras, números y guiones bajos.")
    @Schema(example = "juan_perez", description = "Nombre de usuario único para iniciar sesión")
	private String usuario_nombre;
	
    @Pattern(regexp = "Administrador|Usuario|Empleado", message = "El nivel de acceso debe ser algunas de las opciones correctas: Administrador, Usuario o Empleado")
    @Schema(example = "Usuario", description = "Nivel de acceso del usuario, puede ser Administrador, Usuario o Empleado")
	private String usuario_nivelAcceso;

	
}
