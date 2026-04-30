package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soda_roja.backend.model.DiaDomicilio;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomicilioDTORequest {
    @NotBlank(message = "La calle no puede estar vacía")
    @Schema(description = "La calle del domicilio", example = "Calle Falsa")
    private String calle;

    @NotBlank(message = "El número no puede estar vacío")
    @Pattern(regexp = "\\d{1,5}", message = "El número debe tener entre 1 y 5 dígitos")
    private String numero;

    @Pattern(regexp = "\\d{0,5}", message = "El número de casa debe tener entre 0 y 5 dígitos")
    @Schema(description = "El número de casa del domicilio (opcional)", example = "123")
    private String casa;

    @Schema(description = "El ID de la zona a la que pertenece el domicilio", example = "1")
    private Long zonaId;

    @Schema(description = "El ID de la persona asociada al domicilio", example = "1")
    private Long personaId;



    @Schema(description = "Indica si el domicilio está activo o no", example = "true")
    private String activo;


    @Schema(description = "Habilitado o no segun lo que pone el admin para que no pongan una direccion en un lugar que no se entrega. 0: Pendiente, 1: Habilitado, 2: Deshabilitado", example = "1")
    private String habilitado;// 0: Pendiente, 1: Habilitado, 2: Deshabilitado

    private List<DiaDomicilioDTORequest> diasDomicilio;

}
