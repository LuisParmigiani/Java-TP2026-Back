package soda_roja.backend.dtoRequest;

import lombok.*;

import java.util.List;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoDTORequest {
	
	private int id;
	private String nombre;
	private String detalle;
	private double precio;
	private int stock;

	

}
