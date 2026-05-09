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
            String activo,
            Integer diaIndex,
            String habilitado,
            String   nameSearch,
            Long  diaId,
            Long camionId,
            Long    zona,
            String direccion
    ) {}

    public static Specification<Domicilio> filtrar(DomicilioFiltrosDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.usuarioId() != null) {
                predicates.add(cb.equal(root.get("persona").get("usuario").get("id"), filtro.usuarioId()));
            }
            if (filtro.activo() != null) {
                if (filtro.activo().equals("Mostrar Todas") || filtro.activo().isBlank()) {
                    // No agregar Predicate para mostrar todas las opciones
                } else {
                    if (filtro.activo().equalsIgnoreCase("Activas")) {
                        predicates.add(cb.equal(root.get("activo"), "Activa"));
                    } else {
                        predicates.add(cb.equal(root.get("activo"), "Inactiva"));

                    }
                }
            }


            if (filtro.habilitado() != null) {
                switch (filtro.habilitado()) {
                    case "Todos" -> predicates.add(cb.notEqual(root.get("habilitado"), "Deshabilitada")); // Mostrar todos excepto Deshabilitados
                    case "Habilitados" -> predicates.add(cb.equal(root.get("habilitado"), "Habilitada"));
                    case "Pendientes de aprobacion" -> predicates.add(cb.equal(root.get("habilitado"), "Pendiente"));
                    case "Rechazados" -> predicates.add(cb.equal(root.get("habilitado"), "Deshabilitada"));
                }
            }else {
                predicates.add(cb.notEqual(root.get("habilitado"), 2));
            }
            if (filtro.nameSearch() != null && !filtro.nameSearch().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("calle")), "%" + filtro.nameSearch().toLowerCase() + "%"));
            }
            if(filtro.diaId() != null){
                predicates.add(cb.equal(root.join("diaZonaOrden").join("zona").join("diaZona").get("id"), filtro.diaId()));
            }
            if(filtro.camionId() != null){

                predicates.add(cb.equal(root.join("diaZonaOrden").join("zona").join("camion").get("id"), filtro.camionId()));
            }
            if(filtro.zona() != null){
                predicates.add(cb.equal(root.join("diaZonaOrden").join("zona").get("id"), filtro.zona()));
            }
            if(filtro.direccion() != null && !filtro.direccion().isBlank()){
                predicates.add(cb.like(cb.lower(root.join("diaZonaOrden").join("domicilio").get("calle")), "%" + filtro.direccion().toLowerCase() + "%"));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}