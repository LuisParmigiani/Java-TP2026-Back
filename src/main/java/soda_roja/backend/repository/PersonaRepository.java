package soda_roja.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
	
	// PersonaRepository.java
	List<Persona> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);


}
