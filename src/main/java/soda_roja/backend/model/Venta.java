package soda_roja.backend.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private PersonaDomicilio personaDomicilio;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedido> lineasPedido;
}
