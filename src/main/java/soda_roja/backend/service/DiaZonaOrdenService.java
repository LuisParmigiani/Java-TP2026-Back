package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DiaZonaOrdenDTORequest;
import soda_roja.backend.dtoRequestPut.DiaZonaOrdenDTORequestPut;
import soda_roja.backend.dtoResponse.DiaZonaOrdenDTOResponse;
import soda_roja.backend.model.DiaZona;
import soda_roja.backend.model.DiaZonaOrden;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.DiaZonaOrdenRepository;
import soda_roja.backend.repository.DiaZonaRepository;
import soda_roja.backend.repository.DomicilioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaZonaOrdenService {

    @Autowired
    private DiaZonaOrdenRepository repository;

    @Autowired
    private DiaZonaRepository diaZonaRepository;
    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<DiaZonaOrdenDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public DiaZonaOrdenDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("DiaZonaOrden no encontrado con id: " + id));
    }

    public DiaZonaOrdenDTOResponse save(DiaZonaOrdenDTORequest entidad,String[] populate) {
        DiaZona diaZona = findDiaZonaOrThrow(entidad.getDiaZonaId());
        Domicilio domicilio = findDomicilioOrThrow(entidad.getDomicilioId());

        DiaZonaOrden diaZonaOrden = DiaZonaOrden.builder()
        		.orden(entidad.getOrden())
        		.diaZona(diaZona)
        		.domicilio(domicilio)
                .build();
        return mapToDTO(repository.save(diaZonaOrden), populate);
    }

    public DiaZonaOrdenDTOResponse update(Long id, DiaZonaOrdenDTORequestPut entidad,String[] populate) {
        DiaZonaOrden existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaZonaOrden no encontrado con id: " + id));

        DiaZona diaZona = findDiaZonaOrThrow(entidad.getDiaZonaId());
        Domicilio domicilio = findDomicilioOrThrow(entidad.getDomicilioId());
        //Solo checkeo esto porque si yo pondria una restricción de que el orden es único, no los podria ordenar a todos
        //al mismo tiempo
        if (entidad.getOrden() != null) existing.setOrden(entidad.getOrden());

        return mapToDTO(repository.save(existing), populate);
    }
    public List<DiaZonaOrdenDTOResponse> updateMultiple(List<DiaZonaOrdenDTORequestPut> entidades, String[] populate) {
        List<DiaZonaOrden> ordenes = new ArrayList<>();
        
        for (DiaZonaOrdenDTORequestPut entidad : entidades) {
            DiaZonaOrden existing = repository.findById(entidad.getId())
                    .orElseThrow(() -> new EntityNotFoundException("DiaZonaOrden no encontrado con id: " + entidad.getId()));
            
            DiaZona diaZona = findDiaZonaOrThrow(entidad.getDiaZonaId());
            Domicilio domicilio = findDomicilioOrThrow(entidad.getDomicilioId());
            
            existing.setOrden(entidad.getOrden());
            existing.setDiaZona(diaZona);
            existing.setDomicilio(domicilio);
            
            ordenes.add(existing);
        }
        
        return repository.saveAll(ordenes).stream().map(p -> mapToDTO(p, populate)).toList();
    }


    public void delete(Long id) {
        DiaZonaOrden DiaZonaOrden = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaZonaOrden no encontrado con id: " + id));
        repository.delete(DiaZonaOrden);
    }

    private DiaZona findDiaZonaOrThrow(Long id) {
        return diaZonaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaZona no encontrada con id: " + id));
    }

    private Domicilio findDomicilioOrThrow(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }
    private DiaZonaOrdenDTOResponse mapToDTO(DiaZonaOrden DiaZonaOrden, String[] populate) {
        return mapToDTOMapper.mapToDTO(DiaZonaOrden, populate);
    }

}
