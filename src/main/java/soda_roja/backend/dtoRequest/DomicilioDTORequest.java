package soda_roja.backend.dtoRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomicilioDTORequest {

    @NotBlank(message = "La calle no puede estar vacía")
    private String calle;

    @NotBlank(message = "El número no puede estar vacío")
    @Pattern(regexp = "\\d{1,5}", message = "El número debe tener entre 1 y 5 dígitos")
    private String numero;

    @Pattern(regexp = "\\d{0,5}", message = "El número de casa debe tener entre 0 y 5 dígitos")
    private String casa;

    @NotNull(message = "El id de la zona no puede estar vacío")
    private Long zonaId;
}
