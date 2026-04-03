package soda_roja.backend.service;
import soda_roja.backend.model.Camion;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import soda_roja.backend.model.Gasto;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.GastoRepository;

import org.springframework.stereotype.Service;
import java.util.List;

import soda_roja.backend.dtoRequest.GastoDTORequest;
import soda_roja.backend.dtoResponse.CamionDTOResponse;
import soda_roja.backend.dtoResponse.GastoDTOResponse;
import soda_roja.backend.service.CamionService;
@Service
public class GastoService {
	@Autowired
	private CamionRepository camionRepository;
    @Autowired
    private GastoRepository repository;

    public List<GastoDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public GastoDTOResponse getById(Long id) {
    	return repository.findById(id)
				.map(this::mapToDTO)
				.orElseThrow(() -> new RuntimeException("Gasto no encontrado con id: " + id));
    }

    public GastoDTOResponse save(GastoDTORequest entidad) {
    	Camion camion = camionRepository.findById(entidad.getCamion_id())
				.orElseThrow(() -> new RuntimeException("Camion no encontrado con id: "));
    	Gasto gasto = Gasto.builder()
    				.detalle(entidad.getDetalle())
    				.monto(entidad.getMonto())
    				.fecha(entidad.getFecha())
    				.camion(camion)
					.build();
    	Gasto saved = repository.save(gasto);
		return mapToDTO(saved);
    }

    public GastoDTOResponse update(Long id, GastoDTORequest  entidad) {
    	Camion camion = camionRepository.findById(entidad.getCamion_id())
    							.orElseThrow(() -> new RuntimeException("Camion no encontrado con id: "));
    	
    	Gasto existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Gasto no encontrado con id: " + id));
    			existing.setDetalle(entidad.getDetalle());
    			existing.setMonto(entidad.getMonto());
    			existing.setFecha(entidad.getFecha());
    			existing.setCamion(camion);
    			return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    
public GastoDTOResponse mapToDTO(Gasto gasto) {
    return GastoDTOResponse.builder()
            .id(gasto.getId())
            .detalle(gasto.getDetalle())
            .monto(gasto.getMonto())
            .fecha(gasto.getFecha())
            .camionId(gasto.getCamion() != null ? gasto.getCamion().getId() : null)
            .build();
}
}
