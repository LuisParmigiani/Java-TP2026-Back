package soda_roja.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Venta;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {
     // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
     // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
     List<Venta> findByDomicilioPersonaUsuarioId(Long idUsuario);

     Page<Venta> findByDomicilioPersonaUsuarioId(Long idUsuario, Pageable pageable);

     @Query("SELECT v FROM Venta v WHERE v.estado = 'En proceso' AND v.domicilio.id = :idDomicilio")
     List<Venta> findWithEstadoEnProceso(@Param("idDomicilio") Long idDomicilio);

     @Query("SELECT v FROM Venta v WHERE v.estado = 'Pendiente' AND v.domicilio.id = :idDomicilio")
     List<Venta> findWithEstadoPendiente(@Param("idDomicilio") Long idDomicilio);
}
