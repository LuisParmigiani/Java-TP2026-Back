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
	
	
	private Boolean estado;
	
	
	private List<GastoDTOResponse> gastos;


    private List<CargaDTOResponse> cargas;
    
    private List<ZonaDTOResponse> zonas;

    private List<Long> gastoIds;

    private List<Long> cargasIds;
    
     private List<Long> zonaIds;
    
    

}
