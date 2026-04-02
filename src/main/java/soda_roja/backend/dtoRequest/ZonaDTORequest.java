package soda_roja.backend.dtoRequest;

import lombok.*;
import soda_roja.backend.model.Domicilio;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ZonaDTORequest {
    private long id;
    private String nombre;
    private String detalle;

}
