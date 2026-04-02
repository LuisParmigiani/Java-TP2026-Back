package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.DomicilioRepository;
import soda_roja.backend.repository.ZonaRepository;

import java.util.List;
@Service

public class DomicilioService {

    @Autowired
    private DomicilioRepository repository;
    @Autowired
    private ZonaRepository zonaRepository;

    public List<DomicilioDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public DomicilioDTOResponse getById(Long id) {
        Domicilio domicilio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado con id: " + id));
        return mapToDTO(domicilio);
    }

    public DomicilioDTOResponse save(DomicilioDTORequest dto) {
        Zona zona = zonaRepository.findById(dto.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + dto.getZonaId()));

        Domicilio domicilio = Domicilio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .zona(zona)
                .build();

        return mapToDTO(repository.save(domicilio));
    }

    public DomicilioDTOResponse update(Long id, DomicilioDTORequest entidad) {
        Domicilio existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Domicilio no encontrado con id: " + id));

         Zona zona = zonaRepository.findById(entidad.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + entidad.getZonaId()));
        existing.setCalle(entidad.getCalle());
        existing.setNumero(entidad.getNumero());
        existing.setCasa(entidad.getCasa());
        existing.setZona(zona);

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    private DomicilioDTOResponse mapToDTO(Domicilio domicilio) {
        return DomicilioDTOResponse.builder()
                .id(domicilio.getId())
                .calle(domicilio.getCalle())
                .numero(domicilio.getNumero())
                .casa(domicilio.getCasa())
                .zona(domicilio.getZona())
                .build();
    }
}

