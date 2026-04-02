package soda_roja.backend.dtoResponse;

import lombok.*;
import soda_roja.backend.model.Domicilio;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ZonaDTOResponse {
    private long id;
    private String nombre;
    private String detalle;
    private List<soda_roja.backend.dtoResponse.ProductoDTORequest> producto;
    private List<Domicilio> domicilio;
}
