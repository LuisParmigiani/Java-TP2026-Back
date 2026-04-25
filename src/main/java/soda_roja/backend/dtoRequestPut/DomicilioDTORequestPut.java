package soda_roja.backend.dtoRequestPut;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soda_roja.backend.dtoRequest.DiaDomicilioDTORequest;

import javax.swing.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomicilioDTORequestPut {
    @Schema(description = "La calle del domicilio", example = "Calle Falsa")
    private String calle;

    @Pattern(regexp = "\\d{1,5}", message = "El número debe tener entre 1 y 5 dígitos")
    private String numero;

    @Schema(description = "El número de casa del domicilio (opcional)", example = "123")
    private String casa;

    @Size(min = 7, max = 7, message = "El array de días debe tener exactamente 7 elementos")
    @Schema(example = "[true, false, true, false, true, false, true]", description = "Array de 7 elementos que indica si la persona recibe el pedido en cada día de la semana")
    private Integer[] dia;

    @Schema(description = "El ID de la zona a la que pertenece el domicilio", example = "1")
    private Long zonaId;

    @Schema(description = "El ID de la persona asociada al domicilio", example = "1")
    private Long personaId;



    @Schema(description = "Indica si el domicilio está activo o no", example = "true")
    private Boolean activo;

    @Schema(description = "Habilitado o no segun lo que pone el admin para que no pongan una direccion en un lugar que no se entrega. 0: Pendiente, 1: Habilitado, 2: Deshabilitado", example = "1")
    private Integer habilitado;

    private List<DiaDomicilioDTORequest> diasDomicilio;
}
