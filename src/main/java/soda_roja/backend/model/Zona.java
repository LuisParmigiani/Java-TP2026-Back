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
    private Long id;
    @Column(name = "nombre", nullable = false,unique = true)
    private String nombre;
    @Column(name = "detalle")
    private String detalle;

    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductoZona> productosZona;


    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true,fetch =  FetchType.LAZY)
    // cascade: para que todo lo que se haga en zona quede aplicado en el domicilio, por ejemplo si borro zona se borran todos los domicilios relacionados
    // orphanRemoval: Es para el manejo de entidades huerfanas es decir si uno borra un domicilio de la lista de zona se elimina el domicilio de la base de datos.
    @JsonManagedReference

    private List<Domicilio> domicilio;
}
