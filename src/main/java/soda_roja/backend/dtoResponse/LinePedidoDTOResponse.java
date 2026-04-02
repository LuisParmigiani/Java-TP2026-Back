package soda_roja.backend.dtoResponse;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LinePedidoDTOResponse {
    private Long id;
    private int cantidad;
    private float subtotal;
}
