package soda_roja.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import soda_roja.backend.model.Producto;
import soda_roja.backend.model.Zona;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    List<Producto> findByActivoTrue();

    Page<Producto> findByActivoTrue(Pageable pageable);

    @Query("SELECT pz.producto FROM ProductoZona pz WHERE pz.zona.id = :zonaId AND pz.producto.activo = true")
    List<Producto> findByZona(@Param("zonaId") Long zonaId);
}
