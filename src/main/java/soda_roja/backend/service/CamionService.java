package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import soda_roja.backend.model.Camion;
import soda_roja.backend.repository.CamionRepository;

import org.springframework.stereotype.Service;
import java.util.List;

import soda_roja.backend.dtoRequest.CamionDTORequest;
import soda_roja.backend.dtoResponse.CamionDTOResponse;
@Service
public class CamionService {

    @Autowired
    private CamionRepository repository;
     @Autowired
	 private GastoService gastoService;

    public List<CamionDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CamionDTOResponse getById(Long id) {
    	return repository.findById(id)
				.map(this::mapToDTO)
				.orElseThrow(() -> new RuntimeException("Camion no encontrado con id: " + id));
    }

    public CamionDTOResponse save(CamionDTORequest entidad) {
    		Camion camion = Camion.builder()
					.patente(entidad.getPatente())
					.modelo(entidad.getModelo())
					.marca(entidad.getMarca())
					.kilometraje(entidad.getKilometraje())
					.build();
    	Camion saved = repository.save(camion);
		return mapToDTO(saved);
    }

    public CamionDTOResponse update(Long id, CamionDTORequest  entidad) {
    		Camion existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Camion no encontrado con id: " + id));
    			existing.setPatente(entidad.getPatente());
   				existing.setModelo(entidad.getModelo());
    			existing.setMarca(entidad.getMarca());
    			existing.setKilometraje(entidad.getKilometraje());
    			return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    public CamionDTOResponse mapToDTO(Camion camion) {
		return CamionDTOResponse.builder()
				.id(camion.getId())
				.patente(camion.getPatente())
				.modelo(camion.getModelo())
				.marca(camion.getMarca())
				.kilometraje(camion.getKilometraje())
				.gastos(camion.getGastos() != null ? camion.getGastos().stream().map(gasto -> gastoService.mapToDTO(gasto)).toList() : null)
				.build();
	} 
}
