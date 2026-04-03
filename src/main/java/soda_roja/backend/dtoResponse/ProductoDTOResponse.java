package soda_roja.backend.dtoResponse;

import lombok.*;
import soda_roja.backend.dtoRequest.ZonaDTORequest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
	
	@JsonBackReference
	private List<ZonaDTOResponse> zonas;
	
	

}
