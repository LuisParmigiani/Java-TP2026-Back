package soda_roja.backend.dtoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZonaDTOResponse {
    private Long id;
    private String nombre;
    private String detalle;
    private boolean[] dia;
    private List<ProductoZonaDTOResponse> productoZonas;
    private List<DomicilioDTOResponse> domicilios;
    private CamionDTOResponse camion;
    private Long camionId;
    private List<Long> domicilioIds;
    private List<Long> productoZonaIds;
}


