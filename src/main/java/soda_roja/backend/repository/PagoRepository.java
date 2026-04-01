package soda_roja.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Pago;
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
}
