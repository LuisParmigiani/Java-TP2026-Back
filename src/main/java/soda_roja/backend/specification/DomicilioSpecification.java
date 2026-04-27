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
            Integer diaIndex,
            String habilitado,
            String   nameSearch
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
            if (filtro.habilitado() != null) {
                switch (filtro.habilitado()) {
                    case "Todos" -> predicates.add(cb.notEqual(root.get("habilitado"), 2)); // Mostrar todos excepto Deshabilitados
                    case "Habilitados" -> predicates.add(cb.equal(root.get("habilitado"), 1));
                    case "Pendientes de aprobacion" -> predicates.add(cb.equal(root.get("habilitado"), 0));
                    case "Rechazados" -> predicates.add(cb.equal(root.get("habilitado"), 2));
                }
            }else {
                predicates.add(cb.notEqual(root.get("habilitado"), 2));
            }
            if (filtro.nameSearch() != null && !filtro.nameSearch().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("calle")), "%" + filtro.nameSearch().toLowerCase() + "%"));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}