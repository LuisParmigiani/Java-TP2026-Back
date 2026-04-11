package soda_roja.backend.dtoResponse;

import lombok.*;


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
    private ZonaDTOResponse zona;
    private boolean[] dia;
    private List<VentaDTOResponse> ventas;
    private List<ProductoDomicilioDTOResponse> productosDomicilio;
    private Boolean activo;
}

