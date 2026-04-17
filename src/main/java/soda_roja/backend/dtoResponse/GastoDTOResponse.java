package soda_roja.backend.dtoResponse;



import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GastoDTOResponse {
	
	
	 private Long id;
    
	 private String detalle;
    
	 private double monto;
    
	 private String fecha;
	 
	private CamionDTOResponse camion;

	private Long camionId;
}
