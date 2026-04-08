package soda_roja.backend.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombreUsuario", unique = true, nullable = false)
    private String nombreUsuario;
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
     @Column(name = "nivelAcceso", nullable = false)
     private String nivelAcceso;
     @OneToOne
     @JoinColumn(name = "personaId",nullable = false)
     @JsonManagedReference
     private Persona persona;
     @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
     private List<Carga> cargas;

}
