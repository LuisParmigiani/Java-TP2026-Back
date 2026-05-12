package soda_roja.backend.specification;

import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Dia;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Zona;

public class PersonaSpecification {
    
    public static Specification<Persona> porZona(Zona zona) {
        return (root, query, cb) -> zona == null ? null : cb.equal(root.get("Domicilios").get("zona"), zona);
    }
    
    public static Specification<Persona> porCamion(Camion camion) {
        return (root, query, cb) -> camion == null ? null : cb.equal(root.get("Domicilios").get("zona").get("camion"), camion);
    }
    
    public static Specification<Persona> porDia(Dia dia) {
        return (root, query, cb) -> dia == null ? null : cb.equal(root.get("Domicilios").get("diasDomicilio").get("dia"), dia);
    }
    
    public static Specification<Persona> porNivelAcceso(String estado) {
        return (root, query, cb) -> estado == null ? null : cb.equal(root.get("usuario").get("nivelAcceso"), estado);
    }
}
