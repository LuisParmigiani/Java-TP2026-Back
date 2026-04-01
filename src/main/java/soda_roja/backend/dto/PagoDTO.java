package soda_roja.backend.dto;

import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PagoDTO {
    private Long id;
    private float monto;
    private Date fecha;
    private String metodoPago;
}
