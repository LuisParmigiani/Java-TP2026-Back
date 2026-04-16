package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZonaDTOResponse {
    private Long id;
    private String nombre;
    private String detalle;
    private boolean[] dia;

}


