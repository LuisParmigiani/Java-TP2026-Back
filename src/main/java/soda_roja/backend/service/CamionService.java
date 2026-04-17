package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequestPut.CamionDTORequestPut;
import soda_roja.backend.model.Camion;
import soda_roja.backend.repository.CamionRepository;

import java.util.List;

import soda_roja.backend.dtoRequest.CamionDTORequest;

import soda_roja.backend.dtoResponse.CamionDTOResponse;
/*
 * Cabe aclarar que las otras excepciones existentes como las Constraint de la base de datos
 * o las de validacion de datos de acuerdo con los objetos DTORequest son manejadas
 * por el global Exception Handler, por lo que no es necesario capturarlas en el service.
 * ¿COMO HACE ESTO SPRING?
 * El controlador tiene el decorador @Valid, @REquestBody CamionDTORequest entidad,
 * lo que hace que Spring valide automáticamente los datos entrantes contra las 
 * restricciones definidas en el DTORequest.
 * Si la validacion falla, Spring lanza una MethodArgumentNotValidException, que es capturada por el 
 * GlobalExceptionHandler.
 * */

@Service
public class CamionService {

    @Autowired
    private CamionRepository repository;

    @Autowired
    private MapToDTO mapToDTOMapper;

    public List<CamionDTOResponse> getAll(String[] populate) {
        return repository.findAll().stream().map(c -> mapToDTO(c, populate)).toList();
    }

    public CamionDTOResponse getById(Long id,String[]populate) {
        return repository.findById(id)
                .map(c -> mapToDTO(c, populate))
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
    }

    public CamionDTOResponse save(CamionDTORequest entidad,String[]populate) {


        Camion camion = Camion.builder()
                .patente(entidad.getPatente())
                .modelo(entidad.getModelo())
                .marca(entidad.getMarca())
                .kilometraje(entidad.getKilometraje())
                .build();
        Camion saved = repository.save(camion);
        return mapToDTO(saved, populate);
    }

    public CamionDTOResponse update(Long id, CamionDTORequestPut entidad,String[]populate) {

        Camion existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con id: " + id));
        if(entidad.getPatente() != null) {
            existing.setPatente(entidad.getPatente());
        }
        if(entidad.getModelo() != null) {
            existing.setModelo(entidad.getModelo());
        }
        if(entidad.getMarca() != null) {
            existing.setMarca(entidad.getMarca());
        }
        if(entidad.getKilometraje() != null) {
            existing.setKilometraje(entidad.getKilometraje());
        }
        if(entidad.getEstado() != null) {
			existing.setEstado(entidad.getEstado());
		}

        return mapToDTO(repository.save(existing), populate);
    }
    
    public CamionDTOResponse disable (Long id,String[]populate) {
        Camion existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
        existing.setEstado(false);
        return mapToDTO(repository.save(existing), populate);
    }

    public void delete(Long id) {
    	Camion existing = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
        repository.delete(existing);
    }
    private CamionDTOResponse mapToDTO(Camion camion, String[] populate) {
        return mapToDTOMapper.mapToDTO(camion, populate);
    }

}
