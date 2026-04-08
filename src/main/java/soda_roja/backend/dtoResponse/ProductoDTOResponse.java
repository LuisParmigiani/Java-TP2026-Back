package soda_roja.backend.dtoResponse;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;


@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTOResponse {

    @Schema(example = "1", description = "Identificador único")
    private Long id;

    @Schema(example = "Gaseosa Cola 2L", description = "Nombre del producto")
    private String nombre;

    @Schema(example = "Bebida gaseosa de cola de 2 litros", description = "Descripción del producto")
    private String detalle;

    @Schema(example = "2500.0", description = "Precio del producto")
    private double precio;

    @Schema(example = "20", description = "Cantidad disponible en stock")
    private int stock;
    
    
    
}
