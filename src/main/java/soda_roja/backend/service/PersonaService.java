package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.PersonaDTORequest;
import soda_roja.backend.dtoResponse.PersonaDTOResponse;
import soda_roja.backend.model.Persona;
import soda_roja.backend.repository.PersonaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class PersonaService {

    @Autowired
    private PersonaRepository repository;
    @Autowired
    private PagoService pagoService;


    public List<PersonaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

   public PersonaDTOResponse getById(Long id) {
        Persona persona = repository.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrado con id: " + id));
        return mapToDTO(persona);
    }

    public PersonaDTOResponse save(PersonaDTORequest entidad) {
        Persona persona = Persona.builder()
                .tipoDoc(entidad.getTipoDoc())
                .nroDocumento(entidad.getNroDocumento())
                .nombre(entidad.getNombre())
                .apellido(entidad.getApellido())
                .email(entidad.getEmail())
                .telefono(entidad.getTelefono())
                .deuda(entidad.getDeuda())
                .build();

        return mapToDTO(repository.save(persona));
    }

    public PersonaDTOResponse update(Long id, PersonaDTORequest entidad) {

        Persona existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Persona no encontrado con id: " + id));
        existing.setTipoDoc(entidad.getTipoDoc());
        existing.setNroDocumento(entidad.getNroDocumento());
        existing.setNombre(entidad.getNombre());
        existing.setApellido(entidad.getApellido());
        existing.setEmail(entidad.getEmail());
        existing.setTelefono(entidad.getTelefono());
        existing.setDeuda(entidad.getDeuda());


        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
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
                .deuda(persona.getDeuda())
                .pagos(persona.getPagos() != null ? persona.getPagos().stream().map(pago ->
                        new PagoService().mapToDTO(pago)
                ).collect(Collectors.toList()) : List.of())
                .build();
    }


}

