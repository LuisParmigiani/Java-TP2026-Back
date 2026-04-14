package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.ZonaDTORequest;
import soda_roja.backend.dtoRequestPut.ZonaDTORequestPut;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.ZonaRepository;

import java.util.List;

@Service
public class ZonaService {

    @Autowired
    private ZonaRepository repository;

    public List<ZonaDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public ZonaDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }

    public ZonaDTOResponse save(ZonaDTORequest entidad) {
        Zona zona = Zona.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .build();
        return mapToDTO(repository.save(zona));
    }

    public ZonaDTOResponse update(Long id, ZonaDTORequestPut entidad) {
        Zona zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
        zona.setDetalle(entidad.getDetalle());
        zona.setNombre(entidad.getNombre());
        return mapToDTO(repository.save(zona));
    }

    public void delete(Long id) {
        Zona zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
        repository.delete(zona);
    }

    public ZonaDTOResponse mapToDTO(Zona zona) {
        return ZonaDTOResponse.builder()
                .id(zona.getId())
                .nombre(zona.getNombre())
                .detalle(zona.getDetalle())
                .build();
    }
}
