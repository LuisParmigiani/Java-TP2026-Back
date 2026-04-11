package soda_roja.backend.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "venta")
@Schema(description = "Entidad que representa una venta")
public class Venta {
    @Id
    @Column(name="id",unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @Column(name = "fecha", nullable = false)
    private Date fecha;
    @Column(name = "total", nullable = false)
    private double total;
    @Column(name = "pagado", nullable = false)
    private boolean pagado;
    @ManyToOne
    @JoinColumn(name = "domicilio_id", nullable = false)
    private Domicilio domicilio;
    @OneToMany(mappedBy = "venta",cascade = CascadeType.ALL)
    private List<LineaPedido> lineasPedido;
    @Column(name = "estado", nullable = false)
    private String estado;
}
