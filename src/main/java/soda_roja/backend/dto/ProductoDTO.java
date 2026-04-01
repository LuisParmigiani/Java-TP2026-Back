package soda_roja.backend.dto;

import lombok.*;

@Data //getters, setters, toString, equals y hashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoDTO {
	
	private int id;
	private String nombre;
	private String detalle;
	private double precio;
	private int stock;
	
	

}
