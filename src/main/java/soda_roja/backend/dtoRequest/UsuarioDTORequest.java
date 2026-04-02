package soda_roja.backend.dtoRequest;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UsuarioDTORequest {
    private Long id;
    private String nombreUsuario;
    private String contrasena;
    private String nivelAcceso;
}
