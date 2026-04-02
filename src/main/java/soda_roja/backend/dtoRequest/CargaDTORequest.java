package soda_roja.backend.dtoRequest;

import lombok.*;

import java.util.Date;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargaDTORequest {
    private Long id;
    private String tipo;;
    private Date fechaHora;
}
