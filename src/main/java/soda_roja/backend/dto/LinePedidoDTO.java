package soda_roja.backend.dto;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LinePedidoDTO {
    private Long id;
    private int cantidad;
    private float subtotal;
}
