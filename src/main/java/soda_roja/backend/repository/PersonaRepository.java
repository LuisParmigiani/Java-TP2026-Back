package soda_roja.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {
	// JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
	// No es necesario escribir código adicional aquí a menos que quieras métodos personalizados

	// PersonaRepository.java
	List<Persona> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

	// Método que combina búsqueda por nombre/apellido con filtros y paginación
	Page<Persona> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
			String nombre,
			String apellido,
			Specification<Persona> spec,
			Pageable pageable
	);

	@Query("SELECT p FROM Persona p INNER JOIN p.usuario u WHERE p.estado = :estado and u.nivelAcceso = 'Usuario'")
	List<Persona> findByEstado(@Param("estado") String estado);
	
	@Query("SELECT p FROM Persona p INNER JOIN p.usuario u WHERE u.nivelAcceso = 'Usuario'")
	Page<Persona> findAllClientes(Pageable pageable);
}