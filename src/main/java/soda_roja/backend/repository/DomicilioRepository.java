package soda_roja.backend.repository;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Domicilio;

import java.util.List;

@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, Long>, JpaSpecificationExecutor<Domicilio> {
    List<Domicilio> findDomicilioByPersonaUsuarioId(
            Long id

    );
    @Query("SELECT d FROM Domicilio d WHERE d.calle LIKE %:calleNumero% OR d.numero LIKE %:calleNumero%")
    List<Domicilio> findDomicilioByCalleOrNumero(@Param("calleNumero") String calleNumero);

    List<Domicilio> findDomicilioByHabilitado(String habilitado);


}
