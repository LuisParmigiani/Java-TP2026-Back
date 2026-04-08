package soda_roja.backend.dtoResponse;

import lombok.*;
import soda_roja.backend.model.Zona;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class DomicilioDTOResponse {
    private Long id;
    private String calle;
    private String numero;
    private String casa; // es el número de la casa o del departamento dentro de un conjunto de hogares
    private ZonaDTOResponse zona;
}

