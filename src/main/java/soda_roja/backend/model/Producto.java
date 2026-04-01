package soda_roja.backend.model;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data // Genera getters, setters, toString, equals y hashCode
@AllArgsConstructor // Genera un constructor con todos los campos
@NoArgsConstructor// Genera un constructor sin argumentos
@Builder // Genera un builder para la clase

@Entity                  // 
@Table(name = "producto") // ← opcional pero recomendado
@Schema(name = "Producto", description = "Representa un producto disponible en el sistema")
public class Producto { //Estos mensajes te tiran la bronca a nivel de app cuando viene mal la data
	//Los 3 primeros si afectan a la base de datos, los demás que no sean column no afectan a la base de datos.
	
	@Id // Marca este campo como la clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el valor automáticamente
	@Column(name = "id", unique=true, nullable=false) // Especifica el nombre de la columna en la base de datos
	@Schema(example = "1", description = "Identificador único del producto")
	private int id;
	
	@NotBlank(message = "El nombre no puede estar vacío") // Valida que el campo no esté vacío
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres") // Valida el tamaño máximo del campo
	@Column(unique=true, length=100) // Solo hace falta column si le queremos poner propiedades o nombres distintos.
	@Schema(example = "Gaseosa Cola 2L", description = "Nombre del producto")
	private String nombre;


	@Size(min=10, max = 250, message = "El detalle no puede tener menos de 10 caracteres ni más de 250 caracteres") // Valida el tamaño del campo
	@NotBlank(message = "El detalle no puede estar vacío")
	@Column(length = 250, nullable= false) // Especifica el tamaño máximo de la columna en la base de datos
	@Schema(example = "Bebida gaseosa de cola de 2 litros", description = "Descripción del producto")
	private String detalle;
	
	@NotNull(message = "El precio no puede ser nulo") // Valida que el campo no sea nulo
	@Min(value = 0, message = "El precio no puede ser negativo") // Valida que el valor sea mayor o igual a 0
	@Column(nullable = false) // Especifica que la columna no puede ser nula en la base de datos
	@Schema(example = "2500.0", description = "Precio del producto")
	private double precio;
	
	@NotNull(message = "El stock no puede ser nulo") // Valida que el campo no sea nulo
	@Min(value = 0, message = "El stock no puede ser negativo") // Valida que el valor sea mayor o igual a 0
	@Column(nullable = false) // Especifica que la columna no puede ser nula en la base de datos
	@Schema(example = "20", description = "Cantidad disponible en stock")
	private int stock;
	

}
