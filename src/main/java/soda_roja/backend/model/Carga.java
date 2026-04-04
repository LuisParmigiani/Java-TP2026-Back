package soda_roja.backend.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "carga")
@Schema(description = "Entidad que representa una carga y descarga de mercadería")

public class Carga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "tipo", nullable = false)
    private String tipo;;
    @Column(name = "fechaHora", nullable = false)
    private Date fechaHora;

    @ManyToOne()
    @JoinColumn(name = "usuarioCarga",nullable = false)
    private Usuario usuario;
    
    @ManyToOne()
    @JoinColumn(name = "camionCarga",nullable = false)
    private Camion camion;


}
