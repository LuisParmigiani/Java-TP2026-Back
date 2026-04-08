package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTORequest {
	
	@NotBlank(message = "El email es requerido")
	@Email(message = "El email debe ser válido")
	@Schema(description = "Correo electrónico del usuario", example = "pepito@gmail.com")
	private String email;
	
	@NotBlank(message = "La contraseña es requerida")
	@Schema(description = "Contraseña del usuario", example = "password123")
	private String contrasena;
}
