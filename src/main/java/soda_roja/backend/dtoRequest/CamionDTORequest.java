package soda_roja.backend.dtoRequest;
import lombok.*;
@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CamionDTORequest {

    private Long id;
    private String patente;
    private String modelo;
	private String marca;
	private int Kilometraje;
}
