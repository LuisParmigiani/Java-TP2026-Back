package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.PersonaDomicilioDTORequest;
import soda_roja.backend.dtoResponse.PersonaDomicilioDTOResponse;
import soda_roja.backend.model.*;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.PersonaRepository;
import soda_roja.backend.repository.PersonaDomicilioRepository;
import soda_roja.backend.repository.VentaRepository;

import java.util.List;
@Service

public class PersonaDomicilioService {

    @Autowired
    private PersonaDomicilioRepository repository;
    @Autowired
    private PersonaRepository PersonaRepository;
    @Autowired
    private DomicilioRepository DomicilioRepository;
    @Autowired
    private VentaRepository VentaRepository;

    public List<PersonaDomicilioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public PersonaDomicilioDTOResponse getById(Long id) {
        PersonaDomicilio usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonaDomicilio no encontrado con id: " + id));
        return mapToDTO(usuario);
    }

    public PersonaDomicilioDTOResponse save (PersonaDomicilioDTORequest entidad) {
        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
            .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));
        Domicilio domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
            .orElseThrow(() -> new RuntimeException("Domicilio no encontrado con id: " + entidad.getDomicilioId()));
        PersonaDomicilio personaDomicilio = PersonaDomicilio.builder()
                .persona(persona)
                .domicilio(domicilio)
                .dia(entidad.getDia())
                .build();
        return mapToDTO(repository.save(personaDomicilio));
    }

    public PersonaDomicilioDTOResponse update(Long id,PersonaDomicilioDTORequest entidad) {
        PersonaDomicilio existing = repository.findById(id).orElseThrow(()-> new RuntimeException("PersonaDomicilio no encontrado con id: " + id));

        Persona persona = PersonaRepository.findById(entidad.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + entidad.getPersonaId()));
         Domicilio domicilio = DomicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado con id: " + entidad.getDomicilioId()));

        existing.setPersona(persona);
        existing.setDia(entidad.getDia());
        existing.setDomicilio(domicilio);


        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public PersonaDomicilioDTOResponse mapToDTO(PersonaDomicilio personaDomicilio) {

        return PersonaDomicilioDTOResponse.builder()
                .id(personaDomicilio.getId())
                .dia(personaDomicilio.getDia())
                .personas(new PersonaService().mapToDTO(personaDomicilio.getPersona()))
                .domicilios(new DomicilioService().mapToDTO(personaDomicilio.getDomicilio()))
                .ventas(personaDomicilio.getVentas() != null
                        ? personaDomicilio.getVentas().stream()
                          .map(venta -> new VentaService().mapToDTO(venta))
                          .toList()
                        : List.of()) // Return an empty list if ventas is null
                .build();
    }
}
