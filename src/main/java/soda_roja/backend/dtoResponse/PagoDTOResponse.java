package soda_roja.backend.dtoResponse;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PagoDTOResponse {
    private Long id;
    private float monto;
    private Date fecha;
    private String metodoPago;
    private PersonaDTOResponse persona;
    private Long personaId;
    private String estado;
}
