package soda_roja.backend.repository;


import java.util.Date;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import soda_roja.backend.model.Carga;

@Repository
public interface CargaRepository extends JpaRepository<Carga, Long>, JpaSpecificationExecutor<Carga> {
    // Find any carga for a user on the given day (use start/end of day Date bounds)
    List<Carga> findByUsuarioIdAndFechaHoraBetween(Long usuarioId, Date startOfDay, Date endOfDay);
    @Query("SELECT c FROM Carga c WHERE DATE(c.fechaHora) = CURRENT_DATE")
    List<Carga> findCargasHoy();
}