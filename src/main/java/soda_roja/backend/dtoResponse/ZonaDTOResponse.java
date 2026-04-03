package soda_roja.backend.dtoResponse;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class ZonaDTOResponse {
    private long id;
    private String nombre;
    private String detalle;
    
    @JsonManagedReference
    private List<ProductoDTOResponse> productos;
}
