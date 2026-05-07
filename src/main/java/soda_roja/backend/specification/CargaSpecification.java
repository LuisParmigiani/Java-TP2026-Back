package soda_roja.backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.Carga;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CargaSpecification {

    public record CargaFiltrosDTO(
            Long usuarioId,
            LocalDate fecha
    ) {}

    public static Specification<Carga> filtrar(CargaFiltrosDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por usuario
            if (filtro.usuarioId != null) {
                predicates.add(cb.equal(root.get("usuario").get("id"), filtro.usuarioId));
            }

            // Filtro por fecha de hoy
            if (filtro.fecha != null) {
                LocalDate fechaInicio = filtro.fecha;
                LocalDate fechaFin = filtro.fecha.plusDays(1);

                Date dateInicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date dateFin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

                predicates.add(cb.between(root.get("fechaHora"), dateInicio, dateFin));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Carga> cargasDeHoyPorUsuario(Long usuarioId) {
        return filtrar(new CargaFiltrosDTO(usuarioId, LocalDate.now()));
    }
}

