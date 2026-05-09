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

import java.util.Date;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private VentaService ventaService;

    public List<PagoDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public PagoDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));
    }

    public PagoDTOResponse save(PagoDTORequest entidad,String[] populate) {
        Persona persona = findPersonaOrThrow(entidad.getPersonaId());

        Pago pago = Pago.builder()
                .fecha(entidad.getFecha())
                .monto(entidad.getMonto())
                .metodoPago(entidad.getMetodoPago())
                .persona(persona)
                .estado(entidad.getEstado())
                .build();
        Pago saved = repository.save(pago);
        ventaService.enviarMailPago(persona, saved.getMonto(), saved.getMetodoPago(), saved.getFecha() != null ? saved.getFecha() : new Date(), persona.getSaldo());
        return mapToDTO(saved, populate);
    }

    public List<PagoDTOResponse> getByUserId(String userId, String[] populate) {
        return repository.findByPersonaUsuarioIdOrderByFechaDesc(Long.parseLong(userId)).stream()
                .map(p -> mapToDTO(p, populate))
                .toList();
    }

    public PagoDTOResponse update(Long id, PagoDTORequestPut entidad,String[] populate) {
        Pago existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con id: " + id));

        if(entidad.getPersonaId() != null) {
            Persona persona = findPersonaOrThrow(entidad.getPersonaId());
            existing.setPersona(persona);
        }
        if(entidad.getEstado() != null) {
            existing.setEstado(entidad.getEstado());
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

        return mapToDTO(repository.save(existing), populate);
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

    private PagoDTOResponse mapToDTO(Pago pago, String[] populate) {
        return mapToDTOMapper.mapToDTO(pago, populate);
    }

}
