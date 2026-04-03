package soda_roja.backend.dtoResponse;

import lombok.*;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class PersonaDomicilioDTOResponse {
    private Long id;
    private String dia;
    private PersonaDTOResponse personas;
    private DomicilioDTOResponse domicilios;
}
