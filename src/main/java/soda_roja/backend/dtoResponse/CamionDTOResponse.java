package soda_roja.backend.dtoResponse;

import java.util.List;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CamionDTOResponse {
	
    private Long id;
    
    private String patente;
	
	
    private String modelo;
	
	
	private String marca;
	
	
	private int kilometraje;
	
	private List<GastoDTOResponse> gastos;

    private List<PersonaDomicilioDTOResponse> personasDomicilios;

}
