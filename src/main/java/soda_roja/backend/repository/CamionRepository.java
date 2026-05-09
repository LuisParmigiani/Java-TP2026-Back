package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Carga;
import soda_roja.backend.model.Domicilio;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, Long>, JpaSpecificationExecutor<Camion> {
    // JpaRepository ya tiene métodos para CRUD (Create, Read, Update, Delete)
    // No es necesario escribir código adicional aquí a menos que quieras métodos personalizados
    @Query("SELECT c FROM Carga c WHERE c.usuario.id = :userId AND c.fechaHora BETWEEN :initialDay AND :finalDay")
    List<Carga> cargasDelDia(@Param("userId") Long userId, @Param("finalDay") LocalDateTime finalDay, @Param("initialDay") LocalDateTime initialDay);
}



