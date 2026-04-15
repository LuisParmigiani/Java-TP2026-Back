package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.PagoDTORequest;
import soda_roja.backend.dtoRequestPut.PagoDTORequestPut;
import soda_roja.backend.dtoResponse.PagoDTOResponse;
import soda_roja.backend.model.Pago;
import soda_roja.backend.model.Persona;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.PagoRepository;

import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

    public List<PagoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public PagoDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
    }

    public PagoDTOResponse save(PagoDTORequest entidad) {
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());

        Pago pago = Pago.builder()
                .fecha(entidad.getFecha())
                .monto(entidad.getMonto())
                .metodoPago(entidad.getMetodoPago())
                .persona(persona)
                .build();
        return mapToDTO(repository.save(pago));
    }

    public PagoDTOResponse update(Long id, PagoDTORequestPut entidad) {
        Pago existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));

        if(entidad.getPersonaId() != null) {
            Persona persona = findPersonaOrThrow(entidad.getPersonaId());
            existing.setPersona(persona);
        }

        if(entidad.getMetodoPago() != null) {
            existing.setMetodoPago(entidad.getMetodoPago());
        }
        if(entidad.getMonto() != null) {
            existing.setMonto(entidad.getMonto());
        }
        if(entidad.getFecha() != null) {
            existing.setFecha(entidad.getFecha());
        }

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
        repository.delete(pago);
    }

    private Persona findPersonaOrThrow(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con id: " + id));
    }

    public PagoDTOResponse mapToDTO(Pago pago) {
        return PagoDTOResponse.builder()
                .id(pago.getId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .fecha(pago.getFecha())
                .personaId(pago.getPersona().getId())
                .build();
    }
}
