package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import soda_roja.backend.model.PedidoSemanal;
import java.util.List;

public interface PedidoSemanalRepository extends JpaRepository<PedidoSemanal, Long>, JpaSpecificationExecutor<PedidoSemanal> {
    List<PedidoSemanal> findAllByDomicilioId(Long domicilioId);
}
