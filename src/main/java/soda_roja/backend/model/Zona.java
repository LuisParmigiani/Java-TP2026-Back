package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "zona")
@Schema(description = "Entidad que representa una zona")
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único de la zona")
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false,unique = true)
    @Schema(example = "Fisherton", description = "Nombre de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{3,50}", message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;
    @Column(name = "detalle")
    @Schema(example = "Zona de Rosario", description = "Detalle de la zona")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{0,200}", message = "El detalle debe tener entre 0 y 200 caracteres")
    private String detalle;

    @ManyToMany
    @JoinTable(
        name = "zonaProducto", // Nombre de la tabla intermedia
        joinColumns = @JoinColumn(name = "zonaId"), // Clave foránea que referencia a Zona
        inverseJoinColumns = @JoinColumn(name = "productoId") )// Clave foránea que referencia a Producto
    @Schema(example = "[{\"id\": 1, \"nombre\": \"Producto A\", \"detalle\": \"Detalle del producto A\", \"precio\": 19.99, \"stock\": 100}]", description = "Lista de productos asociados a la zona")
    private List<Producto> producto;

    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    // cascade: para que todo lo que se haga en zona quede aplicado en el domicilio, por ejemplo si borro zona se borran todos los domicilios relacionados
    // orphanRemoval: Es para el manejo de entidades huerfanas es decir si uno borra un domicilio de la lista de zona se elimina el domicilio de la base de datos.
    @Schema(example = "[{\"id\": 1, \"calle\": \"Calle Falsa\", \"numero\": \"123\", \"ciudad\": \"Rosario\"}]", description = "Lista de domicilios asociados a la zona")
    @JsonManagedReference
    private List<Domicilio> domicilio;
}
