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
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<ZonaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(z -> mapToDTO(z, populate)).toList();
    }

    public ZonaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(z -> mapToDTO(z, populate))
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }

    public ZonaDTOResponse save(ZonaDTORequest entidad,String[] populate) {
        Zona zona = Zona.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .dia(entidad.getDia())
                .build();
        return mapToDTO(repository.save(zona), populate);
    }

    public ZonaDTOResponse update(Long id, ZonaDTORequestPut entidad,String[] populate) {
        Zona zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));

        if(entidad.getNombre() != null) {
            zona.setNombre(entidad.getNombre());
        }
        if(entidad.getDetalle() != null) {
            zona.setDetalle(entidad.getDetalle());
        }
        if (entidad.getDia() != null) {
            zona.setDia(entidad.getDia());
        }

        return mapToDTO(repository.save(zona), populate);
    }

    public void delete(Long id) {
        Zona zona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
        repository.delete(zona);
    }

    private ZonaDTOResponse mapToDTO(Zona zona, String[] populate) {
        return mapToDTOMapper.mapToDTO(zona, populate);
    }

}
