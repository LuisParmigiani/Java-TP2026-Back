package soda_roja.backend.dtoResponse;

import lombok.*;
import soda_roja.backend.model.Persona;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UsuarioDTOResponse {
    private Long id;
    private String nombreUsuario;
    private String nivelAcceso;
    private PersonaDTOResponse persona;
}
