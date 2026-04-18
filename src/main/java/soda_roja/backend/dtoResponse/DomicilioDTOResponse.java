package soda_roja.backend.dtoResponse;

import lombok.*;
import soda_roja.backend.model.Persona;


import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class DomicilioDTOResponse {
    private Long id;
    private String calle;
    private String numero;
    private String casa; // es el número de la casa o del departamento dentro de un conjunto de hogares
    private Integer[] dia;
    private ZonaDTOResponse zona;
    private List<VentaDTOResponse> ventas;
    private List<ProductoDomicilioDTOResponse> productosDomicilio;
    private CamionDTOResponse camion;
    private PersonaDTOResponse persona;
    private Boolean activo;
    private List<PedidoSemanalDTOResponse> pedidosSemanales;
    private List<Long> ventaIds;
    private List<Long> productoDomicilioIds;
    private List<Long> pedidoSemanalIds;
    private Long zonaId;
    private Long personaId;

}

