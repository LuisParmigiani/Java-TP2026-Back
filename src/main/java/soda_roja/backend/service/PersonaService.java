package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Persona;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.PersonaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Dia;
import soda_roja.backend.specification.PersonaSpecification;



@Service
public class PersonaService {

    @Autowired
    private PersonaRepository repository;

    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private MapToDTO mapToDTOMapper;

    public Page<PersonaDTOResponse> getAll(int page, int size, String[] populate) {
        return repository.findAll(PageRequest.of(page, size))
            .map(p -> mapToDTO(p, populate));
    }
    public Page<PersonaDTOResponse> getByNameAndFiltered(String query, Zona zona, Camion camion, Dia dia, String ordenSaldo, int page, int size, String[] populate) {
        Specification<Persona> spec = Specification.where(PersonaSpecification.porZona(zona))
            .and(PersonaSpecification.porCamion(camion))
            .and(PersonaSpecification.porDia(dia))
            .and((root, query1, cb) -> {
                if (query == null || query.isEmpty()) return null;
                return cb.or(
                    cb.like(cb.lower(root.get("nombre")), "%" + query.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("apellido")), "%" + query.toLowerCase() + "%")
                );
            });
        
        Sort sort = "Descendente".equals(ordenSaldo) ? 
            Sort.by(Sort.Direction.DESC, "saldo") : 
            Sort.by(Sort.Direction.ASC, "saldo");
        
        return repository.findAll(spec, PageRequest.of(page, size, sort))
            .map(p -> mapToDTO(p, populate));
    }

    public Page<Persona> getAllFiltrado(Zona zona, Camion camion, Dia dia, String ordenSaldo, int page, int size) {
        Specification<Persona> spec = Specification.where(PersonaSpecification.porZona(zona))
            .and(PersonaSpecification.porCamion(camion))
            .and(PersonaSpecification.porDia(dia));
        
        Sort sort = "Descendente".equals(ordenSaldo) ? 
            Sort.by(Sort.Direction.DESC, "saldo") : 
            Sort.by(Sort.Direction.ASC, "saldo");
        
        return repository.findAll(spec, PageRequest.of(page, size, sort));
    }
 

    public PersonaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }

    public PersonaDTOResponse save(PersonaDTORequest entidad,String[] populate) {


        Persona persona = Persona.builder()
                .tipoDoc(entidad.getTipoDoc())
                .nroDocumento(entidad.getNroDocumento())
                .nombre(entidad.getNombre())
                .apellido(entidad.getApellido())
                .email(entidad.getEmail())
                .telefono(entidad.getTelefono())
                .saldo(entidad.getSaldo())
                .estado(entidad.getEstado()!= null ? entidad.getEstado() : "Pendiente") // Si no se proporciona estado, se asigna "Pendiente"
                .build();

        return mapToDTO(repository.save(persona), populate);
    }

    public PersonaDTOResponse update(Long id, PersonaDTORequestPut entidad,String[] populate) {
        Persona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));

        if(entidad.getTipoDoc() != null) {
            existing.setTipoDoc(entidad.getTipoDoc());
        }
        if(entidad.getNroDocumento() != null) {
            existing.setNroDocumento(entidad.getNroDocumento());
        }
        if(entidad.getNombre() != null) {
            existing.setNombre(entidad.getNombre());
        }
        if(entidad.getApellido() != null) {
            existing.setApellido(entidad.getApellido());
        }
        if(entidad.getEmail() != null) {
            existing.setEmail(entidad.getEmail());
            // Sincronizar email con el usuario asociado
            if(existing.getUsuario() != null) {
                existing.getUsuario().setEmail(entidad.getEmail());
            }
        }
        if(entidad.getTelefono() != null) {
            existing.setTelefono(entidad.getTelefono());
        }
        if(entidad.getSaldo() != null) {
            existing.setSaldo(entidad.getSaldo());
        }
        if(entidad.getEstado() != null) {
			existing.setEstado(entidad.getEstado());
		}

        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        Persona persona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
        repository.delete(persona);
    }
    public void logicDelete(Long id) {
		Persona persona = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
		persona.setEstado("Deshabilitado");
		repository.save(persona);
	}

    private Domicilio findDomicilioOrThrow(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }

    private PersonaDTOResponse mapToDTO(Persona persona, String[] populate) {
        return mapToDTOMapper.mapToDTO(persona, populate);
    }


}
