package soda_roja.backend.service;

import soda_roja.backend.dtoRequestPut.GastoDTORequestPut;
import soda_roja.backend.model.Camion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	@Autowired
	private MapToDTO mapToDTOMapper;

	public Page<GastoDTOResponse> getAll(int page, int size, String[] populate) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
		return repository.findAll(pageable).map(g -> mapToDTO(g, populate));
	}

	public GastoDTOResponse getById(Long id,String[] populate) {
		return repository.findById(id)
				.map(g -> mapToDTO(g, populate))
				.orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id));
	}

	public GastoDTOResponse save(GastoDTORequest entidad,String[] populate) {
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
		return mapToDTO(saved, populate);
	}

	public GastoDTOResponse update(Long id, GastoDTORequestPut entidad,String[] populate) {
		Gasto existing = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id));

		if(entidad.getDetalle() != null) {
			existing.setDetalle(entidad.getDetalle());
		}
		if(entidad.getMonto() != null) {
			existing.setMonto(entidad.getMonto());
		}
		if(entidad.getFecha() != null) {
			existing.setFecha(entidad.getFecha());
		}
		if(entidad.getCamion_id() != null) {
			Camion camion = findCamionOrThrow(entidad.getCamion_id());
			existing.setCamion(camion);
		}

		return mapToDTO(repository.save(existing), populate);
	}

	public void delete(Long id ) {
		Gasto gasto = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Gasto no encontrado con id: " + id));
		repository.delete(gasto);
	}

	private Camion findCamionOrThrow(Long id) {
		return camionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
	}

	private GastoDTOResponse mapToDTO(Gasto gasto, String[] populate) {
		return mapToDTOMapper.mapToDTO(gasto, populate);
	}

}
