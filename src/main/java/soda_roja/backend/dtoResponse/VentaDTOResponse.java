package soda_roja.backend.dtoResponse;
import lombok.*;
import soda_roja.backend.model.LineaPedido;

import java.util.Date;
import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VentaDTOResponse {
    private long id;
    private Date fecha;
    private double total;
    private boolean pagado;
    private long idDomicilio;
    private List<LineaPedidoDTOResponse> lineasPedido;
}
