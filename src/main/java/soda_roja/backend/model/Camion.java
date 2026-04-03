package soda_roja.backend.model;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(example = "1", description = "Identificador único del camion")
    private Long id;
    
	@Column(unique=true, length=7) // Solo hace falta column si le queremos poner propiedades o nombres distintos.
	
    private String patente;
	
	@Column(length = 250, nullable= false) // Especifica el tamaño máximo de la columna en la base de datos
    private String modelo;
	@Column(length = 250, nullable= false) // Especifica el tamaño máximo de la columna en la base de datos
	
	private String marca;
	
	@Column(nullable = false) // Especifica que la columna no puede ser nula en la base de datos
	private int kilometraje;
	
	
    @OneToMany(mappedBy = "camion", cascade = CascadeType.ALL, orphanRemoval = true,fetch =  FetchType.EAGER)
    // orphanRemoval: Es para el manejo de entidades huerfanas es decir si uno borra un domicilio de la lista de zona se elimina el domicilio de la base de datos.
    @JsonManagedReference

    private List<Gasto> gastos;

}
