package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Persona;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.PersonaRepository;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository repository;

    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<PersonaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
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
        }
        if(entidad.getTelefono() != null) {
            existing.setTelefono(entidad.getTelefono());
        }
        if(entidad.getSaldo() != null) {
            existing.setSaldo(entidad.getSaldo());
        }

        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        Persona persona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
        repository.delete(persona);
    }

    private Domicilio findDomicilioOrThrow(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }

    private PersonaDTOResponse mapToDTO(Persona persona, String[] populate) {
        return mapToDTOMapper.mapToDTO(persona, populate);
    }


}
