package soda_roja.backend.dto;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class PersonaDomicilioDTO {
    private Long id;
    private String dia;
}
