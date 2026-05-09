package soda_roja.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Pago;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> , JpaSpecificationExecutor<Pago> {
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
    List<Pago> findByPersonaUsuarioIdOrderByFechaDesc(Long userId);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE p.fecha BETWEEN :mesAtras AND :hoy AND p.estado = 'Aprobado'")
    Float findIngresosMensuales(@Param("hoy") java.util.Date hoy, @Param("mesAtras") java.util.Date mesAtras);
}
