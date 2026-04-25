package soda_roja.backend.dtoResponse;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaDomicilioDTOResponse {
    private Long id;
    private String estado;
    private DiaDTOResponse dia;
    private Long diaId;
    private DomicilioDTOResponse domicilio;
    private Long domicilioId;
}
