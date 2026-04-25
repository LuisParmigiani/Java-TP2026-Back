package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Dia;

import java.util.Optional;

@Repository
public interface DiaRepository extends JpaRepository<Dia, Long> {
    Optional<Dia> findByNombre(String nombre);
}
