package soda_roja.backend.dtoResponse;

import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiaZonaOrdenDTOResponse {
    private Long id;
    private Integer orden;
    private DiaZonaDTOResponse diaZona;
    private Long diaZonaId;
    private DomicilioDTOResponse domicilio;
    private Long domicilioId;
}
