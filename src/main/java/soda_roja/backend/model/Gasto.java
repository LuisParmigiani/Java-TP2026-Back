package soda_roja.backend.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "gasto")
@Schema(description = "Entidad que representa un gasto asociado o no a un camión")

public class Gasto {
	
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",unique = true, nullable = false)
	 private Long id;
    
    @Column(name = "detalle", nullable = false,unique = false)
    
	 private String detalle;
    
    @Column(name = "monto", nullable = false,unique = false)
	 private double monto;
    @Column(name = "fecha", nullable = false,unique = false)
	 private String fecha;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "camion_id", nullable = true)
    @JsonBackReference
	 private Camion camion;

	 // Getters y Setters

}
