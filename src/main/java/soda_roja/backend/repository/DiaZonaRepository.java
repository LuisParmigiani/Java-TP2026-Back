package soda_roja.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.DiaZona;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiaZonaRepository extends JpaRepository<DiaZona, Long>, JpaSpecificationExecutor<DiaZona> {

    @Query(value = "SELECT DISTINCT dz.* FROM dia_zona dz " +
            "INNER JOIN dia_zona_orden dzo ON dz.id = dzo.dia_zona_id " +
            "INNER JOIN domicilio d ON d.id = dzo.domicilio_id AND d.habilitado = 'Habilitada' " +
            "WHERE dz.dia_id = :diaId AND dz.zona_id = :zonaId",
            nativeQuery = true)
    List<DiaZona> findByZonaIdAndDiaId(@Param("zonaId") Long zonaId, @Param("diaId") Long diaId);


    @Query("SELECT dz FROM DiaZona dz JOIN FETCH dz.zona JOIN FETCH dz.zona.camion JOIN FETCH dz.dia WHERE dz.zona.camion.id = :camionId AND dz.dia.id = :diaId")
    List<DiaZona> findByCamionIdAndDia(@Param("camionId") Long camionId, @Param("diaId") Long diaId);

    @Query(
            "SELECT DISTINCT dz FROM DiaZona dz " +
                    "INNER JOIN FETCH dz.zona z " +
                    "INNER JOIN z.camion c " +
                    "INNER JOIN FETCH dz.diaZonaOrdenes dzo " +
                    "INNER JOIN FETCH dzo.domicilio d " +
                    "INNER JOIN  d.ventas v " +
                    "WHERE c.id = :camionId " +
                    "AND dz.dia.id = :diaId " +
                    "AND dzo.domicilio.habilitado = 'Habilitada' " +

                    "AND (:zonaNombre IS NULL OR TRIM(:zonaNombre) = '' OR LOWER(z.nombre) LIKE CONCAT('%', LOWER(:zonaNombre), '%')) " +
                    "AND (:calle IS NULL OR TRIM(:calle) = '' OR LOWER(d.calle) LIKE CONCAT('%', LOWER(:calle), '%')) " +
                    "ORDER BY dzo.orden ASC"
    )
    List<DiaZona> findByCamionIdAndDiaId(@Param("camionId") Long camionId, @Param("diaId") Long diaId, @Param("zonaNombre") String zonaNombre,
                                         @Param("calle") String calle);
    @Query(
        "SELECT DISTINCT dz FROM DiaZona dz " +
        "INNER JOIN FETCH dz.zona z " +
        "INNER JOIN z.camion c " +
        "INNER JOIN FETCH dz.diaZonaOrdenes dzo " +
        "INNER JOIN FETCH dzo.domicilio d " +
        "INNER JOIN  d.ventas v ON v.estado = 'En Proceso' " +
        "WHERE c.id = :camionId " +
        "AND dz.dia.id = :diaId " +
        "AND dzo.domicilio.habilitado = 'Habilitada' " +
        "AND NOT EXISTS (" +
        "  SELECT 1 FROM Venta v " +
        "  WHERE v.domicilio.id = dzo.domicilio.id " +
        "  AND v.fecha >= :initialDay " +
        "  AND v.fecha < :finalDay " +
        "AND v.estado = 'Completado'" +
        ") " +
                "AND (:zonaNombre IS NULL OR TRIM(:zonaNombre) = '' OR LOWER(z.nombre) LIKE CONCAT('%', LOWER(:zonaNombre), '%')) " +
                "AND (:calle IS NULL OR TRIM(:calle) = '' OR LOWER(d.calle) LIKE CONCAT('%', LOWER(:calle), '%')) " +
                "ORDER BY dzo.orden ASC"
    )
    List<DiaZona> findByCamionIdAndDiaIdWithFechaVentaToday(
            @Param("camionId") Long camionId,
            @Param("diaId") Long diaId,
            @Param("initialDay") LocalDateTime initialDay,
            @Param("zonaNombre") String zonaNombre,
            @Param("calle") String calle,
            @Param("finalDay") LocalDateTime finalDay
    );

}
