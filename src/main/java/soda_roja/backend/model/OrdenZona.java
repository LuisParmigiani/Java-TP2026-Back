package soda_roja.backend.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ordenZona")
@Schema(description = "Entidad utilizada para guardar el orden en el que se deben recorrer las zonas para atender a los domicilios de la manera más óptima cada día")
public class OrdenZona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "dia", nullable = false)
    private Integer dia;
    @Column(name = "orden", nullable = false)
    private Integer orden;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zonaId", nullable = false)
    private Zona zona;
    @ManyToOne
    @JoinColumn(name = "domicilioId", nullable = false)
    private Domicilio domicilio;
    
    
    
}
