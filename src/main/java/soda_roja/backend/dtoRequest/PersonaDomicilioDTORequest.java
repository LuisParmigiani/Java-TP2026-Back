package soda_roja.backend.dtoRequest;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class PersonaDomicilioDTORequest {
    private Long id;
    private String dia;
}
