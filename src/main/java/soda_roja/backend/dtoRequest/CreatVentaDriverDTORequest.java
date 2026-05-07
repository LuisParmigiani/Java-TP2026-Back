package soda_roja.backend.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatVentaDriverDTORequest {
    private VentaDTORequest venta;
    private List<ProductoDomicilioDTORequest> productoDomicilio;
}
