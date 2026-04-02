package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.PagoDTORequest;
import soda_roja.backend.dtoResponse.PagoDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.PagoRepository;

import java.util.List;
@Service

public class PagoService {

    @Autowired
    private PagoRepository repository;
    @Autowired
    private PersonaRepository PersonaRepository;

    public List<PagoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public PagoDTOResponse getById(Long id) {
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
        return mapToDTO(pago);
    }

    public PagoDTOResponse save(PagoDTORequest entidad) {
        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));

        Pago pago = Pago.builder()
                .fecha(entidad.getFecha())
                .monto(entidad.getMonto())
                .metodoPago(entidad.getMetodoPago())
                .persona(persona)
                .build();
        return mapToDTO(repository.save(pago));
    }

    public PagoDTOResponse update(Long id, PagoDTORequest entidad) {
        Pago existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Pago no encontrado con id: " + id));

        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));
        existing.setMetodoPago(entidad.getMetodoPago());
        existing.setMonto(entidad.getMonto());
        existing.setFecha(entidad.getFecha());
        existing.setPersona(persona);


        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
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
