package soda_roja.backend.dto;
import lombok.*;
@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CamionDTO {

    private Long id;
    private String patente;
    private String modelo;
	private String marca;
	private int Kilometraje;
}
