package soda_roja.backend.dtoResponse;

import lombok.*;
import java.util.List;


@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UsuarioDTOResponse {
    private Long id;
    private String nombreUsuario;
    private String nivelAcceso;
    private String email;
    private PersonaDTOResponse persona;
    private List<CargaDTOResponse> cargas;
    private Long personaId;
    private double precioPedidosSemanales;
}
