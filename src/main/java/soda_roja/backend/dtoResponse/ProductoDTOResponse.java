package soda_roja.backend.dtoResponse;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import soda_roja.backend.model.Domicilio;

import java.util.List;


@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTOResponse {
    private Long id;
    private String nombre;
    private String detalle;
    private double precio;
    private int stock;
    private String imagenUrl;
    private boolean activo; // Indica si el producto está activo o inactivo
    private List<CargaProductoDTOResponse> cargaProductos;
    private List<ProductoZonaDTOResponse> productoZonas;
    private List<ProductoDomicilioDTOResponse>  productosDomicilio;
    private List<Long> productoZonaIds;
    private List<Long> productoDomicilioIds;

}
