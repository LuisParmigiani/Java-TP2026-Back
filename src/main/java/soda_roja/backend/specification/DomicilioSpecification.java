package soda_roja.backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.Domicilio;

import java.util.ArrayList;
import java.util.List;

public class DomicilioSpecification {

    // DTO interno de filtros para la Specification
    public record DomicilioFiltrosDTO(
            Long usuarioId,
            Boolean activo,
            Integer diaIndex
    ) {}

    public static Specification<Domicilio> filtrar(DomicilioFiltrosDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.usuarioId() != null) {
                predicates.add(cb.equal(root.get("persona").get("usuario").get("id"), filtro.usuarioId()));
            }
            if (filtro.activo() != null) {
                predicates.add(cb.equal(root.get("activo"), filtro.activo()));
            }

            if (filtro.diaIndex() != null) {
                // Por ahora NO se agrega Predicate para evitar errores de runtime.
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}