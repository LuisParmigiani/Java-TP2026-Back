package soda_roja.backend.repository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.DiaZonaOrden;
@Repository
public interface DiaZonaOrdenRepository extends JpaRepository<DiaZonaOrden, Long> , JpaSpecificationExecutor<DiaZonaOrden> {
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
    DiaZonaOrden findTopByDiaZonaIdOrderByOrdenDesc(Long diaZonaId);

    DiaZonaOrden findByDomicilioIdAndDiaZonaId(Long domicilioId, Long diaZonaId);

    @Modifying
    @Transactional
    @Query("UPDATE DiaZonaOrden dzo SET dzo.orden = dzo.orden - 1 " +
            "WHERE dzo.diaZona.id = :diaZonaId AND dzo.orden > :ordenEliminado")
    void decrementarOrdenesPosteriores(@Param("diaZonaId") Long diaZonaId, @Param("ordenEliminado") Integer ordenEliminado);

}

