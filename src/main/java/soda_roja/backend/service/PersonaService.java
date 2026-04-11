package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
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

    public PersonaDTOResponse update(Long id, PersonaDTORequest entidad) {

        Persona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));

        existing.setTipoDoc(entidad.getTipoDoc());
        existing.setNroDocumento(entidad.getNroDocumento());
        existing.setNombre(entidad.getNombre());
        existing.setApellido(entidad.getApellido());
        existing.setEmail(entidad.getEmail());
        existing.setTelefono(entidad.getTelefono());
        existing.setSaldo(entidad.getSaldo());

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
                        ? persona.getDomicilios().stream().map(this::mapDomicilioToDTO).toList()
                        : List.of())
                .pagos(persona.getPagos() != null
                        ? persona.getPagos().stream().map(pagoService::mapToDTO).toList()
                        : List.of())
                .build();
    }

    private DomicilioDTOResponse mapDomicilioToDTO(Domicilio domicilio) {
        return new DomicilioService().mapToDTO(domicilio);
    }
}
