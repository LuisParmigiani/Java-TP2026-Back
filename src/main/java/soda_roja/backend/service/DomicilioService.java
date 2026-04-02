package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soda_roja.backend.dtoRequest.DomicilioDTORequest;
import soda_roja.backend.dtoResponse.DomicilioDTOResponse;
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
        return repository.findAll();
    }

    public DomicilioDTOResponse getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public DomicilioDTOResponse save(DomicilioDTORequest dto) {
        Zona zona = zonaRepository.findById(dto.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada con id: " + dto.getZonaId()));

        DomicilioDTOResponse domicilio = DomicilioDTOResponse.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .casa(dto.getCasa())
                .zona(zona)
                .build();

        return repository.save(domicilio);
    }

    public DomicilioDTOResponse update(Long id, DomicilioDTORequest entidad) {
        DomicilioDTOResponse existing = repository.findById(id).orElseThrow();
        existing.setCalle(entidad.getCalle());
        existing.setNumero(entidad.getNumero());
        existing.setCasa(entidad.getCasa());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
