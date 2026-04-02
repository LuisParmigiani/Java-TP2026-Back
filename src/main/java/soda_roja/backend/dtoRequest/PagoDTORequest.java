package soda_roja.backend.dtoRequest;

import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PagoDTORequest {
    private Long id;
    private float monto;
    private Date fecha;
    private String metodoPago;
}
