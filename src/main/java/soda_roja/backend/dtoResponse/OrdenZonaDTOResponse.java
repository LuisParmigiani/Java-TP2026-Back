package soda_roja.backend.dtoResponse;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenZonaDTOResponse {
    private Long id;
    
    private Integer dia;
    
    private Integer orden;
    
    private Long zonaId;
    
    private Long domicilioId;
    
    private ZonaDTOResponse zona;
    
    private DomicilioDTOResponse domicilio;
    
}
