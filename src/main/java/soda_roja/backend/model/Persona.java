package soda_roja.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( unique = true, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "tipoDoc", nullable = false)
    private String tipoDoc;

    @Column(name = "nroDocumento", unique = true, nullable = false)
    private String nroDocumento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

     @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "telefono", nullable = false, unique = true)
    private String telefono;

    @Column(name = "saldo", nullable = true)
    private float saldo;
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private String estado = "Habilitado";
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Pago> pagos;

    @OneToMany(mappedBy = "persona")
    private List<Domicilio> Domicilios;
}