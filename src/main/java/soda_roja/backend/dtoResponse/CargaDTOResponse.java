package soda_roja.backend.dtoResponse;

import lombok.*;
import java.util.Date;
import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargaDTOResponse {
    private Long id;
    private String tipo;
    private Date fechaHora;
    private UsuarioDTOResponse usuario;
    private CamionDTOResponse camion;
    private List<CargaProductoDTOResponse> cargasProducto;
    private Long usuarioId;
    private Long camionId;
}
