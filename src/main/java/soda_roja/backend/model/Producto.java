package soda_roja.backend.model;
import lombok.*;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "producto")
@Schema(name = "Producto", description = "Representa un producto disponible en el sistema")
public class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique=true, nullable=false)
	private Long id;
	
	@Column(unique=true, length=100)
	private String nombre;

	@Column(length = 250, nullable= false)
	private String detalle;
	
	@Column(nullable = false)
	private Double precio;
	
	@Column(nullable = false)
	private Integer stock;
	@Column(length = 500)
	private String imagenUrl;
	@Column(nullable = false)
	private boolean activo; // Indica si el producto está activo o inactivo

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<ProductoZona> productosZona;

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<ProductoDomicilio> productosDomicilio; 
	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<CargaProducto> cargasProducto;
	
}
