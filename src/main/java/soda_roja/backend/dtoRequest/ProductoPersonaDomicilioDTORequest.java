package soda_roja.backend.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO para crear o actualizar relaciones ProductoPersonaDomicilio")
public class ProductoPersonaDomicilioDTORequest {
    @NotNull(message = "El ID del producto no puede ser nulo")
    @Positive(message = "El ID del producto debe ser positivo")
    @Schema(description = "ID del producto", example = "1")
    private Long productoId;
    
    @NotNull(message = "El ID de persona domicilio no puede ser nulo")
    @Positive(message = "El ID de persona domicilio debe ser positivo")
    @Schema(description = "ID de la persona domicilio", example = "1")
    private Long personaDomicilioId;
    
    @NotNull(message = "La cantidad de vacíos actuales no puede ser nula")
    @Positive(message = "La cantidad de vacíos actuales debe ser positiva")
    @Schema(description = "Cantidad de vacíos actuales", example = "5")
    private Integer cantVaciosActuales;
    
    @NotNull(message = "La aproximación semanal no puede ser nula")
    @Positive(message = "La aproximación semanal debe ser positiva")
    @Schema(description = "Aproximación semanal de consumo de ese producto", example = "2")
    private Integer aproxSemanal;
}
