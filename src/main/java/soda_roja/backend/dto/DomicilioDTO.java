package soda_roja.backend.dto;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class DomicilioDTO {
    private Long id;
    private String calle;
    private String numero;
    private String casa; // es el número de la casa o del departamento dentro de un conjunto de hogares
}
