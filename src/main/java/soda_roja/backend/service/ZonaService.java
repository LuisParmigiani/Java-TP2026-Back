package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.ZonaDTORequest;
import soda_roja.backend.dtoRequestPut.ZonaDTORequestPut;
import soda_roja.backend.dtoResponse.ZonaDTOResponse;
import soda_roja.backend.model.Zona;
import soda_roja.backend.model.Camion;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.repository.CamionRepository;

import java.util.List;

@Service
public class ZonaService {

    @Autowired
    private ZonaRepository repository;
    @Autowired
    private MapToDTO mapToDTOMapper;
    @Autowired
    private CamionRepository camionRepository;

    public List<ZonaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(z -> mapToDTO(z, populate)).toList();
    }

    public ZonaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(z -> mapToDTO(z, populate))
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }

    public ZonaDTOResponse save(ZonaDTORequest entidad,String[] populate) {
    	Camion camion = camionRepository.findById(entidad.getCamionId())
				.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + entidad.getCamionId()));
        Zona zona = Zona.builder()
                .nombre(entidad.getNombre())
                .detalle(entidad.getDetalle())
                .dia(entidad.getDia())
                .camion(camion)
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
        if(entidad.getCamionId() != null) {
			Camion camion = camionRepository.findById(entidad.getCamionId())
					.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id"));
			zona.setCamion(camion);
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
