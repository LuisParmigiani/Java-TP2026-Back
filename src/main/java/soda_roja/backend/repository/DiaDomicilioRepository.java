package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.DiaDomicilio;

import java.util.List;

@Repository
public interface DiaDomicilioRepository extends JpaRepository<DiaDomicilio, Long> {
    @Query("SELECT dd FROM DiaDomicilio dd WHERE dd.domicilio.id = :domicilioId AND dd.dia.id = :diaId")
    List<DiaDomicilio> findByDomicilioIdAndDiaId(@Param("domicilioId") Long domicilioId, @Param("diaId") Long diaId);

    @Query("SELECT dd FROM DiaDomicilio dd WHERE dd.domicilio.id = :domicilioId")
    List<DiaDomicilio> findByDomicilioId(@Param("domicilioId") Long domicilioId);
}
