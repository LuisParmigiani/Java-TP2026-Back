package soda_roja.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Zona;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long>, JpaSpecificationExecutor<Zona> {
}
