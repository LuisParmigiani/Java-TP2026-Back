package soda_roja.backend.dtoResponse;

import lombok.*;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoDTOResponse {
	
	private int id;
	private String nombre;
	private String detalle;
	private double precio;
	private int stock;
	private List<soda_roja.backend.dtoResponse.ZonaDTORequest> zona;
	
	

}
