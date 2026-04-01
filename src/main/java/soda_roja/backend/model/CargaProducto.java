package soda_roja.backend.model;

import java.util.Date;
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
@Table(name = "cargaProducto")
@Schema(description = "Entidad que representa una carga y descarga de los productos del camion")
public class CargaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único de la carga de producto")
    private Long id;

    @Min(value = 0, message = "La cantidad de llenos no puede ser negativa")
    @Column(name = "cant_lleno", nullable = false)
    @Schema(example = "10", description = "Cantidad de productos llenos")
    private int cantLleno;

    @Min(value = 0, message = "La cantidad de vacíos no puede ser negativa")
    @Column(name = "cant_vacio", nullable = false)
    @Schema(example = "5", description = "Cantidad de productos vacíos")
    private int cantVacio;
}
