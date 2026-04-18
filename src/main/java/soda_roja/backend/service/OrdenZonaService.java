package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.OrdenZonaDTORequest;
import soda_roja.backend.dtoRequestPut.OrdenZonaDTORequestPut;
import soda_roja.backend.dtoResponse.OrdenZonaDTOResponse;
import soda_roja.backend.model.OrdenZona;
import soda_roja.backend.model.Zona;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.OrdenZonaRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.repository.DomicilioRepository;

import java.util.List;

@Service
public class OrdenZonaService {

    @Autowired
    private OrdenZonaRepository repository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<OrdenZonaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(oz -> mapToDTO(oz, populate)).toList();
    }

    public OrdenZonaDTOResponse getById(Long id, String[] populate) {
        return repository.findById(id)
                .map(oz -> mapToDTO(oz, populate))
                .orElseThrow(() -> new EntityNotFoundException("OrdenZona no encontrada con id: " + id));
    }

    public OrdenZonaDTOResponse save(OrdenZonaDTORequest entidad, String[] populate) {
        Zona zona = zonaRepository.findById(entidad.getZonaId())
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + entidad.getZonaId()));

        Domicilio domicilio = domicilioRepository.findById(entidad.getDomicilioId())
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + entidad.getDomicilioId()));

        OrdenZona ordenZona = OrdenZona.builder()
                .dia(entidad.getDia())
                .orden(entidad.getOrden())
                .zona(zona)
                .domicilio(domicilio)
                .build();

        return mapToDTO(repository.save(ordenZona), populate);
    }

    public OrdenZonaDTOResponse update(Long id, OrdenZonaDTORequestPut entidad, String[] populate) {
        OrdenZona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrdenZona no encontrada con id: " + id));

        if(entidad.getDia() != null && entidad.getDia() != existing.getDia() && entidad.getDia() <=6 && entidad.getDia() >= 0) {
            existing.setDia(entidad.getDia());
        }
        if(entidad.getOrden() != null) {
            existing.setOrden(entidad.getOrden());
        }
        if(entidad.getZonaId() != null) {
            Zona zona = zonaRepository.findById(entidad.getZonaId())
                    .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + entidad.getZonaId()));
            existing.setZona(zona);
        }
        if(entidad.getDomicilioId() != null) {
            Domicilio domicilio = domicilioRepository.findById(entidad.getDomicilioId())
                    .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + entidad.getDomicilioId()));
            existing.setDomicilio(domicilio);
        }

        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        OrdenZona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrdenZona no encontrada con id: " + id));
        repository.delete(existing);
    }

    private OrdenZonaDTOResponse mapToDTO(OrdenZona ordenZona, String[] populate) {
        return mapToDTOMapper.mapToDTO(ordenZona, populate);
    }
}
