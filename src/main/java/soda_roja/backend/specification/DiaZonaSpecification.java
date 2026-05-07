package soda_roja.backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.DiaZona;
import soda_roja.backend.model.Venta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiaZonaSpecification {

    // DTO interno de filtros para la Specification
    public record DiaZonaFiltrosDTO(
            Long diaId,
            Long camionId,
            String zonaNombre,
            String direccionBusqueda
    ) {}

    public static Specification<DiaZona> filtrar(DiaZonaFiltrosDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por día
            if (filtro.diaId() != null) {
                predicates.add(cb.equal(root.get("dia").get("id"), filtro.diaId()));
            }

            // Filtro por camión (a través de la zona)
            if (filtro.camionId() != null) {
                predicates.add(cb.equal(root.get("zona").get("camion").get("id"), filtro.camionId()));
            }

            // Filtro por nombre de zona
            if (filtro.zonaNombre() != null && !filtro.zonaNombre().isBlank() && !filtro.zonaNombre().equals("Todas")) {
                predicates.add(cb.like(cb.lower(root.get("zona").get("nombre")), "%" + filtro.zonaNombre().toLowerCase() + "%"));
            }


            if (filtro.direccionBusqueda() != null && !filtro.direccionBusqueda().isBlank()) {
                predicates.add(cb.like(cb.lower(root.join("diaZonaOrdenes").join("domicilio").get("calle")), "%" + filtro.direccionBusqueda().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<DiaZona> byCamionAndDia(Long camionId, Long diaId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.join("zona").join("camion").get("id"), camionId));
            predicates.add(cb.equal(root.get("dia").get("id"), diaId));
            predicates.add(cb.equal(root.join("diaZonaOrdenes").join("domicilio").get("habilitado"), "Habilitada"));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    public static Specification<DiaZona> byZona(String zona) {
        return (root, query, cb) -> {
            if (zona == null || zona.isEmpty()) {
                return cb.conjunction(); // sin filtro
            }
            return cb.like(cb.lower(root.get("zona").get("nombre")), "%" + zona.toLowerCase() + "%");
        };
    }


    public static Specification<DiaZona> byDireccion(String direccion) {
        return (root, query, cb) -> {
            if (direccion == null || direccion.isEmpty()) {
                return cb.conjunction(); // sin filtro
            }
            return cb.like(cb.lower(root.join("diaZonaOrdenes").join("domicilio").get("calle")),
                          "%" + direccion.toLowerCase() + "%");
        };
    }




}

