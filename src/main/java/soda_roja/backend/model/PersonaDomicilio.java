package soda_roja.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "personaDomicilio")
@Schema(description = "Entidad que representa el hogar de una persona")
public class PersonaDomicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "dia", nullable = false)
    private boolean[] dia = new boolean[7];

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personaId", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domicilioId", nullable = false)
    private Domicilio domicilio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camionId")
    private Camion camion;

    @OneToMany(mappedBy = "personaDomicilio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venta> ventas;

    @OneToMany(mappedBy = "personaDomicilio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductoPersonaDomicilio> productosPersonaDomicilio;
}
