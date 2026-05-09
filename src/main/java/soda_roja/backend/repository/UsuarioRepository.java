package soda_roja.backend.repository; import java.util.Optional;
import soda_roja.backend.dtoResponse.UsuarioDTOResponse;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	Optional<Usuario> findByEmail(String email);
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
	
	List<Usuario>findByNivelAcceso(String nivelAcceso);
}
