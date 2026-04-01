package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Carga;

@Repository
public interface CargaRepository extends JpaRepository<Carga, Long> {
}
