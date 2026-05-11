package soda_roja.backend.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.*;

import java.util.ArrayList;
import java.util.List;

public class ProductoSpecification
{
    public record ProductoFiltrosDTO(
            String userId,
            String zone,
            String estado,
           String searchTerm,
           Double minPrice,
           Double maxPrice
    ) {}

    public static Specification<Producto> filtrar(ProductoSpecification.ProductoFiltrosDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.zone != null) {

                predicates.add(cb.equal(root.get("productosZona").get("zona").get("id"), Long.parseLong(filtro.zone)));
            }

            if (filtro.userId != null) {
                Join<Producto, ProductoZona> pz = root.join("productosZona");
                Join<ProductoZona, Zona> z = pz.join("zona");
                Join<Zona, Domicilio> d = z.join("domicilios");
                Join<Domicilio, Persona> p = d.join("persona");
                Join<Persona, Usuario> u = p.join("usuario");

                predicates.add(cb.equal(u.get("id"), filtro.userId()));

                query.distinct(true);
            }
            if (filtro.searchTerm != null) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + filtro.searchTerm.toLowerCase() + "%"));
            }
            if (filtro.estado != null) {
                boolean isActivo = filtro.estado.equalsIgnoreCase("activo");
                predicates.add(cb.equal(root.get("activo"), isActivo));
            }

            if (filtro.minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precio"), filtro.minPrice));
            }

            if (filtro.maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precio"), filtro.maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

