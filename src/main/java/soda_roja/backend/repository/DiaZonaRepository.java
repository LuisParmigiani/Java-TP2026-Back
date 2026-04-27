package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.DiaZona;

import java.util.List;

@Repository
public interface DiaZonaRepository extends JpaRepository<DiaZona, Long> {
    @Query("SELECT dz FROM DiaZona dz JOIN FETCH dz.zona JOIN FETCH dz.dia WHERE dz.zona.id = :zonaId AND dz.dia.id = :diaId")
    List<DiaZona> findByZonaIdAndDiaId(@Param("zonaId") Long zonaId, @Param("diaId") Long diaId);

    @Query("SELECT dz FROM DiaZona dz JOIN FETCH dz.zona JOIN FETCH dz.zona.camion JOIN FETCH dz.dia WHERE dz.zona.camion.id = :camionId AND dz.dia.id = :diaId")
    List<DiaZona> findByCamionIdAndDiaId(@Param("camionId") Long camionId, @Param("diaId") Long diaId);
    

}
