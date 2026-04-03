package soda_roja.backend.model;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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
	private Long id;
	
	
	@Column(unique=true, length=100) // Solo hace falta column si le queremos poner propiedades o nombres distintos.
	private String nombre;


	
	@Column(length = 250, nullable= false) // Especifica el tamaño máximo de la columna en la base de datos
	private String detalle;
	
	
	@Column(nullable = false) // Especifica que la columna no puede ser nula en la base de datos
	private double precio;
	
	
	@Column(nullable = false) // Especifica que la columna no puede ser nula en la base de datos
	private int stock;

	@ManyToMany(mappedBy = "productos", fetch = FetchType.LAZY)//fetch.LAZY se utiliza para cargar los productos asociados a la zona solo cuando se soliciten, evitando cargar toda la información de los productos al obtener una zona solo se carga al poner zona.getProducto()
	private List<Zona> zonas;


}
