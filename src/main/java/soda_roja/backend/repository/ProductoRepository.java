package soda_roja.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
	// JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
	// No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
}
