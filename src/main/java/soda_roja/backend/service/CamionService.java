package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import soda_roja.backend.dtoRequestPut.CamionDTORequestPut;
import soda_roja.backend.model.Camion;
import soda_roja.backend.model.Domicilio;
import soda_roja.backend.repository.CamionRepository;
import soda_roja.backend.repository.DomicilioRepository;

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
    private GastoService gastoService;
    @Autowired
    private DomicilioRepository DomicilioRepository;
    @Autowired
    private DomicilioService domicilioService;

    public List<CamionDTOResponse> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).toList();
    }

    public CamionDTOResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
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

    public CamionDTOResponse update(Long id, CamionDTORequestPut entidad) {

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

        return mapToDTO(repository.save(existing));
    }

    public void delete(Long id) {
    	Camion existing = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Camion no encontrado con id: " + id));
        repository.delete(existing);
    }
    private Domicilio findDomicilioOrThrow(Long id) {
        return DomicilioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domicilio no encontrado con id: " + id));
    }
    public CamionDTOResponse mapToDTO(Camion camion) {
        return CamionDTOResponse.builder()
                .id(camion.getId())
                .patente(camion.getPatente())
                .modelo(camion.getModelo())
                .marca(camion.getMarca())
                .kilometraje(camion.getKilometraje())
                .gastos(camion.getGastos() != null ? camion.getGastos().stream().map(gasto -> gastoService.mapToDTO(gasto)).toList() : null)
                .Domicilios(camion.getDomicilios() != null
                        ? camion.getDomicilios().stream()
                        .map(Domicilio -> domicilioService.mapToDTO(Domicilio))
                        .toList()
                        : List.of())
                .build();
    }
}
