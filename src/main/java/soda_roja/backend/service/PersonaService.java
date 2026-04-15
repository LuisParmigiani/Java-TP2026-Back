package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoRequestPut.PersonaDTORequestPut;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
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
    private PagoService pagoService;

    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private DomicilioService domicilioService;

    public List<PersonaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public PersonaDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }

    public PersonaDTOResponse save(PersonaDTORequest entidad) {


        Persona persona = Persona.builder()
                .tipoDoc(entidad.getTipoDoc())
                .nroDocumento(entidad.getNroDocumento())
                .nombre(entidad.getNombre())
                .apellido(entidad.getApellido())
                .email(entidad.getEmail())
                .telefono(entidad.getTelefono())
                .saldo(entidad.getSaldo())
                .build();

        return mapToDTO(repository.save(persona));
    }

    public PersonaDTOResponse update(Long id, PersonaDTORequestPut entidad) {
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

        return mapToDTO(repository.save(existing));
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

    public PersonaDTOResponse mapToDTO(Persona persona) {
        return PersonaDTOResponse.builder()
                .id(persona.getId())
                .tipoDoc(persona.getTipoDoc())
                .nroDocumento(persona.getNroDocumento())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .email(persona.getEmail())
                .telefono(persona.getTelefono())
                .saldo(persona.getSaldo())
                .domicilios(persona.getDomicilios() != null
                        ? persona.getDomicilios().stream().map(domicilioService::mapToDTO).toList()
                        : List.of())
                .pagos(persona.getPagos() != null
                        ? persona.getPagos().stream().map(pagoService::mapToDTO).toList()
                        : List.of())
                .build();
    }


}
