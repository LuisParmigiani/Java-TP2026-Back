package soda_roja.backend.dtoRequest;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LinePedidoDTORequest {
    private Long id;
    private int cantidad;
    private float subtotal;
}
