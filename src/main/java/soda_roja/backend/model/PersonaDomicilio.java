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
@Table(name = "personaDomicilio")
@Schema(description = "Entidad que representa una el hogar de una persona")
public class PersonaDomicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(example = "1", description = "Identificador único del domicilio de la persona")
    private Long id;
    @NotBlank(message = "El día no puede estar vacío")
    @Column(name = "dia", nullable = false)
    @Schema(example = "Lunes", description = "Día de la semana en el que la persona recibe el pedido, debe ser un día de la semana válido (Lunes, Martes, Miércoles, Jueves, Viernes, Sábado o Domingo)")
    @Pattern(regexp = "(Lunes|Martes|Miércoles|Jueves|Viernes|Sábado|Domingo)", message = "El día debe ser un día de la semana válido (Lunes, Martes, Miércoles, Jueves, Viernes, Sábado o Domingo)")
    private String dia;
}
