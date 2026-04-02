package soda_roja.backend.model;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data // Genera getters, setters, toString, equals y hashCode
@AllArgsConstructor // Genera un constructor con todos los campos
@NoArgsConstructor// Genera un constructor sin argumentos
@Builder // Genera un builder para la clase

@Entity                  // 
@Table(name = "camion") // ← opcional pero recomendado
@Schema(name = "Camion", description = "Representa un camion disponible en el sistema")

public class Camion {
	
	@Id // Marca este campo como la clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente
	@Column(name = "id", unique=true, nullable=false) // Especifica el nombre de la columna en la base de datos
	@Schema(example = "1", description = "Identificador único del camion")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
	@NotBlank(message = "La patente no puede estar vacía") // Valida que el campo no esté vacío
	@Size(max = 7, message = "La patente no puede tener más de 7 caracteres ") // Valida el tamaño máximo del campo
	@Column(unique=true, length=7) // Solo hace falta column si le queremos poner propiedades o nombres distintos.
	@Schema(example = "AA010GH", description = "Patente del camion")
	@Pattern(regexp = "^([A-Z]{3}\\s?\\d{3}|[A-Z]{2}\\s?\\d{3}\\s?[A-Z]{2})$", message = "La patente debe tener formato argentino: AAA 000 o AA 000 AA")
    private String patente;
	
	@Size(min=10, max = 250, message = "El modelo no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@NotBlank(message = "El modelo no puede estar vacío")
	@Column(length = 250, nullable= false) // Especifica el tamaño máximo de la columna en la base de datos
	@Schema(example = "Sprinter Chasis", description = "Modelo del camion")
    private String modelo;
	@Size(min=10, max = 250, message = "La marca no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@NotBlank(message = "La marca no puede estar vacío")
	@Column(length = 250, nullable= false) // Especifica el tamaño máximo de la columna en la base de datos
	@Schema(example = "Mercedez Benz", description = "Marca del camion")
	private String marca;
	
	@NotNull(message = "El kilometraje no puede ser nulo") // Valida que el campo no sea nulo
	@Min(value = 0, message = "El kilometraje no puede ser negativo") // Valida que el valor sea mayor o igual a 0
	@Column(nullable = false) // Especifica que la columna no puede ser nula en la base de datos
	@Schema(example = "20", description = "Kilometraje del camión")
	private int kilometraje;

}
