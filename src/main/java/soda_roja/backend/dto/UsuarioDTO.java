package soda_roja.backend.dto;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private String contrasena;
    private String nivelAcceso;
}
