package soda_roja.backend.service;

import soda_roja.backend.model.Camion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.model.Gasto;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.GastoRepository;
import java.util.List;
import soda_roja.backend.dtoRequest.GastoDTORequest;
import soda_roja.backend.dtoResponse.GastoDTOResponse;

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
				.orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id));
	}

	public GastoDTOResponse save(GastoDTORequest entidad) {
		Camion camion = entidad.getCamion_id() != null 
		        ? findCamionOrThrow(entidad.getCamion_id()) 
		        : null;	
				Gasto gasto = Gasto.builder()
				.detalle(entidad.getDetalle())
				.monto(entidad.getMonto())
				.fecha(entidad.getFecha())
				.camion(camion)
				.build();
		Gasto saved = repository.save(gasto);
		return mapToDTO(saved);
	}

	public GastoDTOResponse update(Long id, GastoDTORequest entidad) {
		Camion camion = entidad.getCamion_id() != null 
		        ? findCamionOrThrow(entidad.getCamion_id()) 
		        : null;	
		Gasto existing = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id));
		existing.setDetalle(entidad.getDetalle());
		existing.setMonto(entidad.getMonto());
		existing.setFecha(entidad.getFecha());
		existing.setCamion(camion);
		return mapToDTO(repository.save(existing));
	}

	public void delete(Long id) {
		Gasto gasto = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id));
		repository.delete(gasto);
	}

	private Camion findCamionOrThrow(Long id) {
		return camionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
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
