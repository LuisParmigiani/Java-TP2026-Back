package soda_roja.backend.dtoResponse;

import lombok.*;
import soda_roja.backend.dtoRequest.ZonaDTORequest;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoDTOResponse {
	
	private Long id;
	private String nombre;
	private String detalle;
	private double precio;
	private int stock;
	private List<ZonaDTOResponse> zonas;
	
	

}
