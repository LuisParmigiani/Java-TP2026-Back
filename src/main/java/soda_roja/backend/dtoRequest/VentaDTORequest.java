package soda_roja.backend.dtoRequest;

import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VentaDTORequest {
    private long id;
    private Date fecha;
    private double total;
    private boolean pagado;
}
