package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.ProductoZona;
import java.util.Optional;

@Repository

public interface ProductoZonaRepository extends JpaRepository<ProductoZona, Long>, JpaSpecificationExecutor<ProductoZona> {
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
    public Optional<ProductoZona> findByZonaIdAndProductoId(Long zonaId, Long productoId);
}
