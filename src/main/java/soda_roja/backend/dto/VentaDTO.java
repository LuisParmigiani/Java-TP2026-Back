package soda_roja.backend.dto;
import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VentaDTO {
    private long id;
    private Date fecha;
    private double total;
    private boolean pagado;
}
