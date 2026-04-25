package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DiaDomicilioDTORequest;
import soda_roja.backend.dtoRequestPut.DiaDomicilioDTORequestPut;
import soda_roja.backend.dtoResponse.DiaDomicilioDTOResponse;
import soda_roja.backend.model.Dia;
import soda_roja.backend.model.DiaDomicilio;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.DiaDomicilioRepository;
import soda_roja.backend.repository.DiaRepository;
import soda_roja.backend.repository.DomicilioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaDomicilioService {

    @Autowired
    private DiaDomicilioRepository repository;

    @Autowired
    private DiaRepository diaRepository;
    
    @Autowired
    private DomicilioRepository domicilioRepository;
    
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<DiaDomicilioDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public DiaDomicilioDTOResponse getById(Long id, String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("DiaDomicilio no encontrado con id: " + id));
    }

    public DiaDomicilioDTOResponse save(DiaDomicilioDTORequest entidad, String[] populate) {
    	validateEstado(entidad.getEstado());
        Dia dia = findDiaOrThrow(entidad.getDiaId());
        Domicilio domicilio = findDomicilioOrThrow(entidad.getDomicilioId());

        DiaDomicilio diaDomicilio = DiaDomicilio.builder()
                .estado(entidad.getEstado())
                .dia(dia)
                .domicilio(domicilio)
                .build();
        return mapToDTO(repository.save(diaDomicilio), populate);
    }

    public DiaDomicilioDTOResponse update(Long id, DiaDomicilioDTORequestPut entidad, String[] populate) {
        DiaDomicilio existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaDomicilio no encontrado con id: " + id));
		validateEstado(entidad.getEstado());
        if (entidad.getEstado() != null) existing.setEstado(entidad.getEstado());
        if (entidad.getDiaId() != null) {
            Dia dia = findDiaOrThrow(entidad.getDiaId());
            existing.setDia(dia);
        }
        if (entidad.getDomicilioId() != null) {
            Domicilio domicilio = findDomicilioOrThrow(entidad.getDomicilioId());
            existing.setDomicilio(domicilio);
        }

        return mapToDTO(repository.save(existing), populate);
    }

    public List<DiaDomicilioDTOResponse> updateMultiple(List<DiaDomicilioDTORequestPut> entidades, String[] populate) {
        List<DiaDomicilio> diaDomicilios = new ArrayList<>();
        
        for (DiaDomicilioDTORequestPut entidad : entidades) {
            DiaDomicilio existing = repository.findById(entidad.getId())
                    .orElseThrow(() -> new EntityNotFoundException("DiaDomicilio no encontrado con id: " + entidad.getId()));
            
            validateEstado(entidad.getEstado());
            if (entidad.getEstado() != null) existing.setEstado(entidad.getEstado());
            if (entidad.getDiaId() != null) {
                Dia dia = findDiaOrThrow(entidad.getDiaId());
                existing.setDia(dia);
            }
            if (entidad.getDomicilioId() != null) {
                Domicilio domicilio = findDomicilioOrThrow(entidad.getDomicilioId());
                existing.setDomicilio(domicilio);
            }
            
            diaDomicilios.add(existing);
        }
        
        return repository.saveAll(diaDomicilios).stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public void delete(Long id) {
        DiaDomicilio diaDomicilio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaDomicilio no encontrado con id: " + id));
        repository.delete(diaDomicilio);
    }

    private Dia findDiaOrThrow(Long id) {
        return diaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + id));
    }

    private Domicilio findDomicilioOrThrow(Long id) {
        return domicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }
    
    private void validateEstado(String estado) {
        if (estado != null && !estado.matches("^(ACTIVO|INACTIVO)$")) {
            throw new IllegalArgumentException("El estado debe ser 'ACTIVO' o 'INACTIVO', recibido: " + estado);
        }
    }
    private DiaDomicilioDTOResponse mapToDTO(DiaDomicilio diaDomicilio, String[] populate) {
        return mapToDTOMapper.mapToDTO(diaDomicilio, populate);
    }
}
