package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DiaZonaDTORequest;
import soda_roja.backend.dtoRequest.DiaZonaDTORequestWithOrdenes;
import soda_roja.backend.dtoRequestPut.DiaZonaDTORequestPut;
import soda_roja.backend.dtoResponse.DiaZonaDTOResponse;
import soda_roja.backend.model.DiaZona;
import soda_roja.backend.model.DiaZonaOrden;
import soda_roja.backend.repository.DiaZonaRepository;
import soda_roja.backend.repository.ZonaRepository;
import soda_roja.backend.model.Zona;
import soda_roja.backend.repository.DiaRepository;
import soda_roja.backend.model.Dia;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaZonaService {
	@Autowired
	 private DiaRepository diaRepository;
	@Autowired
	 private ZonaRepository zonaRepository;
	@Autowired
		private DiaZonaRepository repository;
    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<DiaZonaDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
    }

    public DiaZonaDTOResponse getById(Long id,String[] populate) {
        return repository.findById(id)
                .map(p -> mapToDTO(p, populate))
                .orElseThrow(() -> new EntityNotFoundException("DiaZona no encontrado con id: " + id));
    }

    public List<DiaZonaDTOResponse> getByZonaIdAndDiaId(Long zonaId, Long diaId, String[] populate) {
        return repository.findByZonaIdAndDiaId(zonaId, diaId)
                .stream()
                .map(p -> mapToDTO(p, populate))
                .toList();
    }

    public List<DiaZonaDTOResponse> getByCamionIdAndDiaId(Long camionId, Long diaId, String[] populate) {
        return repository.findByCamionIdAndDiaId(camionId, diaId)
                .stream()
                .map(p -> mapToDTO(p, populate))
                .toList();
    }

    public DiaZonaDTOResponse save(DiaZonaDTORequest entidad,String[] populate) {
    	Zona zona = findZonaOrThrow(entidad.getZonaId());
    	Dia dia = findDiaOrThrow(entidad.getDiaId());
        DiaZona diaZona = DiaZona.builder()
        		.dia(dia)
        		.zona(zona)
                .build();
        return mapToDTO(repository.save(diaZona), populate);
    }

    public DiaZonaDTOResponse update(Long id, DiaZonaDTORequestPut entidad,String[] populate) {
        DiaZona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaZona no encontrado con id: " + id));
        if(entidad.getZonaId() != null) {
        	Zona zona = findZonaOrThrow(entidad.getZonaId());
			existing.setZona(zona);
		
        }
        if(entidad.getDiaId() != null) {
			Dia dia = findDiaOrThrow(entidad.getDiaId());
			existing.setDia(dia);
		}

        return mapToDTO(repository.save(existing), populate);
    }
    public DiaZonaDTOResponse updateWithOrdenes(Long id, DiaZonaDTORequestWithOrdenes entidad, String[] populate) {
        DiaZona existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaZona no encontrado con id: " + id));
        
        if (entidad.getDiaZonaOrdenes() != null) {
            List<DiaZonaOrden> ordenes = new ArrayList<>();
            //Loopeo la coleección con el nuevo orden
            for (int i = 0; i < entidad.getDiaZonaOrdenes().size(); i++) {
            	//busco por cada DiaZonaOrden del DTO, el orden existente en la base de datos y solo reemplazo el orden
            	//el resto de los campos no se modifican, para evitar problemas con las relaciones y la integridad de los datos
                var dtoOrden = entidad.getDiaZonaOrdenes().get(i);
                DiaZonaOrden ordenExistente = existing.getDiaZonaOrdenes()
                        .stream()
                        .filter(o -> o.getId().equals(dtoOrden.getId()))
                        .findFirst()
                        .orElse(null);
                
                if (ordenExistente != null) {
                    ordenExistente.setOrden(dtoOrden.getOrden());
                    ordenes.add(ordenExistente);
                }
            }
            existing.setDiaZonaOrdenes(ordenes);
        }
        
        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
        DiaZona DiaZona = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiaZona no encontrado con id: " + id));
        repository.delete(DiaZona);
    }

    private Zona findZonaOrThrow(Long id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Zona no encontrada con id: " + id));
    }
    private Dia findDiaOrThrow(Long id) {
        return diaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dia no encontrada con id: " + id));
    }

    private DiaZonaDTOResponse mapToDTO(DiaZona DiaZona, String[] populate) {
        return mapToDTOMapper.mapToDTO(DiaZona, populate);
    }
}
