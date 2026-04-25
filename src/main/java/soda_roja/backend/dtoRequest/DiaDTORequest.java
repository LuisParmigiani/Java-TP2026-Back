package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO de solicitud para Dia")
public class DiaDTORequest {
    @NotBlank(message = "El nombre es requerido")
    @Schema(description = "Nombre del día", example = "lunes")
    private String nombre;
}
