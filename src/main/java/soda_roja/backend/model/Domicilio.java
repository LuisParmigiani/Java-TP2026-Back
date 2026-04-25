package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "domicilio")
@Schema(description = "Entidad que representa un domicilio del hogar")
public class Domicilio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "calle", nullable = false)
    private String calle;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "casa")
    private String casa;
    //De que zona es el domicilio
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;
    //A que persona pertenece el domicilio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id")
    private Persona persona;
    //Indica las ventas que se han hecho a ese domicilio
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> ventas = new ArrayList<>();
    //Indica el orden en que se atiende ese domicilio en ese dia y esa zona
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DiaZonaOrden> diaZonaOrden = new ArrayList<>();
    //Indica en que días se atiende ese domicilio
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DiaDomicilio> diasDomicilio = new ArrayList<>();
    //Indica que productos se entregan en ese domicilio
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductoDomicilio> productoDomicilio = new ArrayList<>();

    @Column(name = "activo")
    private Boolean activo;
    //Indica lo que se compra semanalmente en ese domicilio por producto
    @OneToMany(mappedBy = "domicilio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PedidoSemanal> pedidosSemanal;

    @Column(name = "habilitado")
    private Integer habilitado;
}
