package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequest.DiaDTORequest;
import soda_roja.backend.dtoResponse.DiaDTOResponse;
import soda_roja.backend.model.Dia;
import soda_roja.backend.repository.DiaRepository;

import java.util.List;

@Service
public class DiaService {

	@Autowired
	private DiaRepository repository;

	@Autowired
	private MapToDTO mapToDTOMapper;

	public List<DiaDTOResponse> getAll(String[] populate) {
		return repository.findAll().stream().map(p -> mapToDTO(p, populate)).toList();
	}

	public DiaDTOResponse getById(Long id, String[] populate) {
		return repository.findById(id).map(p -> mapToDTO(p, populate))
				.orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + id));
	}

	public DiaDTOResponse save(DiaDTORequest entidad, String[] populate) {

		Dia dia = Dia.builder().nombre(entidad.getNombre()).build();
		return mapToDTO(repository.save(dia), populate);
	}

	public DiaDTOResponse update(Long id, DiaDTORequest entidad, String[] populate) {
		Dia existing = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + id));
		if (entidad.getNombre() != null) {
			existing.setNombre(entidad.getNombre());
		}


		return mapToDTO(repository.save(existing), populate);
	}

	public void delete(Long id) {
		Dia dia = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Dia no encontrado con id: " + id));
		repository.delete(dia);
	}

	private DiaDTOResponse mapToDTO(Dia dia, String[] populate) {
		return mapToDTOMapper.mapToDTO(dia, populate);
	}

}
