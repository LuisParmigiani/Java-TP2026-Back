package soda_roja.backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonaDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
}
