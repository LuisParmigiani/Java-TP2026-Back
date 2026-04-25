package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.DiaDomicilio;

import java.util.List;

@Repository
public interface DiaDomicilioRepository extends JpaRepository<DiaDomicilio, Long> {
    DiaDomicilio findByDomicilioIdAndDiaId(Long domicilioId, Long diaId);

    List<DiaDomicilio> findByDomicilioId( Long domicilioId);
}
