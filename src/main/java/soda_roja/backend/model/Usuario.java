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
@Table(name = "usuario")
@Schema(description = "Entidad que representa el usuario de una persona")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(example = "1", description = "Identificador único del producto")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,)}$", message = "El nombre de usuario debe tener entre 3 y 20 caracteres y solo puede contener letras, números y guiones bajos")
    @Column(name = "nombreUsuario", unique = true, nullable = false)
    @Schema(example = "juan_perez", description = "Nombre de usuario único para iniciar sesión")
    private String nombreUsuario;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "La contraseña debe tener al menos 8 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial")
    @Column(name = "contrasena", nullable = false)
    @Schema(example = "P@ssw0rd", description = "Contraseña para iniciar sesión, debe cumplir con los requisitos de seguridad")
    private String contrasena;
    @NotBlank(message = "El nivel de acceso no puede estar vacío")
    @Pattern(regexp = "Administrador|Usuario|Empleado", message = "El nivel de acceso debe ser algunas de las opciones correctas: Administrador, Usuario o Empleado")
    @Column(name = "nivelAcceso", nullable = false)
    @Schema(example = "Usuario", description = "Nivel de acceso del usuario, puede ser Administrador, Usuario o Empleado")
    private String nivelAcceso;
}
