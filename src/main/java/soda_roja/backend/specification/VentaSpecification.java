package soda_roja.backend.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.*;

import java.util.ArrayList;
import java.util.List;

public class VentaSpecification {

        public record VentaFiltrosDTO(
                String estado,
                Long userId,
                String zona
        ) {}

        public static Specification<Venta> filtrar(VentaFiltrosDTO filtro) {
            return (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if (filtro.estado != null && !"Todos".equals(filtro.estado)) {
                    switch (filtro.estado) {
                        case "Pendientes" -> predicates.add(cb.equal(root.get("estado"), "Pendiente"));
                        case "En Proceso" -> predicates.add(cb.equal(root.get("estado"), "En proceso"));
                        case "Completadas" -> predicates.add(cb.equal(root.get("estado"), "Completada"));
                        case "Canceladas" -> predicates.add(cb.equal(root.get("estado"), "Cancelada"));

                    }
                }
                if(filtro.userId != null){
                    Join<Venta, Domicilio> domicilioJoin = root.join("domicilio");
                    Join<Domicilio, Persona> personaJoin = domicilioJoin.join("persona");
                    predicates.add(cb.equal(personaJoin.get("usuario").get("id"), filtro.userId));
                }
                if (filtro.zona != null && !filtro.zona.isEmpty() && !filtro.zona.equalsIgnoreCase("Todas")) {
                    Join<Venta, Domicilio> domicilioJoin = root.join("domicilio");
                    predicates.add(cb.equal(domicilioJoin.get("zona").get("nombre"), filtro.zona));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };
        }
    }


