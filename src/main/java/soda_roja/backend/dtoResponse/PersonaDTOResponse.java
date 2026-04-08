package soda_roja.backend.dtoResponse;

import lombok.*;


import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PersonaDTOResponse {

    private Long id;
    private String tipoDoc;
    private String nroDocumento;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private float deuda;
    private List<PagoDTOResponse> pagos;
    private List<DomicilioDTOResponse> domicilios;

}
