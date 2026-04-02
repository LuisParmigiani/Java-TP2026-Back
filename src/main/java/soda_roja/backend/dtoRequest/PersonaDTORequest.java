package soda_roja.backend.dtoRequest;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PersonaDTORequest {

    private Long id;
    private String tipoDoc;
    private String nroDocumento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private float deuda;


}
