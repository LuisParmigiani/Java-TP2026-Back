package soda_roja.backend.dto;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ZonaDTO {
    private long id;
    private String nombre;
    private String detalle;
}
